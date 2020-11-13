const { DataTypes } = require('sequelize');
const commonOptions = require('./commonOptions');

module.exports = {
    name: "projects",
    attributes: {
        id: {
            type: DataTypes.BIGINT,
            primaryKey: true,
            autoIncrement: true
        },
        name: {
            type: DataTypes.STRING
        },
        language: {
            type: DataTypes.STRING
        },
        user: {
            type: DataTypes.BIGINT,
            // references: {
            //     model: 'users',
            //     key: 'id'
            // }
        },
        isAllowed: {
            type: DataTypes.BOOLEAN,
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
        tableName: "projects"
    },
    requires: [
        {
            selfKey: 'user',
            targetModel: 'users',
            targetKey: 'id',
            onDelete: 'CASCADE'
        }
    ]
}


