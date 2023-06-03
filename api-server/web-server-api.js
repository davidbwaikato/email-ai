const fs         = require('fs');
const path       = require('path');
const express    = require('express');
const cors       = require('cors');
const http       = require('http');
const bodyParser = require('body-parser')


const { Configuration, OpenAIApi } = require("openai");

const configuration = new Configuration({
  apiKey: process.env.OPENAI_API_KEY,
});
const openai = new OpenAIApi(configuration);



//
// Instantiate web-server 
//
const app    = express();
const server = http.createServer(app);

const httpHost = process.env.EMAIL_AI_LOCAL_HOST || "localhost";
const httpPort = process.env.EMAIL_AI_LOCAL_PORT || 3000;
const httpLocalServer = "http://" + httpHost + ":" + httpPort;

app.use(bodyParser.json())
app.use(bodyParser.urlencoded({ extended: false }))

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

async function processTLDR(text_in)
{
    const response = await openai.createCompletion({
	model: "text-davinci-003",
	prompt: text_in,
	temperature: 0.7,
	max_tokens: 60,
	top_p: 1.0,
	frequency_penalty: 0.0,
	presence_penalty: 1,
    });

    const text_out = response.data.choices

    console.log("processTLDR() away to return the JavaScript Object:");
    console.log("----");
    console.log(JSON.stringify(text_out));
    console.log("----");
    
    return text_out;
}

app.get('/tldr', async (req, res) => {

    const text_in = req.query.text

    const text_out = await processTLDR(text_in);
    
    returnJSON = { "status": "ok", "info": { "text": text_out } };
    
    res.setHeader("Content-Type", "application/json");
    res.end(JSON.stringify(returnJSON));    	   

});

async function processSubject(text_in)
{
    const response = await openai.createCompletion({
        model: "text-davinci-003",
        prompt: text_in,
        temperature: 0.7,
        max_tokens: 30,
        n: 1,
	stop: '\n'
    });

    const text_out = response.data.choices

    console.log("processSubject() away to return the JavaScript Object:");
    console.log("----");
    console.log(JSON.stringify(text_out));
    console.log("----");

return text_out;
}

app.post('/subject', async (req, res) => {
    let data = req.body;
    let text_in = data.text;

    const subject = await processSubject(text_in)

    returnJSON = { "status": "ok", "info": { "Subject": subject } };

    res.setHeader("Content-Type", "application/json");
    res.end(JSON.stringify(returnJSON));
});

app.get('/subject', async (req, res) => {

    const text_in = req.query.text

    const text_out = await processSubject(text_in);

    returnJSON = { "status": "ok", "info": { "text": text_out } };

    res.setHeader("Content-Type", "application/json");
    res.end(JSON.stringify(returnJSON));

});

app.post('/tldr', async (req, res) => {
    let data = req.body;
    let text_in = data.text;

    const text_out = await processTLDR(text_in);

    returnJSON = {"status": "OK", "info": { "text": text_out}};

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



