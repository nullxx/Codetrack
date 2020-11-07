const { default: kfs } = require("key-file-storage");

/**
 * Saves file data into {folderPath}/{metadata.fileName}
 * @param {string} folderPath folder to save (eg: path/to/the/folder)
 * @param {object} neededData { metadata, data } where metadata need "fileName" prop and data will be the data to store
 * @returns {object} result {success: <boolean>, path: <string> }
 */
const save = (folderPath, { metadata, data }) => {
    // Write something to file 'my/storage/path/myfile'
    if (!folderPath || !metadata || !metadata.fileName || !data) throw new Error('ERR_NO_FOLDER_PATH');
    const folder = kfs(folderPath);
    folder[metadata.fileName] = data;

    return {
        success: folder[metadata.fileName] ? true : false,
        path: `${folderPath}/${metadata.fileName}`
    };
};

/**
 * Reads file data from {folderPath}/{metadata.fileName}
 * @param {string} folderPath folder to read (eg: path/to/the/folder)
 * @param {object} neededData { metadata } where metadata need "fileName" prop
 * @returns {any} result file data
 */
const read = (folderPath, { metadata }) => {
    if (!folderPath || !metadata) throw new Error('ERR_NO_FOLDER_PATH');
    const folder = kfs(folderPath);
    const fileData = folder[metadata.fileName];

    return fileData;
}

/**
 * Deletes file data from {folderPath}/{metadata.fileName}
 * @param {string} folderPath folder to delete (eg: path/to/the/folder)
 * @param {object} neededData { metadata } where metadata need "fileName" prop
 * @returns {boolean} result if true => deleted successfully
 */
const del = (folderPath, { metadata }) => {
    if (!folderPath || !metadata) throw new Error('ERR_NO_FOLDER_PATH');
    const folder = kfs(folderPath);
    delete folder[metadata.fileName];

    return folder[metadata.fileName] ? false : true;
}

module.exports.save = save;
module.exports.read = read;
module.exports.delete = del;
