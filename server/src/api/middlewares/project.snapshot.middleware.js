const loggerLib = require('../../lib/logger');


const validateFile = (req, res, next) => {
    try {
        if (req.files && req.files.data) {
            return next();
        }
        throw new Error('ERR_NO_SNAPSHOT');
    } catch (error) {
        next(error);
    }
}
const checkSize = (req, res, next) => { // this should be a middleware
    const { data } = req.files;
    try {
        if (Array.isArray(data)) {
            for (let i = 0; i < data.length; i++) {
                const newData = data[i];
                if (newData.size > parseInt(process.env.FILE_UPLOAD_MAX_BYTES)) {
                    throw new Error('ERR_SNAPSHOT_DATA_MAX_EXCEED');
                }
                loggerLib.log('info', '/project/snapshot - middleware checkSize', `File Bytes: ${newData.size}`, req);
            }
            return;
        }
        if (data.size > parseInt(process.env.FILE_UPLOAD_MAX_BYTES)) {
            throw new Error('ERR_SNAPSHOT_DATA_MAX_EXCEED');
        }
        loggerLib.log('info', '/project/snapshot - middleware checkSize', `File Bytes: ${data.size}`, req);
        next();
    } catch (error) {
        loggerLib.log('error', '/project/snapshot - middleware checkSize', error.message, error.stack, req);
        next(error);
    }
}

module.exports.validateFile = validateFile;
module.exports.checkSize = checkSize;
