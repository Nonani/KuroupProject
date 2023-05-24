const express = require('express');
const axios = require('axios');
const cheerio = require('cheerio');

const router = express.Router();

router.get('/list', async (req, res) => {
    try {
        const url = 'https://www.wevity.com/?c=find&s=1&gub=1'; // 대상 웹 페이지 URL을 여기에 입력

        // 웹 페이지 가져오기
        const response = await axios.get(url);
        const html = response.data;

        // Cheerio를 사용하여 웹 페이지 파싱
        const $ = cheerio.load(html);

        // 원하는 데이터 추출
        const contests = [];

        const listItems = $('.list').text()
        console.log(listItems)

        // 추출한 데이터를 JSON 형식으로 응답
        res.json(contests);
    } catch (error) {
        res.status(500).json({ error: 'Internal server error' });
    }
});

module.exports = router;
