const express = require('express');
const router = express.Router();

const { login } = require('../../lib/auth');
const controller = require('../controllers/auth.controller');
// login
router.post('/',
    login,
    controller.login
);

module.exports = router;
