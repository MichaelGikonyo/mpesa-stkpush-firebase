let functions = require('firebase-functions');

let admin = require('firebase-admin');

admin.initializeApp(functions.config().firebase);
const express = require('express');
const bodyParser = require('body-parser');

//Initialize our web App
const app = express();
app.use(bodyParser.json());
app.disable('x-powered-by');


//This is our actual callback url `Format will be www.example.com/api/myCallbackUrl`
app.post('/myCallbackUrl', (req, res) => {
    let response = {
        "ResultCode": 0,
        "ResultDesc": "Success"
    }
    //Send response back to safaricom that payload has been received successfully
    res.status(200).json(response);

      //Then handle data through above received payload as per your app logic.
    let body = req.body;
    let payload = JSON.stringify(body)

      console.log(payload)
	  
	  //validate the payload has some data and update it to realtime Database
  if (payload != null) {
      // initialize the variables b4 sending to database
      let Amount = body.Body.stkCallback.CallbackMetadata.Item[0].Value;
      let MpesaReceiptNumber = body.Body.stkCallback.CallbackMetadata.Item[1].Value;
      //let Balance = body.Body.stkCallback.CallbackMetadata.Item[2].Value;
      let TransactionDate = body.Body.stkCallback.CallbackMetadata.Item[3].Value;
      let PhoneNumber = body.Body.stkCallback.CallbackMetadata.Item[4].Value;

      console.log(Amount, MpesaReceiptNumber, TransactionDate, PhoneNumber);

      database.ref('/mpesaTrans/' + MpesaReceiptNumber)
      database.ref('/mpesaTrans/' + MpesaReceiptNumber + '/amount').set(Amount).stringify;
      database.ref('/mpesaTrans/' + MpesaReceiptNumber + '/mpesaId').set(MpesaReceiptNumber);
      //database.ref('/mpesaTrans/'+ MpesaReceiptNumber + '/balance').set(Balance);
      database.ref('/mpesaTrans/' + MpesaReceiptNumber + '/transDate').set(TransactionDate).stringify;
      database.ref('/mpesaTrans/' + MpesaReceiptNumber + '/phoneNumber').set(PhoneNumber).stringify;
  }

    let id =  body.Body.stkCallback.CheckoutRequestID

      const payloadSend = {
            data: {
                payload,
            },
             topic: id
        };

         return admin.messaging().send(payloadSend).catch(error=>{
         console.error(error)
         })


})

exports.api = functions.https.onRequest(app);