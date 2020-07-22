require('dotenv').config();

const express = require("express");
const bodyParser = require("body-parser");
const axios = require("axios");
const mysql = require("mysql"); 
const jwt = require("jsonwebtoken");
const bcrypt = require("bcrypt");
const passport = require("passport");
const path = require("path");

const flash = require("express-flash");
const session = require("express-session");

const initializePassport = require("./passport-config");

initializePassport(passport, findUserWithUsername, findUserWithId);

const app = express();

// Local database connection
const db = mysql.createConnection({
    host: "localhost",
    user: "root",
    password: "Password123",
    database: "test_database"
});

// Remote database connection
// const db = mysql.createConnection({
//     host: "us-cdbr-east-02.cleardb.com",
//     user: "bd54a33decc040",
//     password: "be8e7ce8556c9ff",
//     database: "	heroku_54e8d758703d17f"
// })

db.connect((err) => {
    if (err) {
        throw err;
    }

    console.log("MySQL connected...");
});

app.set('view-engine', 'ejs');
app.listen(3000, () => console.log("listening at 3000"));
app.use(bodyParser.json());
app.use(express.static("views"));
app.use(bodyParser.urlencoded({extended: true}));
app.use(flash());
app.use(session({
    secret: "f56630f647222a5ae532556f2b634b920ef514b41553f977e1d1b88e5819cf84",
    resave: false,
    saveUninitialized: false
}))
app.use(passport.initialize());
app.use(passport.session());


// Used for login purposes
async function findUserWithId(id) {
    const result = await findUserWithIdPromise(id);
    return result;
}

function findUserWithIdPromise(id) {
    return new Promise((resolve, reject) => {
        db.query("SELECT * FROM user WHERE user_id = ?", id,
            (err, results, fields) => {
                return err ? reject(err) : resolve(results[0]);
            });
    })
};

async function findUserWithUsername(username) {
    const result = await findUserWithUsernamePromise(username);
    console.log(result);
    return result;
}

function findUserWithUsernamePromise(username) {
    return new Promise((resolve, reject) => {
        db.query("SELECT * FROM user WHERE Username = ?", username,
            (err, results, fields) => {
                return err ? reject(err) : resolve(results[0]);
            });
    })
};

//Home page
app.get('/', async function(req, res) {
    var user = await req.user;

    if (user == null) {
        res.render("index.ejs", {username: null});
    } else {
        res.render("index.ejs", { username: user.Username});
    }
})

// Login
app.post('/login', passport.authenticate('local', {
    successRedirect: "/login_success",
    failureRedirect: '/login',
    failureFlash: true
}))

// Login page
app.get("/login", function (req, res) {
    res.render("login.ejs");
})

// Login success page
app.get("/login_success", async function (req, res) {
    var user = await req.user;

    res.render("login_success.ejs", { name: user.Username });
})

// Sign up

// Registration page
app.get("/registration", function (req, res) {
    res.render("registration.ejs");
})

//Username validation
app.post("/check_username", function(req, res) {
    var username = req.body.username;

    db.query("SELECT * FROM user WHERE Username = ?", username, 
        function(err, results, fields) {
            if (err) throw err;

            console.log(results);

            if (results.length != 0) {
                return res.send("Username taken!");
            } else {
                return res.send("");
            }
    })
})

// Mobile number validation
app.post("/check_mobile_number", function (req, res) {
    var mobile_number = req.body.mobile_number;

    if (!mobile_number.startsWith("+65")) {
        mobile_number = "+65" + mobile_number;
    }

    if (mobile_number.split(" ").length == 2) {
        mobile_number = mobile_number.replace(" ", "");
    }

    db.query("SELECT * FROM user WHERE Mobile_number = ?", mobile_number,
        function (err, results, fields) {
            if (err) throw err;

            console.log(results);

            if (results.length != 0) {
                return res.send("Mobile number taken!");
            } else {
                return res.send("");
            }
        })
})


// Email validation
app.post("/check_email", function (req, res) {
    var email = req.body.email;

    db.query("SELECT * FROM user WHERE Email = ?", email,
        function (err, results, fields) {
            if (err) throw err;

            console.log(results);

            if (results.length != 0) {
                return res.send("Email is used!");
            } else {
                return res.send("");
            }
        })
})


//Update data
app.post("/update_address", async function (req, res) {
    var new_address = "a";
    var user_id = 1;
    var email = "@example.com"

    var data = [new_address, email, user_id];


    db.query("UPDATE user SET Address = ?, Email = ? WHERE user_id = ?", data, 
        (err, results, fields) => {
            if (err) throw err;

            console.log(results);
        });

    res.redirect("update_success.html");
})


// Sign up process (putting to the database)
app.post("/sign_up", async function (req, res) {

    try {
        var username = req.body.username_input;
        var password = await bcrypt.hash(req.body.password_input, 10);
        var email = req.body.email_input;
        var addressInput = req.body.address_autocomplete;
        var mobile_number = req.body.mobile_number_input;
        var token, longitude, latitude;

        // Creating the token for the user
        const user = {
            username: username,
            password: password,
            email: email,
            address: addressInput,
            mobile_number: mobile_number
        }
        token = jwt.sign(user, process.env.ACCESS_TOKEN_SECRET);

        if (!mobile_number.startsWith("+65")) {
            mobile_number = "+65" + mobile_number;
        }

        if (mobile_number.split(" ").length == 2) {

            mobile_number = mobile_number.replace(" ", "");
        }


        // Getting all the longitude and latitude of the place
        var geocodeURL = "https://maps.googleapis.com/maps/api/geocode/json";

        console.log(addressInput);


        axios.get(geocodeURL, {
            params: {
                key: "AIzaSyDIWEKaDIthMAPmibgP4PEIUuNeCP69fi0", // googleMapAPI key
                address: addressInput
            }
        })
            .then(response => {
                longitude = response.data.results[0].geometry.location.lng;
                latitude = response.data.results[0].geometry.location.lat;

                // Inserting into the database
                var data = [
                    username, // Username
                    password, // Password
                    email, // Email
                    addressInput, // Address
                    token, // Token
                    mobile_number, // Mobile_number
                    longitude, // Longitude
                    latitude, // Latitude
                    2 // Status
                ];

                db.query("INSERT INTO user (Username, Password, Email, " +
                    "Address, Token, Mobile_number, Longitude, Latitude, Status) VALUES (?)", [data],
                    function (err, results, fields) {
                        if (err) throw err;

                        console.log(results);
                    });
            })
            .catch(err => {
                console.log(err);
            })

        return res.redirect("registration_success.html");
    } catch {
        res.redirect("/registration");
    }
});
