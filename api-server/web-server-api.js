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


app.get('/api/getInfo', (req,res) => {
    
    console.log("API /getInfo called");

    // Fake some interesting data to return
    returnJSON = { "status": "ok", "info": { "exampleText": "example", "exampleVals": [ 1, 2, 3] } };
		   
    res.setHeader("Content-Type", "application/json");
    res.end(JSON.stringify(returnJSON));    	   
});


app.get('/api/getInfoAdvanced/:testParam', (req, res) => {
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


// The following can be used as a 'catch all', if needed
//
// app.get('*', (req, res) => {
// });


server.listen(httpPort, () => {
    console.log('For all interfaces, listening on port: ' + httpPort);

    console.log('Local server: ' + httpLocalServer);
});



