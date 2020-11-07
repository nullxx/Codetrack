const crypto = require('crypto');

const createSHA256 = (data) => {
    const hash = crypto.createHash('sha256')
        .update(data)
        .digest('hex');
    return hash;
}
const createSHA512 = (data) => {
    const hash = crypto.createHash('sha512')
        .update(data)
        .digest('hex');
    return hash;
}
module.exports.createSHA256 = createSHA256;
module.exports.createSHA512 = createSHA512;
