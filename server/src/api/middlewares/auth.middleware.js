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

module.exports.authorize = authorize;