
const express = require('express');
const { validate } = require('express-validation');

const router = express.Router();

const { authorize } = require('../middlewares/auth.middleware');
const controller = require('../controllers/project.controller');
const { create, update } = require('../validations/project.root.validation');


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
    )

module.exports = router;
