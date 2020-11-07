const { Joi } = require('express-validation');


module.exports = {
    create: {
        body: Joi.object({
            project: Joi.number().required(),
            data: Joi.any().meta({ swaggerType: 'file' }),
        }),
    }
}