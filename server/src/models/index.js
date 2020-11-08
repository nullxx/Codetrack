const loggerLib = require('../lib/logger');

const { defineModel } = require('../utils/models.utils');

// BEGIN IMPORT MODELS
const RolesPreModel = require('./roles.model');
const UsersPreModel = require('./users.model');
const ProjectsPreModel = require('./projects.model');
const SnapshotPreModel = require('./snapshots.model');
const FilesPreModel = require('./files.model');
const ProjectFilesPreModel = require('./projectFiles.model');
const CollisionsPreModel = require('./collisions.model');
const GroupsPreModel = require('./groups.model');
const ObservationsPreModel = require('./observations.model');
const ProjectObservationsPreModel = require('./projectObservations');
// END IMPORT MODELS

const models = [
    RolesPreModel,
    UsersPreModel,
    ProjectsPreModel,
    SnapshotPreModel,
    FilesPreModel,
    ProjectFilesPreModel,
    CollisionsPreModel,
    GroupsPreModel,
    ObservationsPreModel,
    ProjectObservationsPreModel,
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
    __processRequires(conn);
}

/**
 * Creates connections between foreign keys (FIXME revise me)
 * @param {Sequelize} conn Sequelize connction
 */
const __processRequires = (conn) => {
    for (let i = 0; i < models.length; i++) {
        const model = models[i];
        for (let j = 0; j < model.requires.length; j++) {
            const modelRequires = model.requires[j];
            conn.models[model.name].belongsTo(conn.models[modelRequires.targetModel], { foreignKey: modelRequires.selfKey, as: `fk_${modelRequires.selfKey}`, targetKey: modelRequires.targetKey, onDelete: modelRequires.onDelete })
        }
    }
}