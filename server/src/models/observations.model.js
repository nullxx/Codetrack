const { DataTypes } = require('sequelize');
const commonOptions = require('./commonOptions');

module.exports = {
    name: "observations",
    attributes: {
        id: {
            type: DataTypes.BIGINT,
            primaryKey: true,
            autoIncrement: true,
        },
        group: {
            type: DataTypes.BIGINT,
        },
        shortId: {
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
        tableName: "observations"
    },
    requires: [
        {
            selfKey: 'group',
            targetModel: 'groups',
            targetKey: 'id',
            onDelete: 'SET NULL'
        }
    ]
}


