var connect = require('connect');
var serveStatic = require('serve-static');
connect().use(serveStatic('./')).listen(8080);
console.log('asd');

//var WebSocketServer = require('ws').Server
//var wss = new WebSocketServer({port: 8085, path: '/main'});
//wss.on('connection', function (ws) {
//    ws.on('message', function (message) {
//        console.log('received: %s', message);
//    });
//    ws.send('something');
//});
