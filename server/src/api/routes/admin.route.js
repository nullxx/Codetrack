const express = require('express');
const router = express.Router();
const { validate } = require('express-validation');

const { superAuthorize, authorize } = require('../middlewares/auth.middleware');
const controller = require('../controllers/admin.controller');
const { getObservations, getProjectObservations } = require('../validations/admin.validation');


router.use(
    authorize,
    superAuthorize
);
router
    .route('/groups')
    .get(
        controller.getGroups
    );
router
    .route('/observations/:group')
    .get(
        validate(getObservations),
        controller.getObservations,
    );
router
    .route('/observations/:observation/projects')
    .get(
        validate(getProjectObservations),
        controller.getProjectObservations,
    );
module.exports = router;
