const jwt = require('jsonwebtoken');

/**
 * Encode to jwt
 * @param {string || buffer || object} payload 
 * @param {string} secret 
 * @param {string || number} expiresIn 
 */
const sign = (payload, secret, expiresIn) => {
    const token = jwt.sign(payload, secret, { expiresIn });
    return token;
};

/**
 * Decode jwt token
 * @param {string} token 
 * @param {string} secret 
 */
const decode = (token, secret) => {
    const decoded = jwt.verify(token, secret);
    return decoded;
}


module.exports.sign = sign;
module.exports.decode = decode;