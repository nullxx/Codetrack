const checkChanged = (object) => {
    const toReturn = {};
    Object.keys(object).forEach((key) => {
        if (typeof object[key] !== 'undefined' && object[key].length > 0) {
            toReturn[key] = object[key];
        }
    })
    return toReturn;
}

module.exports.checkChanged = checkChanged;
