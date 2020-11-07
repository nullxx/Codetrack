const express = require('express');
const router = express.Router();

// being route imports
const projectRoot = require('./project.root.route');
const snapshot = require('./project.snapshot.route');
// end route imports

// start router use
router.use('/', projectRoot);
router.use('/snapshot', snapshot);
// end router use

module.exports = router;
