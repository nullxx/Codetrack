const { DataTypes } = require('sequelize');
const commonOptions = require('./commonOptions');

module.exports = {
    name: "projectObservations",
    attributes: {
        id: {
            type: DataTypes.BIGINT,
            primaryKey: true,
            autoIncrement: true,
        },
        project: {
            type: DataTypes.BIGINT,
        },
        observation: {
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
        tableName: "projectObservations"
    },
    requires: [
        {
            selfKey: 'project',
            targetModel: 'projects',
            targetKey: 'id',
            onDelete: 'SET NULL'
        },
        {
            selfKey: 'observation',
            targetModel: 'observations',
            targetKey: 'shortId',
            onDelete: 'SET NULL'
        }
    ]
}


