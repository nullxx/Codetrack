const { Sequelize } = require('sequelize');

const loggerLib = require('../lib/logger');
/**
 * Creates connection to DB
 */
module.exports.createConn = () => {

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
    return sequelize.authenticate;

}



