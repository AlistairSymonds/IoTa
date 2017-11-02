// Load required packages
var mongoose = require('mongoose');


var iotaDevice   = new mongoose.Schema({
    name: String,
    type: String,
    quantity: Number
});

// Export the Mongoose model
module.exports = mongoose.model('iotaDevice', iotaDevice);