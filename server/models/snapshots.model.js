const { DataTypes } = require('sequelize');
const commonOptions = require('./commonOptions');

module.exports = {
    name: "snapshots",
    attributes: {
        id: {
            type: DataTypes.BIGINT,
            primaryKey: true,
            autoIncrement: true
        },
        project: {
            type: DataTypes.BIGINT,
        },
        user: {
            type: DataTypes.BIGINT,
        },
        data: {
            type: DataTypes.TEXT('long'),
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
        tableName: "snapshots"
    },
    requires: []
}


