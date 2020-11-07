const loggerLib = require('../src/lib/logger');

const { defineModel } = require('../src/utils/models.utils');

// BEGIN IMPORT MODELS
const RolesPreModel = require('../models/roles.model');
const UsersPreModel = require('../models/users.model');
const ProjectsPreModel = require('../models/projects.model');
const SnapshotPreModel = require('../models/snapshots.model');
const FilesPreModel = require('../models/files.model');
const ProjectFilesPreModel = require('../models/projectFiles.model');

// END IMPORT MODELS

const models = [
    RolesPreModel,
    UsersPreModel,
    ProjectsPreModel,
    SnapshotPreModel,
    FilesPreModel,
    ProjectFilesPreModel,
];
/**
 * Define all models
 * @param {Sequelize} conn Sequelize connection
 */
module.exports.initModels = async (conn) => {
    for (let i = 0; i < models.length; i++) {
        const model = models[i];
        defineModel(conn, model);
        loggerLib.log('debug', 'Defined model', `Model name: ${model.name}`, JSON.stringify(model.attributes, null, 2));
    }
    // __processRequires(conn);
}

// const __processRequires = (conn) => {
//     for (let i = 0; i < models.length; i++) {
//         const model = models[i];
//         if (model.requires) {
//             for (let j = 0; j < model.requires.length; j++) {
//                 const requireRef = model.requires[j];
//                 for (let k = 0; k < models.length; k++) {
//                     const model2 = models[k];
//                     if (model.name !== model2.name) {
//                         if (requireRef.targetModel == model2.name) {
//                             conn.models[model.name].belongsTo(conn.models[requireRef.targetModel], { foreignKey: requireRef.targetKey, as: requireRef.selfKey, onDelete: requireRef.onDelete })
//                         }
//                     }
//                 }
//             }
//         }
//     }
// }