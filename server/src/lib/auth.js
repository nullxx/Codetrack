const DB = require('../lib/database');

/**
 * Authorize level 1
 * @param {Request} req express request
 * @param {Response} _res express response
 * @param {Next} _next express next function
 */
const login = async (req, _res, next) => {
    try {
        const { email, password } = req.body;
        const User = DB.getConn().models.users;
        const user = await User.findAll({
            where: {
                email,
                password,
            },
            raw: true,
        });
        req.user = user[0];
        if (!req.user) {
            throw new Error('ERR_USER_NOT_FOUND');
        }
    } catch (error) {
        next(error);
    }
    next();
};


module.exports.login = login;