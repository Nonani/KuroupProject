const express = require('express');
const axios = require('axios');
const cheerio = require('cheerio');
const router = express.Router();

router.use(express.json());

router.post('/list', async (req, res) => {
    try {
        const { uid } = req.body;
        console.log(uid);
        console.log('request arrived');
        const url = 'https://www.wevity.com/?c=find&s=1&gub=1&cidx=21'; // 대상 웹 페이지 URL을 여기에 입력

        // 웹 페이지 가져오기
        const response = await axios.get(url);
        const html = response.data;

        // Cheerio를 사용하여 웹 페이지 파싱
        const $ = cheerio.load(html);

        // 원하는 데이터 추출
        let contests = [];



        const titleItems = $('ul.list>li>div.tit>a');
        const subtitleItems = $('ul.list>li>div.tit>div.sub-tit');
        const dDayItems = $('div.day');
        const readCntItems = $('div.read');

        for (let i = 0; i < titleItems.length; i++) {
            var dictObject = {}
            const title = $(titleItems[i].children[0]).text().trimEnd();
            const detail_url = $(titleItems[i]).attr('href');
            const sub_title = $(subtitleItems[i].children[0]).text().trimEnd();
            const d_day = $(dDayItems[i + 1].children[0]).text().trim()
            const read_cnt = $(readCntItems[i + 1]).text().trim();

            dictObject['title'] = title
            dictObject['detail_url'] = "https://www.wevity.com/" + detail_url
            dictObject['sub_title'] = sub_title
            dictObject['d_day'] = d_day
            dictObject['read_cnt'] = read_cnt
            dictObject['clipped'] = false
            contests.push(dictObject)
            // console.log(i + 1, contests[i]);

        }
        // 추출한 데이터를 JSON 형식으로 응답
        res.json(contests);
    } catch (error) {
        console.log(error);
        res.status(500).json({ error: 'Internal server error' });
    }
});


router.post('/detail', async (req, res) => {
    try {
        const { url } = req.body;
        console.log('request arrived');

        // 웹 페이지 가져오기
        const response = await axios.get(url);
        const html = response.data;

        // Cheerio를 사용하여 웹 페이지 파싱
        const $ = cheerio.load(html);

        // 원하는 데이터 추출
        // field
        // targetAudience
        // organizer
        // sponsor
        // applicationPeriod
        // totalPrize
        // firstPrize
        // websiteURL

        for (let i = 1; i < 10; i++) {
            var selector = '#container > div.content-area > div.content-wrap > div.content > div > div.cd-area > div.info > ul > li:nth-child(' + i + ')';
            const elements = $(selector);
            console.log(elements.text());
            // 각 li 요소의 텍스트 값 가져오기
        }



        const imageUrl = "https://www.wevity.com/" + $('div.img > div.thumb > img').attr('src');
        console.log(imageUrl)


        res.send("test");
    } catch (error) {
        console.log(error);
        res.status(500).json({ error: 'Internal server error' });
    }
});
module.exports = router;