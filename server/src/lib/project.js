const zlib = require('zlib');
const AdmZip = require('adm-zip');
const loggerLib = require('./logger');
const collisionLib = require('./collision');
const hashLib = require('./hash');
const storageLib = require('./storage');
const DB = require('./database');
const { projectUtils: { checkChanged } } = require('../utils');


const getProjects = async (userId) => {
    const Project = DB.getConn().models.projects;

    const userProjects = await Project.findAll({
        where: {
            user: userId
        },
    });
    return userProjects;
}

const getProject = async (projectId) => {
    const Project = DB.getConn().models.projects;

    const userProject = await Project.findOne({
        where: {
            id: projectId
        },
    });
    return userProject;
}
// TODO check if exists foreign key will exist before insert a row that references to that table
const createProject = async ({ name, language, user }) => {
    const Project = DB.getConn().models.projects;

    const createdProject = await Project.create({
        name,
        language,
        user,
    });
    return createdProject;
}

const updateProject = async ({ name, isAllowed, project, user }) => {
    const Project = DB.getConn().models.projects;
    const toUpdate = checkChanged({ name, isAllowed });

    const updatedProject = await Project.update(
        toUpdate,
        {
            where: {
                id: project,
                user,
            }
        }
    );
    return updatedProject[0] === 1;
}

// 'localPath' is the base path for the file in 'fileData'
const createSnapshot = async ({ user, project, fileData, localPath, natures }) => {
    const Snapshot = DB.getConn().models.snapshots;

    const projectFile = await __checkProjectFilesLocalPath(`${localPath}/${fileData.name}`, project);
    const createdFile = await __createFile(user, fileData);

    const createdSnapshot = await Snapshot.create({
        projectFile: projectFile.dataValues.id,
        file: createdFile.dataValues.id,
        natures
    });

    return createdSnapshot;
}

// 'localPath' is the base path for ALL the files in 'fileDatas'
const createMultipleSnapshots = async ({ user, project, fileDatas, localPath, natures }) => {
    const Snapshot = DB.getConn().models.snapshots;
    const toInsertFiles = [];
    for (let i = 0; i < fileDatas.length; i++) {
        const fileData = fileDatas[i];
        const projectFile = await __checkProjectFilesLocalPath(`${localPath}/${fileData.name}`, project);
        const createdFile = await __createFile(user, fileData);

        toInsertFiles.push({
            projectFile: projectFile.dataValues.id,
            file: createdFile.dataValues.id,
            natures,
        })
    }

    const createdSnapshots = await Snapshot.bulkCreate(toInsertFiles, { individualHooks: true }); // is this permforance best? invidivudalHooks is like running loop and insert single query
    return createdSnapshots;
}

/**
 * List of buffers of last snapshot of a requested project
 * @param {number} project 
 * @param {boolean} compressed zip file will be returned
 */
const getFiles = async (project, compressed) => {
    const compressedFile = new AdmZip();
    const files = []
    const ProjectFiles = DB.getConn().models.projectFiles;
    const Snapshots = DB.getConn().models.snapshots;
    const projectFiles = await ProjectFiles.findAll({
        where: {
            project
        }
    });
    for (let i = 0; i < projectFiles.length; i++) {
        const projectFile = projectFiles[i];
        const snapshot = await Snapshots.findAll({
            where: {
                projectFile: projectFile.id
            },
            limit: 1,
            order: [
                ["createdAt", "DESC"]
            ]
        });
        if (snapshot.length == 1) {
            const file = await __retrieveFile(snapshot[0].file);
            if (compressed) {
                compressedFile.addFile(projectFile.localFilePath, file.buffer);
            } else {
                files.push(file);
            }
        }
    }
    return compressedFile.toBuffer();
}

const __retrieveFile = async (id) => {
    const Files = DB.getConn().models.files;
    const file = await Files.findOne({
        where: {
            id
        }
    });
    if (!file) throw new Error('ERR_FILE_NOT_FOUND');
    const deflatedBase64File = storageLib.read(file.path)
    const decodedFileDeflatedFile = Buffer.from(deflatedBase64File, 'base64');
    const inflatedFile = zlib.inflateSync(decodedFileDeflatedFile);
    return { name: file.name, buffer: inflatedFile };
}
/**
 * Creates a file instance  => saves data to DB and to filesystem
 * @param {string} lastFolder last nested path
 * @param {object} fileData { name, mimetype, truncated, size, md5, data }
 */
