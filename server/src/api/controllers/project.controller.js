const loggerLib = require('../../lib/logger');
const { getProjects, createProject, updateProject, getProject } = require('../../lib/project');

const list = async (req, res, next) => {
    try {
        const listedProjects = await getProjects(req.user.id);
        res.send({ code: 1, data: listedProjects });
        loggerLib.log('info', '/project - list', 'Listed projects', req);
    } catch (error) {
        loggerLib.log('error', '/project - list', error, req);
        next(error);
    }
};

const getOne = async (req, res, next) => {
    try {
        const { project } = req.params;
        const userProject = await getProject(project);
        res.send({ code: 1, data: userProject });
        loggerLib.log('info', '/project - getOne', 'Get one project', req);
    } catch (error) {
        loggerLib.log('error', '/project - getOne', error, req);
        next(error);
    }
};

const create = async (req, res, next) => {
    const { name, language } = req.body;
    try {
        const created = await createProject({ name, language, user: req.user.id });
        res.send({ code: 1, data: created });
        loggerLib.log('info', '/project - create', `Created project id: ${created.id}`, req);
    } catch (error) {
        loggerLib.log('error', '/project - create', error, req);
        next(error);
    }
};


const update = async (req, res, next) => {
    const { name, isAllowed, project } = req.body;
    try {
        const updated = await updateProject({ name, isAllowed, project, user: req.user.id });
        res.send({ code: updated ? 1 : 0 });
        loggerLib.log('info', '/project - update', `Updated project: ${updated}`, req);
    } catch (error) {
        loggerLib.log('error', '/project - update', error, req);
        next(error);
    }
};

module.exports.list = list;
module.exports.create = create;
module.exports.update = update;
module.exports.getOne = getOne;
