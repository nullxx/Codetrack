
const express = require('express');
const { validate } = require('express-validation');

const router = express.Router();

const { authorize } = require('../middlewares/auth.middleware');
const controller = require('../controllers/project.snapshot.controller');
const { create } = require('../validations/project.snapshot.validation');


router
    .route('/')
    //     .get(
    //         authorize,// SUPERAUTORIZE THIS
    //         controller.list
    //     )
    .put(
        authorize,
        validate(create),
        controller.create
    )

module.exports = router;
