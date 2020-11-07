const loggerLib = require('../../lib/logger');

const { createSnapshot, createMultipleSnapshots } = require('../../lib/project');


const __checkSize = (data, req) => { // this should be a middleware
    if (Array.isArray(data)) {
        for (let i = 0; i < data.length; i++) {
            const newData = data[i];
            if (newData.size > parseInt(process.env.FILE_UPLOAD_MAX_BYTES)) {
                throw new Error('ERR_SNAPSHOT_DATA_MAX_EXCEED');
            }
            loggerLib.log('info', '/project/snapshot - create', `File Bytes: ${newData.size}`, req);
        }
        return;
    }
    if (data.size > parseInt(process.env.FILE_UPLOAD_MAX_BYTES)) {
        throw new Error('ERR_SNAPSHOT_DATA_MAX_EXCEED');
    }
    loggerLib.log('info', '/project/snapshot - create', `File Bytes: ${data.size}`, req);
}
const create = async (req, res, next) => {
    // TODO validate file input
    const { project, localPath } = req.body;
    const { data } = req.files;
    try {
        __checkSize(data, req);

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