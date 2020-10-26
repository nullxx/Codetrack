const { promises: fs } = require('fs');
/**
 * Read content from file
 * @param {string} path 
 * @param {string} encoding 
 */
const readFile = async (path, encoding = 'utf8') => {
    let fileContent = await fs.readFile(path, encoding);
    return fileContent;
}

module.exports.readFile = readFile;