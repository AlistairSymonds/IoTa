// Load required packages
var passport = require('passport');
var BasicStrategy = require('passport-http').BasicStrategy;
var User = require('../models/user');
var Client = require('../models/client');
var BearerStrategy = require('passport-http-bearer').Strategy;
var LocalStrategy = require('passport-local');
var CustomBearerStrategy = require('passport-http-custom-bearer');
var Token = require('../models/token');

passport.use(new BasicStrategy(
    function(username, password, callback) {
        User.findOne({ username: username }, function (err, user) {
            console.log("Someone is trying to auth with basic");
            if (err) { return callback(err); }

            // No user found with that username
            if (!user) { return callback(null, false); }

            // Make sure the password is correct
            user.verifyPassword(password, function(err, isMatch) {
                if (err) { return callback(err); }

                // Password did not match
                if (!isMatch) { return callback(null, false); }

                // Success
                return callback(null, user);
            });
        });
    }
));


passport.use('client-basic',new LocalStrategy(
    {
        usernameField: 'client_id',
        passwordField: 'client_secret'
    },
    function(username, password, done) {
        Client.findOne({ clientId: username }, function (err, client) {

            if (err) { return done(err); }
            if (!client || client.secret !== password) { return done(null, false); }

            return done(null, client);
        });
    }
));





passport.use('api-bearer', new CustomBearerStrategy(

    function(token, done) {
        User.findOne({ token: token }, function (err, user) {
            console.log(token);
            if (err) {
                console.log("errr");
                console.log(err);
                return done(err); }
            if (!user) { return done(null, false); }
            return done(null, user, { scope: 'all' });
        });
    }
));


passport.use('my-bearer',new BearerStrategy(
    function(value, callback) {

        Token.findOne({value: value }, function (err, token) {
            console.log("Someone is trying to auth with");
            console.log(value);
            if (err) {
                console.log(err);
                return callback(err);
            }
            console.log("Found token of %s", token);
            // No token found
            if (!token) { return callback(null, false); }

            User.findOne({ _id: token.userId }, function (err, user) {
                if (err) { return callback(err); }
                console.log("Matched token to user %s", user.username);
                // No user found
                if (!user) { return callback(null, false); }

                // Simple example with no scope
                callback(null, user, { scope: '*' });
            });
        });
    }
));

exports.isAuthenticated = passport.authenticate(['basic', 'my-bearer'], { session : false });
exports.isClientAuthenticated = passport.authenticate('client-basic', { session : false });