const __createFile = async (lastFolder, { name, mimetype, truncated, size, md5, data }) => {
    const Files = DB.getConn().models.files;

    const sha512 = hashLib.createSHA512(data);
    const savedPathFolder = `${process.env.BASE_FOLDER_SNAPSHOT}/${new Date().toISOString().slice(0, 10)}/${lastFolder}`;
    const fileName = Date.now();
    const nextStoredPath = storageLib.calcSavePath(savedPathFolder, { metadata: { fileName } });

    const createdFile = await Files.findOrCreate({
        defaults: {
            name,
            mimetype,
            truncated,
            size,
            md5,
            sha512,
            path: nextStoredPath,
        },
        where: {
            md5,
            sha512
        }
    });

    const compressed = zlib.deflateSync(data).toString('base64');
    let toReturn = createdFile[0];

    loggerLib.log('debug', `CreatedFile DB alreadyExists: ${!createdFile[createdFile.length - 1]}`);

    if (createdFile[createdFile.length - 1] === true) {
        const stored = storageLib.save(savedPathFolder, { metadata: { fileName }, data: compressed });
        loggerLib.log('debug', `CreatedFile saving to filesystem result: ${JSON.stringify(stored)}`);
    } else {
        for (let i = 0; i < createdFile.length - 1; i++) { // createdFile.length - 1 because last is a boolean
            const existingFileRecord = createdFile[i];

            const existingFilePath = existingFileRecord.path;
            const existingStored = storageLib.read(existingFilePath);
            const equalFiles = collisionLib.areEqual(existingStored, compressed);

            loggerLib.log('debug', `Collision happened: ${!equalFiles}`);

            if (!equalFiles) { // if this hash was in the database and the files are not the same COLLISION happens
                const Collisions = DB.getConn().models.collisions;
                loggerLib.log('debug', `COLLISION DETECTED at ${JSON.stringify(existingFileRecord)}`);

                const storedCollision = storageLib.save(savedPathFolder, { metadata: { fileName }, data: compressed });
                loggerLib.log('debug', `Save to filesystem file collision result: ${JSON.stringify(storedCollision)}`);

                const savedCollisionedFile = await Files.create({
                    name,
                    mimetype,
                    truncated,
                    size,
                    md5,
                    sha512,
                    path: storedCollision.path,
                });
                loggerLib.log('debug', `Save collision file to DB ${JSON.stringify(savedCollisionedFile)}`);
                toReturn = savedCollisionedFile;

                const savedCollision = await Collisions.create({
                    exFile: existingFileRecord.id,
                    withFile: savedCollisionedFile.id,
                });
                loggerLib.log('debug', `Save collision record to DB ${JSON.stringify(savedCollision)}`);
            }
        }
    }

    return toReturn;
}
/**
 * Check if client localFilePath is existing. If not exists creates new record
 * @param {string} localFilePath client file path
 * @param {number} project
 * @returns {ProjectFiles} projectFile (new or existing)
 */
const __checkProjectFilesLocalPath = async (localFilePath, project) => {
    const ProjectFiles = DB.getConn().models.projectFiles;
    const projectFilesResult = await ProjectFiles.findAll({
        where: {
            project,
            localFilePath,
        }
    });
    if (projectFilesResult.length === 1) {
        return projectFilesResult[0];
    } else if (projectFilesResult.length === 0) {
        const createdProjectFiles = await ProjectFiles.create({
            project,
            localFilePath,
        });
        return createdProjectFiles;
    }
}

module.exports.getProjects = getProjects;
module.exports.getProject = getProject;
module.exports.createProject = createProject;
module.exports.updateProject = updateProject;
module.exports.createSnapshot = createSnapshot;
module.exports.createMultipleSnapshots = createMultipleSnapshots;
module.exports.getFiles = getFiles;
