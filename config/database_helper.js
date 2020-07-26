const mysql = require('mysql');
var db = require('./database');

var exportObj = {};

//Connecting the file to our MySQL Database.
exportObj.handleDisconnect = function () {
    const db_config = {
        host: "us-cdbr-east-02.cleardb.com",
        user: "bee740a43111f7",
        password: "505bb013",
        database: "heroku_0f6f9edfe1a9c1b"
    };
    db = mysql.createConnection(db_config);

    db.connect(function (err) {
        if (err) {
            console.log('error when connecting to db:', err);
            setTimeout(handleDisconnect, 2000);
        }

        console.log("Mysql connected...");
    });

    db.on('error', function (err) {
        console.log('db error', err);
        if (err.code === 'PROTOCOL_CONNECTION_LOST') {
            handleDisconnect();
        } else {
            throw err;
        }
    });
}


module.exports = exportObj;