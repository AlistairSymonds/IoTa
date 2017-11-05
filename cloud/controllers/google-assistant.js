exports.postGoogleAssistant = function (req, res) {
    if (!req.body.inputs) {
        res.status(400).send({error: "missing inputs field"});
        return;
    }

    if (!req.body.inputs[0].intent) {
        res.status(400).send({error: "missing inputs['intent'] field"});
        return;
    }


    if (req.body.inputs[0].intent == "action.devices.SYNC") {
        sync(req, res);
    } else {
        res.status(400).send({error: "invalid intent field"});
    }


    return;

};

sync = function (req, res) {
    var fakeSync = {
        requestId: req.body.requestId,
        payload: {}
    }
    var fakeDevice = {
        id: "fake",
        type: "action.devices.types.SWITCH",
        traits: ["action.devices.traits.OnOff"],
        name: {name: "Switch Boi"},
        willReportState: false

    }

    var devicesArray = [fakeDevice];

    fakeSync.payload['devices'] = devicesArray;
    console.log(JSON.stringify(fakeSync, null, 4));
    res.json(fakeSync);
    res.status(200).send();
}

var execute = function (req, res) {
    console.log()
}