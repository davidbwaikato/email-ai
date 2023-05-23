const fs      = require('fs');
const path    = require('path');
const express = require('express');
const cors    = require('cors');
const http    = require('http');

//
// Instantiate web-server 
//
const app    = express();
const server = http.createServer(app);

const httpHost = process.env.EMAIL_AI_LOCAL_HOST || "localhost";
const httpPort = process.env.EMAIL_AI_LOCAL_PORT || 3000;
const httpLocalServer = "http://" + httpHost + ":" + httpPort;

console.log("Allowing CORS access");
app.use(cors());


app.get('/getInfo', (req,res) => {
    
    console.log("API /getInfo called");

    // Fake some interesting data to return
    returnJSON = { "status": "ok", "info": { "exampleText": "example", "exampleVals": [ 1, 2, 3] } };
		   
    res.setHeader("Content-Type", "application/json");
    res.end(JSON.stringify(returnJSON));    	   
});


app.get('/getInfoAdvanced/:testParam', (req, res) => {
    testParam = req.params.testParam;
    console.log("API /test called with request param: " + testParam);

    // Fake some interesting data to return
    returnedParam = testParam.toUpperCase();
    
    returnJSON = { "status": "ok", "info": { "exampleText": returnedParam } };
    
    res.setHeader("Content-Type", "application/json");
    res.end(JSON.stringify(returnJSON));    	   

});


app.get('/', (req, res) => {
    res.sendFile(__dirname + '/index.html');
});


// The following can be used as a 'catch all', if required/needed
app.get('*', (req, res) => {
    console.error("404 Error - Unrecognized request: " + req.path);
    res.sendStatus(404);
});


server.listen(httpPort, () => {
    console.log('For all interfaces, listening on port: ' + httpPort);

    console.log('Local server: ' + httpLocalServer);
});



