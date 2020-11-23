/* jshint indent: 2 */

const Sequelize = require('sequelize');
module.exports = function(sequelize, DataTypes) {
  return sequelize.define('observations', {
    id: {
      autoIncrement: true,
      type: DataTypes.BIGINT.UNSIGNED,
      allowNull: false,
      primaryKey: true
    },
    group: {
      type: DataTypes.BIGINT.UNSIGNED,
      allowNull: true,
      references: {
        model: 'groups',
        key: 'id'
      }
    },
    shortId: {
      type: DataTypes.STRING(255),
      allowNull: true,
      unique: "shortId"
    },
    createdAt: {
      type: DataTypes.DATE,
      allowNull: true,
      defaultValue: Sequelize.fn('current_timestamp')
    },
    updatedAt: {
      type: DataTypes.DATE,
      allowNull: true
    }
  }, {
    sequelize,
    tableName: 'observations',
    schema: process.env.DB_NAME,
    timestamps: false,
    indexes: [
      {
        name: "PRIMARY",
        unique: true,
        using: "BTREE",
        fields: [
          { name: "id" },
        ]
      },
      {
        name: "id",
        unique: true,
        using: "BTREE",
        fields: [
          { name: "id" },
        ]
      },
      {
        name: "shortId",
        unique: true,
        using: "BTREE",
        fields: [
          { name: "shortId" },
        ]
      },
      {
        name: "group",
        using: "BTREE",
        fields: [
          { name: "group" },
        ]
      },
    ]
  });
};
