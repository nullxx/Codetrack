const { DataTypes } = require('sequelize');
const commonOptions = require('./commonOptions');

module.exports = {
    name: "Roles",
    attributes: {
        id: {
            type: DataTypes.BIGINT,
            primaryKey: true
        },
        name: {
            type: DataTypes.STRING
        },
    },
    options: {
        ...commonOptions,
        tableName: "Roles"
    }
}


