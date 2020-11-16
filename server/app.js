var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');
const fileUpload = require('express-fileupload');
const libLogger = require('./src/lib/logger');
require('./src/utils').errorUtils;
var indexRouter = require('./src/api/routes/index');

const initialize = async () => {
  try {
    const db = require('./src/lib/database');
    await db.createConn()
    await db.initModels(db.getConn())

    var app = express();

    app.use(logger('dev'));
    app.use(express.json());
    app.use(express.urlencoded({ extended: false }));
    app.use(cookieParser());
    app.use(express.static(path.join(__dirname, 'public')));
    app.use(fileUpload({
      limits: { fileSize: 50 * 1024 * 1024 },
    }));

    app.use('/', indexRouter);


    // error handler
    // eslint-disable-next-line no-unused-vars
    app.use(function (err, _req, res, _next) {
      let message = 'Unknown message';
      let status;
      switch (err.message) {
        case 'UNAUTHORIZED':
          message = 'Unauthorized';
          status = 401;
          break;

        default:
          let message = err.toJSON();
          let status = 500;
      }

      res.status = status;
      res.send({ code: -1, error: message });
    });
    return app;
  } catch (error) {
    libLogger.log('error', `Initialization error.`, error.message, error.stack);
  }
};

module.exports = initialize;
