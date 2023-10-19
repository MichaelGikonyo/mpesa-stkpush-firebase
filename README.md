# mpesa-stkpush-demo
1. You need to have configured firebase.
2. install and deploy firebase cloud function. (Firebase Blaze Plan).
3. open terminal on android studio and execute each command. 
         : npm install -g firebase-tools
		 : firebase login
		 : firebase init functions
		 : choose, use an Existing project
		 : for language, Javascript
		 : for ESLint, choose N.
		 :To install dependencies with npm? y

4. once you see firebase initialization is complete continue with the following commands on terminal
         : express body-parser
5. once youve installed body-parser your good to go. 

# code can be found at functions/index.js 
6. on functions folder open index.js paste the code to write mpesa transaction to realtime database.
7. Deploy index.js on terminal 
            : firebase deploy --only functions	 

8. add the folowing depedency on your graddle app file.
    dependencies {
         implementation("com.androidstudy.daraja:daraja:2.0.1")
    }



## Lipa na M-Pesa Online Payment API

9. The following table highlights the requirements needed by Daraja, as described in the [Safaricom Developer API Page](https://developer.safaricom.co.ke/lipa-na-m-pesa-online/apis/post/stkpush/v1/processrequest)

| Name                  | Description           | Parameter Type    | Possible Values |
| -------------         |:--------------------: | ----------------: | ---------------:|
| BusinessShortCode     | The organization shortcode used to receive the transaction        | Numeric             | Shortcode (6 digits)           |
| Passkey     | Lipa Na Mpesa Online PassKey       | Alpha-Numeric              |           | 
| Amount     | The amount to be transacted      | Numeric             | 100           |
| PhoneNumber     | The MSISDN sending the funds        | Numeric             | MSISDN (12 digits)          |
| CallBackURL     | Call Back URL        | URL             | https://ip or domain:port/path           |
| AccountReference     | Account Reference        | Alpha-Numeric	             | Any combinations of letters and numbers |
| TransactionDesc     | Description of the transaction        | String             | any string of less then 20 characters          |

> Get the Pass Key Here : https://developer.safaricom.co.ke/test_credentials

10. open graddle.properties and paste consumerKey, secretKey, and callback url
# for callback url, open your firebase project on the browser click on function. copy the url.
