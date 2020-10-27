var fs = require('fs');
const logPath = 'log';
if (!fs.existsSync(logPath)) {
    fs.mkdirSync(logPath);
}

const SimpleNodeLogger = require('simple-node-logger');

const log = SimpleNodeLogger.createSimpleLogger({
    logFilePath: `${logPath}/all.log`,
    timestampFormat: 'YYYY-MM-DD HH:mm:ss.SSS'
});

module.exports = log;