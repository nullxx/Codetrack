const { authUtils } = require('../utils');
authUtils // temporal (for lint)
/**
 * Authorize level 1
 * @param {Request} req express request
 * @param {Response} res express response
 * @param {Next} next express next function
 */
const authorize = (req, res, next) => {
    // TODO 
    // TODO VALIDATION
    req, res, next // temporal (for lint)
    const { user, hashedPassword } = req.body;
    user, hashedPassword // temporal (for lint)
    // search database
    // authUtils.sign(foundId, process.env.JWT_SECRET);

};


module.exports.authorize = authorize;