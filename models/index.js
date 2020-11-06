const { defineModel } = require('../server/src/utils/models.utils');

// BEGIN IMPORT MODELS
const RolesPreModel = require('../models/roles.model');
// END IMPORT MODELS

/**
 * Define all models
 * @param {Sequelize} conn Sequelize connection
 */
module.exports.initModels = async (conn) => {
    defineModel(conn, RolesPreModel);
}