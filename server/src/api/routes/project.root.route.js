
const express = require('express');
const { validate } = require('express-validation');

const router = express.Router();

const { authorize, superAuthorize } = require('../middlewares/auth.middleware');
const controller = require('../controllers/project.controller');
const { create, update, getOne } = require('../validations/project.root.validation');


router
    .route('/')
    .get(
        authorize,
        controller.list
    )
    .post(
        authorize,
        validate(create),
        controller.create
    )
    .put(
        authorize,
        validate(update),
        controller.update
    );

router
    .route('/:project')
    .get(
        authorize,
        superAuthorize,
        validate(getOne),
        controller.getOne
    );

module.exports = router;
