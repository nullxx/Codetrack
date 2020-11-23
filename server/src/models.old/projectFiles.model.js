const { DataTypes } = require('sequelize');
const commonOptions = require('./commonOptions');

module.exports = {
    name: "projectFiles",
    attributes: {
        id: {
            type: DataTypes.BIGINT,
            primaryKey: true,
            autoIncrement: true,
        },
        project: {
            type: DataTypes.BIGINT,
        },
        localFilePath: {
            type: DataTypes.STRING
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
        tableName: "projectFiles"
    },
    requires: [
        {
            selfKey: 'project',
            targetModel: 'projects',
            targetKey: 'id',
            onDelete: 'SET NULL'
        }
    ]
}


