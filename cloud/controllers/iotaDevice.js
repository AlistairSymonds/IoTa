var iotaDevice = require('../models/iotaDevice');

exports.updateDeviceState = (function(req, res) {
    var device = new iotaDevice();
    device.name = req.body.name;
    device.type = req.body.type;
    device.quantity = req.body.quantity;

    device.save(function(err) {
        if (err)
            res.send(err);

        res.json({ message: 'Device state updated!', data: device });
    });
});