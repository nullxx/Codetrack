const { Joi } = require('express-validation');


module.exports = {
    getObservations: {
        params: Joi.object({
            group: Joi.number().required()
        }),
    },
    getProjectObservations: {
        params: Joi.object({
            observation: Joi.string().required()
        }),
    }
}