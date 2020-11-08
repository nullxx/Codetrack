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
        projectFile: {
            type: DataTypes.BIGINT,
        },
        file: {
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
        tableName: "snapshots"
    },
    requires: [
        {
            selfKey: 'file',
            targetModel: 'files',
            targetKey: 'id',
            onDelete: 'SET NULL'
        },
        {
            selfKey: 'projectFile',
            targetModel: 'projectFiles',
            targetKey: 'id',
            onDelete: 'SET NULL'
        }
    ]
}


