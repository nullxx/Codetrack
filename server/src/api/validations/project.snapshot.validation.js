const { Joi } = require('express-validation');


module.exports = {
    create: {
        body: Joi.object({
            project: Joi.number().required(),
            localPath: Joi.string().required()
        }),
    }
}