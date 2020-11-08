const { DataTypes } = require('sequelize');
const commonOptions = require('./commonOptions');

module.exports = {
    name: "groups",
    attributes: {
        id: {
            type: DataTypes.BIGINT,
            primaryKey: true,
            autoIncrement: true,
        },
        superUser: {
            type: DataTypes.BIGINT,
        },
        name: {
            type: DataTypes.STRING,
        },
        description: {
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
        tableName: "groups"
    },
    requires: [
        {
            selfKey: 'superUser',
            targetModel: 'users',
            targetKey: 'id',
            onDelete: 'SET NULL'
        }
    ]
}


