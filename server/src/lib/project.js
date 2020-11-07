const zlib = require('zlib');
const loggerLib = require('./logger');
const collisionLib = require('./collision');
const hashLib = require('./hash');
const storageLib = require('./storage');
const DB = require('./database');

const getProjects = async (userId) => {
    const Project = DB.getConn().models.projects;

    const userProjects = await Project.findAll({
        where: {
            user: userId
        }
    });
    return userProjects;
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

// 'localPath' is the base path for the file in 'fileData'
const createSnapshot = async ({ user, project, fileData, localPath }) => {
    const Snapshot = DB.getConn().models.snapshots;

    const projectFile = await __checkProjectFilesLocalPath(`${localPath}/${fileData.name}`, project);
    const createdFile = await __createFile(user, fileData);

    const createdSnapshot = await Snapshot.create({
        projectFile: projectFile.dataValues.id,
        file: createdFile.dataValues.id,
    });

    return createdSnapshot;
}

// 'localPath' is the base path for ALL the files in 'fileDatas'
const createMultipleSnapshots = async ({ user, project, fileDatas, localPath }) => {
    const Snapshot = DB.getConn().models.snapshots;
    const toInsertFiles = [];
    for (let i = 0; i < fileDatas.length; i++) {
        const fileData = fileDatas[i];
        const projectFile = await __checkProjectFilesLocalPath(`${localPath}/${fileData.name}`, project);
        const createdFile = await __createFile(user, fileData);

        toInsertFiles.push({
            projectFile: projectFile.dataValues.id,
            file: createdFile.dataValues.id,
        })
    }

    const createdSnapshots = await Snapshot.bulkCreate(toInsertFiles, { individualHooks: true }); // is this permforance best? invidivudalHooks is like running loop and insert single query
    return createdSnapshots;
}

/**
 * Creates a file instance  => saves data to DB and to filesystem
 * @param {string} lastFolder last nested path
 * @param {object} fileData { name, mimetype, truncated, size, md5, data }
 */
const __createFile = async (lastFolder, { name, mimetype, truncated, size, md5, data }) => {
    const Files = DB.getConn().models.files;

    const sha512 = hashLib.createSHA512(data);
    const savedPathFolder = `${process.env.BASE_FOLDER_SNAPSHOT}/${new Date().toLocaleDateString()}/${lastFolder}`;
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
    if (createdFile[1] === true) {
        const stored = storageLib.save(savedPathFolder, { metadata: { fileName }, data: compressed });
        loggerLib.log('debug', `CreatedFile saving to filesystem result: ${JSON.stringify(stored)}`);
    } else {
        const existingFilePath = createdFile[0].path;
        const existingStored = storageLib.read(existingFilePath);
        const equalFiles = collisionLib.areEqual(existingStored, compressed);
        loggerLib.log('debug', `Collision check result: ${equalFiles}`);
        if (equalFiles) {
            const Collisions = DB.getConn().models.collisions;
            loggerLib.log('debug', `COLLISION DETECTED at ${JSON.stringify(createdFile[0])}`);

            const stored = storageLib.save(savedPathFolder, { metadata: { fileName }, data: compressed });
            const savedCollisionedFile = await Files.create({
                name,
                mimetype,
                truncated,
                size,
                md5,
                sha512,
                path: stored.path,
            });

            await Collisions.create({
                exFile: createdFile[0].id,
                withFile: savedCollisionedFile.id,
            });
        }
    }
    loggerLib.log('debug', `CreatedFile DB alreadyExists: ${!createdFile[1]}`);
    return createdFile.length === 2 ? createdFile[0] : false;
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
module.exports.createProject = createProject;
module.exports.createSnapshot = createSnapshot;
module.exports.createMultipleSnapshots = createMultipleSnapshots;
