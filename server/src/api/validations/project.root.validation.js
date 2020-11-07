const { Joi } = require('express-validation');


module.exports = {
    create: {
        body: Joi.object({
            name: Joi.string().required(),
            language: Joi.string().optional().allow(''),
        }),
    }
}