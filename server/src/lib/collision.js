
// if colision of hashes compare raw files
const areEqual = (exFile, nFile) => {
    if (exFile === nFile) {
        return true;
    }
    return false;
}

module.exports.areEqual = areEqual;
