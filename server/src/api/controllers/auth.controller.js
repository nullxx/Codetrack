const loggerLib = require('../../lib/logger');

const { authUtils } = require('../../utils');


const login = (req, res, next) => {
    try {
        let data = {
            token: authUtils.sign(req.user, process.env.JWT_SECRET, process.env.JWT_EXPIRATION_TIME),
            admin: req.user.role !== parseInt(process.env.USER_ROLE),
        };
        res.send({ code: 1, data });
        loggerLib.log('info', '/login - login', 'Logged', req);
    } catch (error) {
        loggerLib.log('error', '/login - login', error, req);
        next(error);
    }
};

module.exports.login = login;