const { authUtils } = require('../../utils');


const authorize = (req, res, next) => {
    try {
        const token = req.headers.authorization ? req.headers.authorization.split('Bearer ')[1] : undefined;

        if (!token) {
            throw new Error('UNAUTHORIZED');
        }
        const user = authUtils.decode(token, process.env.JWT_SECRET);
        req.user = user;
    } catch (error) {
        next(error);
    }
    next();
};

const superAuthorize = (req, res, next) => {
    try {
        const user = req.user;
        if (user.role >= 4) {
            throw new Error('UNAUTHORIZED');
        }
    } catch (error) {
        next(error);
    }
    next();
};
module.exports.authorize = authorize;
module.exports.superAuthorize = superAuthorize;
