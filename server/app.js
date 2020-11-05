var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');

const libLogger = require('./src/lib/logger');
var indexRouter = require('./src/api/routes/index');
const { exit } = require('process');

const initialize = async () => {
  try {
    const db = require('./src/lib/database');
    await db.createConn()()

    console.log("s")
    var app = express();

    app.use(logger('dev'));
    app.use(express.json());
    app.use(express.urlencoded({ extended: false }));
    app.use(cookieParser());
    app.use(express.static(path.join(__dirname, 'public')));

    app.use('/', indexRouter);


    // error handler
    app.use(function (err, req, res) {
      // set locals, only providing error in development
      res.locals.message = err.message;
      res.locals.error = req.app.get('env') === 'development' ? err : {};

      // send the error
      res.status(err.status || 500);
      res.send({ code: -1, error: err.message });
    });
    return app;
  } catch (error) {
    await libLogger.log('error', `Initialization error.`, error.message, error.stack); // without await it will continue executing
    exit(1);
  }
};

module.exports = initialize;
