var WebSocket = require('ws');
var ws = new WebSocket('ws://localhost:8085/main');
ws.on('open', function () {
    ws.send('something');
});
ws.on('message', function (message) {
    console.log('received: %s', message);
});
