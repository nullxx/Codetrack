const { DataTypes } = require('sequelize');
const commonOptions = require('./commonOptions');

module.exports = {
    name: "collisions",
    attributes: {
        id: {
            type: DataTypes.BIGINT,
            primaryKey: true,
            autoIncrement: true,
        },
        exFile: {
            type: DataTypes.BIGINT,
        },
        withFile: {
            type: DataTypes.BIGINT,
        },
        createdAt: {
            type: DataTypes.TIME,
        },
        updatedAt: {
            type: DataTypes.TIME,
        }
    },
    options: {
        ...commonOptions,
        tableName: "collisions"
    },
    requires: []
}


