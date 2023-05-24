const functions = require('firebase-functions');
const express = require('express');
const contestRouter = require('./contest');
const app = express();
app.use('/contest', contestRouter);

app.get('/', (req, res) => {
  res.json({
    success: true,
  });
});

const port = process.env.PORT || 3000;
app.listen(port, () => {
  console.log(`server is listening at localhost:${process.env.PORT}`);
});

// test prefix를 가지는 요청을 express 라우터로 전달
// exports.app = functions.https.o?nRequest(app);