const { DataTypes } = require('sequelize');
const commonOptions = require('./commonOptions');

module.exports = {
    name: "users",
    attributes: {
        id: {
            type: DataTypes.BIGINT,
            primaryKey: true,
            autoIncrement: true
        },
        email: {
            type: DataTypes.STRING
        },
        password: {
            type: DataTypes.STRING
        },
        role: {
            type: DataTypes.BIGINT,
        },
    },
    options: {
        ...commonOptions,
        tableName: "users"
    },
    requires: [
        {
            selfKey: 'role',
            targetModel: 'roles',
            targetKey: 'id',
            onDelete: 'CASCADE'
        }
    ]
}


