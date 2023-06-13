const express = require('express');
const firestore = require("firebase-admin/firestore");
const router = express.Router();
router.use(express.json());
const database = firestore.getFirestore();


router.post('/supporting', async (req, res) => {
    try {
        const { uid } = req.body;
        console.log(uid);
        const map = {};
        const postsRef = database.collection('posts');
        const querySnapshot1 = await postsRef
            .where('members_info', 'array-contains', { "uid": uid, "state": "waiting" })
            .get();
        const waiting_list = []
        querySnapshot1.forEach(doc => {
            waiting_list.push(doc.data());
        });
        const accepted_list = []
        const querySnapshot2 = await postsRef
            .where('members_info', 'array-contains', { "uid": uid, "state": "accepted" })
            .get();
        querySnapshot2.forEach(doc => {
            accepted_list.push(doc.data());
        });
        map["accepted_list"] = accepted_list;
        map["waiting_list"] = waiting_list;
        res.send(map);
    } catch (error) {
        console.log(error);
        res.status(500).json({ error: 'Internal server error' });
    }

});
router.post('/created', async (req, res) => {

    try {
        const { uid } = req.body;
        console.log(uid);
        const collectionRef = database.collection('posts');
        const querySnapshot1 = await collectionRef.where('leader_uid', '==', uid).get();

        const map = {};
        const accepted_list = [];
        const waiting_list = [];
        const accepted_users = [];
        const waiting_users = [];

        querySnapshot1.forEach(doc => {
            doc.data().members_info.forEach(member => {
                if (member.state == "accepted") {
                    accepted_list.push(member.uid);

                } else if (member.state == "waiting") {
                    waiting_list.push(member.uid);
                }
            });
        });

        console.log(waiting_list);
        console.log(accepted_list);
        const usersRef = database.collection('users');

        if (waiting_list.length != 0) {
            for (const _uid of waiting_list) {
                const doc = await usersRef.doc(_uid).get();
                if (doc.exists) {
                    waiting_users.push(doc.data());
                } else {
                    console.log(`User with uid ${uid} does not exist.`);
                }
            }
        }

        if (accepted_list.length != 0) {
            for (const _uid of accepted_list) {
                const doc = await usersRef.doc(_uid).get();
                if (doc.exists) {
                    accepted_users.push(doc.data());
                } else {
                    console.log(`User with uid ${uid} does not exist.`);
                }
            }
        }
        map["waiting_users"] = waiting_users;
        map["accepted_users"] = accepted_users;
        res.send(map);
    } catch (error) {
        console.log(error);
        res.status(500).json({ error: 'Internal server error' });
    }
});

module.exports = router;