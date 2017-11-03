require('dotenv').config()

var express = require('express');
var mongoose = require('mongoose');
var bodyparser = require('body-parser');
var passport = require('passport');
var session = require('express-session');


var deviceController = require('./controllers/iotaDevice');
var userController = require('./controllers/user');
var authController = require('./controllers/auth');
var clientController = require('./controllers/client');
var oauth2Controller = require('./controllers/oauth2');


var app = express();
var port = 3000;
var router = express.Router();

mongoose.connect('mongodb://localhost:27017/iota');

app.use(bodyparser.urlencoded({
    extended: true
}));

//app.use(bodyparser.json);

app.use(session({
    secret: 'Super Secret Session Key',
    saveUninitialized: true,
    resave: true
}));


app.use(passport.initialize());
app.set('view engine', 'jade');


app.all('*', function(req, res, next) {
    console.log("=======BEGIN INCOMING REQUEST=======");
    console.log(req.method + req.url);
    console.log("headers: ");
    console.log(req.headers);
    console.log("body ");
    console.log(req.body);


    console.log("========END INCOMING REQUEST========");

 next();
});

router.get('/', function(req, res) {
    res.render('index', {title: 'IoTa'});
});




router.route('/device')
    .post(authController.isAuthenticated, deviceController.updateDeviceState);


router.route('/users')
    .post(userController.postUsers)
    .get(authController.isAuthenticated,userController.getUsers);

router.route('/clients')
    .post(authController.isAuthenticated, clientController.postClients)
    .get(authController.isAuthenticated, clientController.getClients);

// Create endpoint handlers for oauth2 authorize
router.route('/oauth2/authorize')
    .get(authController.isAuthenticated, oauth2Controller.authorization)
    .post(authController.isAuthenticated, oauth2Controller.decision);

// Create endpoint handlers for oauth2 token
router.route('/oauth2/token')
    .post(authController.isClientAuthenticated, oauth2Controller.token);




app.use('/', router);

app.listen(port);
console.log("Running on " + port);