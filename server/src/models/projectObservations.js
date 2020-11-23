/* jshint indent: 2 */

const Sequelize = require('sequelize');
module.exports = function(sequelize, DataTypes) {
  return sequelize.define('projectObservations', {
    id: {
      autoIncrement: true,
      type: DataTypes.BIGINT.UNSIGNED,
      allowNull: false,
      primaryKey: true
    },
    project: {
      type: DataTypes.BIGINT.UNSIGNED,
      allowNull: true,
      references: {
        model: 'projects',
        key: 'id'
      }
    },
    observation: {
      type: DataTypes.STRING(255),
      allowNull: true,
      references: {
        model: 'observations',
        key: 'shortId'
      }
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
    tableName: 'projectObservations',
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
        name: "project",
        using: "BTREE",
        fields: [
          { name: "project" },
        ]
      },
      {
        name: "observation",
        using: "BTREE",
        fields: [
          { name: "observation" },
        ]
      },
    ]
  });
};
