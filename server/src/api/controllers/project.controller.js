const loggerLib = require('../../lib/logger');
const { getProjects, createProject } = require('../../lib/project');

const list = async (req, res, next) => {
    try {
        const listedProjects = await getProjects(req.user.id);
        res.send(listedProjects);
        loggerLib.log('info', '/project - list', 'Listed projects', req);
    } catch (error) {
        loggerLib.log('error', '/project - list', error, req);
        next(error);
    }
};

const create = async (req, res, next) => {
    const { name, language } = req.body;
    try {
        const created = await createProject({ name, language, user: req.user.id });
        res.send(created);
        loggerLib.log('info', '/project - create', `Created project id: ${created.id}`, req);
    } catch (error) {
        loggerLib.log('error', '/project - create', error, req);
        next(error);
    }
};

module.exports.list = list;
module.exports.create = create;