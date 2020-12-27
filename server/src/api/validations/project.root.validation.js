const { Joi } = require('express-validation');


module.exports = {
    create: {
        body: Joi.object({
            name: Joi.string().required(),
            language: Joi.string().optional().allow(''),
            natures: Joi.string().required(),
        }),
    },
    update: {
        body: Joi.object({
            project: Joi.number().required(),
            name: Joi.string(),
            isAllowed: Joi.boolean()
        })
    },
    getOne: {
        params: Joi.object({
            project: Joi.number().required(),
        })
    },
    download: {
        params: Joi.object({
            project: Joi.number().required(),
        })
    }
}