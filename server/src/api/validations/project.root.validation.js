const { Joi } = require('express-validation');


module.exports = {
    create: {
        body: Joi.object({
            name: Joi.string().required(),
            language: Joi.string().optional().allow(''),
        }),
    },
    update: {
        body: Joi.object({
            project: Joi.number().required(),
            name: Joi.string(),
            isAllowed: Joi.boolean()
        })
    }
}