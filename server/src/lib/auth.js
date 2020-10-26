const { authUtils } = require('../utils');
/**
 * Authorize level 1
 * @param {Request} req express request
 * @param {Response} res express response
 * @param {Next} next express next function
 */
const authorize = (req, res, next) => {
    // TODO 
    // TODO VALIDATION
    const { user, hashedPassword } = req.body;
    // search database
    // authUtils.sign(foundId, process.env.JWT_SECRET);

};


module.exports.authorize = authorize;