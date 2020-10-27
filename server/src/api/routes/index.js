const express = require('express');
const router = express.Router();

// being route imports
const auth = require('./auth.route.js');
// end route imports

// start router use
router.use('/login', auth);
// end router use

module.exports = router;
