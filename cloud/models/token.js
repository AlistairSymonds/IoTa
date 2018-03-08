// Load required packages
var mongoose = require('mongoose');

// Define our token schema
var TokenSchema   = new mongoose.Schema({
    userId: { type: String, required: true },
    tokenType: { type: String, required: true },
    value: { type: String, required: true },
    expiresIn: { type: String, required: true }
});

// Export the Mongoose model
module.exports = mongoose.model('Token', TokenSchema);