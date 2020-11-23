const { Sequelize } = require('sequelize');

const loggerLib = require('../lib/logger');

const { initModels } = require('../models/init-models');
/**
 * Creates connection to DB
 */
let __connection;

module.exports.createConn = async () => {

    const sequelize = new Sequelize(
        process.env.DB_NAME,
        process.env.DB_USER,
        process.env.DB_PASSWORD,
        {
            host: process.env.DB_HOST,
            dialect: process.env.DB_DIALECT_TYPE,
            dialectOptions: {
                timezone: "Etc/GMT0"
            },
            logging: process.env.DB_LOGGING_ENABLED === 'TRUE' ? (msg) => loggerLib.log('debug', 'DATABASE', msg) : false,
        }
    );

    await sequelize.authenticate();
    __connection = sequelize;
    loggerLib.log('debug', 'DB connection established', true);
    return true;
}

module.exports.initModels = async (conn) => {
    initModels(conn);
}

module.exports.getConn = () => {
    return __connection;
}
