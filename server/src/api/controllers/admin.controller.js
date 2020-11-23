const loggerLib = require('../../lib/logger');
const adminLib = require('../../lib/admin');

const getGroups = async (req, res, next) => {
    try {
        const groups = await adminLib.getGroups(req.user.id);
        res.send({ code: 1, data: groups });
        loggerLib.log('info', '/admin - getGroup', req);
    } catch (error) {
        loggerLib.log('error', '/admin - getGroup', error, req);
        next(error);
    }
};

const getObservations = async (req, res, next) => {
    try {
        const { group } = req.params;
        const observations = await adminLib.getObservations(group);
        res.send({ code: 1, data: observations });
        loggerLib.log('info', '/admin - getObservations', req);
    } catch (error) {
        loggerLib.log('error', '/admin - getObservations', error, req);
        next(error);
    }
}

const getProjectObservations = async (req, res, next) => {
    try {
        const { observation } = req.params;
        const projectObservations = await adminLib.getProjectObservations(observation);
        res.send({ code: 1, data: projectObservations });
        loggerLib.log('info', '/admin - getProjectObservations', req);
    } catch (error) {
        loggerLib.log('error', '/admin - getProjectObservations', error, req);
        next(error);
    }
}
module.exports.getGroups = getGroups;
module.exports.getObservations = getObservations;
module.exports.getProjectObservations = getProjectObservations;
