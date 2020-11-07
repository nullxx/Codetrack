const DB = require('../lib/database');

const getProjects = async (userId) => {
    const Project = DB.getConn().models.projects;

    const userProjects = await Project.findAll({
        where: {
            user: userId
        }
    });
    return userProjects;
}

const createProject = async ({ name, language, user }) => {
    const Project = DB.getConn().models.projects;

    const createdProject = await Project.create({
        name,
        language,
        user,
    });
    return createdProject;
}

const createSnapshot = async ({ user, project, data }) => {
    const Snapshot = DB.getConn().models.snapshots;

    const createdSnapshot = await Snapshot.create({
        user,
        project,
        data,
    });
    return createdSnapshot;
}

module.exports.getProjects = getProjects;
module.exports.createProject = createProject;
module.exports.createSnapshot = createSnapshot;
