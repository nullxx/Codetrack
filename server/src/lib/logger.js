const SimpleNodeLogger = require('simple-node-logger');

const log = SimpleNodeLogger.createSimpleLogger({
    logFilePath: 'log/all.log',
    timestampFormat: 'YYYY-MM-DD HH:mm:ss.SSS'
});

module.exports = log;