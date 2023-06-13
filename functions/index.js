const functions = require('firebase-functions');
const express = require('express');
var admin = require("firebase-admin");
const firestore = require("firebase-admin/firestore");

var serviceAccount = require("./kuroup-project-firebase-adminsdk-2nc6t-e7c247e774.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});
const database = firestore.getFirestore();
const contestRouter = require('./contest');
const teamRouter = require('./team');
const app = express();
app.use('/contest', contestRouter);
app.use('/team', teamRouter);
app.use(express.json());
app.use(express.urlencoded({
  extended: false
}))
app.get('/', async (req, res) => {

  res.send(tmp)
});


const port = process.env.PORT || 3000;
app.listen(port, () => {
  console.log(`server is listening at localhost:${process.env.PORT}`);
});

// test prefix를 가지는 요청을 express 라우터로 전달
// exports.app = functions.https.onRequest(app);