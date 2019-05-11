# IoTa
Internet of Things, by Alistair

Note: I've mainly moved onto using home assistant for my IoT projects now, originally this was to avoid having to buy a Raspberrry Pi and learn something about networked code when dealing with micrcontrollers. I feel like I've learned a lot of code and have bought a Pi so this is pmost likely all that will be developed - it ended up going way too far into webdev/server/UI territory - not what I'm good at or want to learn.


Invigorated by 'work' in WiFi WS2812 repo I decided its time to make the "proper" version. Extensible, proper ui, modularity, y'know - all those good things we want in modern software. Then I needed to make something similar for an embedded device (ESP8266). Docs and explanations will be coming as I finish more, this is by and large a learning experience.

Currently working functions
+ internal diagnostics about esp8266
+ serial -> teensy 3.2 -> ws2812b led display
+ network ping
+ ESP32 based wireless oscilloscope


ESP32 oscilloscope demo:
https://www.youtube.com/watch?v=EMdqRHePBUg


If anyone cares, I'll be keeping an album of things I've recording about the project here, it's mostly just going to be demos and other fluff but it will all be here
https://photos.app.goo.gl/47WBGpqVN4JXLkwDA

Additionally if you hate well written code and good demos you can check out a quick demo of the precursor to this, my WiFI WS2818
https://www.youtube.com/watch?v=Iv1DlcE2AxI
