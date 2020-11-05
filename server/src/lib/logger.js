var fs = require('fs');
const logPath = 'log';
if (!fs.existsSync(logPath)) {
    fs.mkdirSync(logPath);
}

const SimpleNodeLogger = require('simple-node-logger');
const __logger = SimpleNodeLogger.createSimpleLogger({
    logFilePath: `${logPath}/all.log`,
    timestampFormat: 'YYYY-MM-DD HH:mm:ss.SSS'
});

const log = (type, ...args) => {
    let log = '';
    for (let i = 0; i < args.length; i++) {
        const arg = args[i];
        log += i === 0 ? arg : `\n\t${arg}`;
    }
    __logger.log(type, log);
}

module.exports = {
    log
};