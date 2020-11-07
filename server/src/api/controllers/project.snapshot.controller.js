const loggerLib = require('../../lib/logger');
const zlib = require('zlib');

const { createSnapshot } = require('../../lib/project');

const create = async (req, res, next) => {
    const { project } = req.body;
    const { data } = req.files;
    try {
        loggerLib.log('info', '/project/snapshot - create', `File Bytes: ${data.size}`, req);
        if (data.size > parseInt(process.env.FILE_UPLOAD_MAX_BYTES)) {
            throw new Error('ERR_SNAPSHOT_DATA_MAX_EXCEED');
        }
        const compressed = zlib.deflateSync(data.data).toString('base64');
        res.send(await createSnapshot({ data: compressed, project, user: req.user.id }));
        loggerLib.log('info', '/project/snapshot - create', `Inserted file with bytes ${data.size}`, req);
    } catch (error) {
        loggerLib.log('error', '/project/snapshot - create', error, req);
        next(error);
    }
};

module.exports.create = create;