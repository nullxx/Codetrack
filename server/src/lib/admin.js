const DB = require('./database');

/**
 * List owned groups by admin
 * @param {number} superUserId 
 */
const getGroups = async (superUserId) => {
    const Group = DB.getConn().models.groups;

    const superUserGroups = await Group.findAll({
        where: {
            superUser: superUserId
        },
    });
    return superUserGroups;
}

/**
 * List group observations
 * @param {number} groupId 
 */
const getObservations = async (groupId) => {
    const Observation = DB.getConn().models.observations;

    const groupObservations = await Observation.findAll({
        where: {
            group: groupId
        },
    });
    return groupObservations;
}

/**
 * List projects for an observation
 * @param {number} observationShortId 
 */
const getProjectObservations = async (observationShortId) => {
    const ProjectObservation = DB.getConn().models.projectObservations;

    const projectObservations = await ProjectObservation.findAll({
        where: {
            observation: observationShortId
        },
    });
    return projectObservations;
}

module.exports.getGroups = getGroups;
module.exports.getObservations = getObservations;
module.exports.getProjectObservations = getProjectObservations;
