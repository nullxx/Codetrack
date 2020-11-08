const { DataTypes } = require('sequelize');
const commonOptions = require('./commonOptions');

module.exports = {
    name: "files",
    attributes: {
        id: {
            type: DataTypes.BIGINT,
            primaryKey: true,
            autoIncrement: true,
        },
        name: {
            type: DataTypes.STRING,
        },
        mimetype: {
            type: DataTypes.STRING
        },
        truncated: {
            type: DataTypes.BOOLEAN,
        },
        size: {
            type: DataTypes.NUMBER,
        },
        md5: {
            type: DataTypes.STRING,
        },
        sha512: {
            type: DataTypes.STRING,
        },
        path: {
            type: DataTypes.STRING,
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
        tableName: "files"
    },
    requires: []
}


