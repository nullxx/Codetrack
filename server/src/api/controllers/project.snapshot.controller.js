const loggerLib = require('../../lib/logger');
const { createSnapshot, createMultipleSnapshots } = require('../../lib/project');

const create = async (req, res, next) => {
    const { project, localPath } = req.body;
    const { data } = req.files;

    try {
        let createdSnapshot;
        if (Array.isArray(data)) {
            createdSnapshot = await createMultipleSnapshots({ fileDatas: data, project, localPath, user: req.user.id });
        } else {
            createdSnapshot = await createSnapshot({ fileData: data, project, localPath, user: req.user.id });
        }
        res.send({ code: 1, data: createdSnapshot });
        loggerLib.log('info', '/project/snapshot - create', `Created snapshot: ${JSON.stringify(createdSnapshot)}`, req);
    } catch (error) {
        loggerLib.log('error', '/project/snapshot - create', error, req);
        next(error);
    }
};

module.exports.create = create;