var DataTypes = require("sequelize").DataTypes;
var _collisions = require("./collisions");
var _files = require("./files");
var _groups = require("./groups");
var _observations = require("./observations");
var _projectFiles = require("./projectFiles");
var _projectObservations = require("./projectObservations");
var _projects = require("./projects");
var _roles = require("./roles");
var _snapshots = require("./snapshots");
var _users = require("./users");

function initModels(sequelize) {
  var collisions = _collisions(sequelize, DataTypes);
  var files = _files(sequelize, DataTypes);
  var groups = _groups(sequelize, DataTypes);
  var observations = _observations(sequelize, DataTypes);
  var projectFiles = _projectFiles(sequelize, DataTypes);
  var projectObservations = _projectObservations(sequelize, DataTypes);
  var projects = _projects(sequelize, DataTypes);
  var roles = _roles(sequelize, DataTypes);
  var snapshots = _snapshots(sequelize, DataTypes);
  var users = _users(sequelize, DataTypes);

  // collisions.belongsTo(files, { foreignKey: "exFile"});
  // files.hasMany(collisions, { foreignKey: "exFile"});
  // collisions.belongsTo(files, { foreignKey: "withFile"});
  // files.hasMany(collisions, { foreignKey: "withFile"});
  // groups.belongsTo(users, { foreignKey: "superUser"});
  // users.hasMany(groups, { foreignKey: "superUser"});
  // observations.belongsTo(groups, { foreignKey: "group"});
  // groups.hasMany(observations, { foreignKey: "group"});
  // projectFiles.belongsTo(projects, { foreignKey: "project"});
  // projects.hasMany(projectFiles, { foreignKey: "project"});
  // projectObservations.belongsTo(projects, { foreignKey: "project"});
  // projects.hasMany(projectObservations, { foreignKey: "project"});
  // projectObservations.belongsTo(observations, { foreignKey: "observation"});
  // observations.hasMany(projectObservations, { foreignKey: "observation"});
  // projects.belongsTo(users, { foreignKey: "user"});
  // users.hasMany(projects, { foreignKey: "user"});
  // snapshots.belongsTo(files, { foreignKey: "file"});
  // files.hasMany(snapshots, { foreignKey: "file"});
  // snapshots.belongsTo(projectFiles, { foreignKey: "projectFile"});
  // projectFiles.hasMany(snapshots, { foreignKey: "projectFile"});
  // users.belongsTo(roles, { foreignKey: "role"});
  // roles.hasMany(users, { foreignKey: "role"});

  return {
    collisions,
    files,
    groups,
    observations,
    projectFiles,
    projectObservations,
    projects,
    roles,
    snapshots,
    users,
  };
}
module.exports = initModels;
module.exports.initModels = initModels;
module.exports.default = initModels;
