const express = require('express');
const axios = require('axios');
const cheerio = require('cheerio');
const firestore = require("firebase-admin/firestore");
const router = express.Router();
const database = firestore.getFirestore();
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
        let scrap = (await database
            .collection("users").doc(uid)
            .get()).data().scrap
        console.log(scrap);


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
            if (scrap.includes(title))
                dictObject['clipped'] = true
            else
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
        console.log(url);
        console.log('request arrived');

        // 웹 페이지 가져오기
        const response = await axios.get(url);
        const html = response.data;
        const data = [];
        // Cheerio를 사용하여 웹 페이지 파싱
        const $ = cheerio.load(html);
        for (let i = 1; i < 10; i++) {
            const selector = '#container > div.content-area > div.content-wrap > div.content > div > div.cd-area > div.info > ul > li:nth-child(' + i + ') ';
            const element = $(selector);

            // 요소의 텍스트 내용 가져오기
            if (element.length > 0) {
                const text = element.text().trim().split('\n');
                text.push('');
                console.log(text);
                data.push(text);
            } else {
                console.log('요소를 찾을 수 없습니다: ' + selector);
            }
            console.log('$i\n');
        }
        // 원하는 데이터 추출
        const imgUrl = $("div.img > div.thumb > img").attr("src")
        const field = data[0][1].trim();
        const targetAudience = data[1][1].trim()
        const organizer = data[2][1].trim()
        const sponsor = data[3][1].trim()
        const applicationPeriod = data[4][1].trim()
        const totalPrize = data[5][1].trim()
        const firstPrize = data[6][1].trim()
        const websiteURL = data[7][1].trim()

        jsonData = {
            "imgUrl": "https://www.wevity.com" + imgUrl,
            "field": field,
            "targetAudience": targetAudience,
            "organizer": organizer,
            "sponsor": sponsor,
            "applicationPeriod": applicationPeriod,
            "totalPrize": totalPrize,
            "firstPrize": firstPrize,
            "websiteURL": websiteURL
        }


        // 추출한 데이터를 JSON 형식으로 응답
        res.json(jsonData);
    } catch (error) {
        console.log(error);
        res.status(500).json({ error: 'Internal server error' });
    }
});
module.exports = router;
