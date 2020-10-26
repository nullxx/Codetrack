const express = require('express');
const router = express.Router();

const { authorize } = require('../../lib/auth');
const controller = require('../controllers/auth.controller');
// login
router.post('/',
    authorize,
    controller.login
);

module.exports = router;
