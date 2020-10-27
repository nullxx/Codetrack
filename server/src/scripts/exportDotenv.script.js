const { fileUtils } = require('../utils');
const libLogger = require('../lib/logger');
// this file exports the content from .env file to node environment variables
const f = async (envPath) => {
    const dotenvContent = await fileUtils.readFile(typeof envPath === 'string' ? envPath : './src/test/.testenv');
    const splitedLines = dotenvContent.split('\n');
    for (let i = 0; i < splitedLines.length; i++) {
        const line = splitedLines[i];
        const lineSplitted = line.split('=');
        if (!lineSplitted) return libLogger.log('error', '.env variable not correctly formatted');

        const varName = line.split('=')[0];
        const varValue = line.split('=')[1];
        process.env[varName] = varValue;
    }
    libLogger.log('info', 'Dontenv file loaded correctly');
};
module.exports = f;