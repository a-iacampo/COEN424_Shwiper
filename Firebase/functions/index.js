const functions = require('firebase-functions');
const kijiji = require("kijiji-scraper");

//For Firestore
const admin = require('firebase-admin');
const serviceAccount = require('./secrets/ServiceAccountKey.json');
admin.initializeApp({
    credential: admin.credential.cert(serviceAccount)   
});

const db = admin.firestore();

// // http request 1
// exports.FetchFromScraper = functions.https.onRequest((req, res) => {
//     var adsList = [];

//     const options = {
//         //Number of ads it fetches
//         minResults: 20, //must be done in batches of 20 (ex: 20, 40, 60, ...) *Note keep scraping to a minimum to avoid detection and bans from Kijiji
//     };

//     const params = {
//         locationId: kijiji.locations.QUEBEC.GREATER_MONTREAL,
//         categoryId: kijiji.categories.BUY_AND_SELL.CLOTHING,
//         sortType: "DATE_DESCENDING",
//         distance: 50 //Distance in Km //Not sure how it knows current location
//         //sortByName: "priceAsc"
//     };

//     kijiji.search(params, options).then((ads) => {
//         ads.forEach((ad) => {
//             adsList.push(ad);
//         })
//         res.json(adsList);

//     }).catch((error => {
//         console.log(error);
//     }));
// });

// // http request 2
// exports.ReloadFromScraper = functions.https.onRequest((req, res) => {
//     var adsList = [];

//     const options = {
//         //Number of ads it fetches
//         minResults: 20, //must be done in batches of 20 (ex: 20, 40, 60, ...) *Note keep scraping to a minimum to avoid detection and bans from Kijiji
//     };

//     const params = {
//         locationId: kijiji.locations.QUEBEC.GREATER_MONTREAL,
//         categoryId: kijiji.categories.BUY_AND_SELL.CLOTHING,
//         sortType: "DATE_DESCENDING",
//         distance: 50 //Distance in Km //Not sure how it knows current location
//         //sortByName: "priceAsc"
//     };

//     kijiji.search(params, options).then((ads) => {
//         ads.forEach((ad) => {
//             adsList.push(ad);
//         })
//         res.json(adsList);

//     }).catch((error => {
//         console.log(error);
//     }));
// });

// // http request 3
// exports.StoreLikedAds = functions.https.onRequest((req, res) => {
//     const url = "https://www.kijiji.ca/v-clothing-lot/canada/wholesale-custom-hoodies-minimum-24/cas_364834";
//     const userId = "SJM8DSFN4923MDS"

//     const likedAd = {
//         urlToAd: url,
//         userID: userId
//     };

//     db.collection("LikedAds").add(likedAd);
//     res.send("Ad successfully Saved!");
// });

// http request 3
exports.test = functions.https.onCall((data, context) => {
    const list = []
    const url = "https://www.kijiji.ca/v-clothing-lot/canada/wholesale-custom-hoodies-minimum-24/cas_364834";
    const title = "New Post!";
    const title2 = "Post 2";

    list.push(`{
        url: "${url}",
        title: "${title}"
    }\n`);

    list.push(`{
        url: "${url}",
        title: "${title2}"
    }`);

    const result = "[" + list + "]";

    return result;
});

// http request 3
exports.FetchFromScraper = functions.https.onCall((data, context) => {
    const list = [];

    const options = {
        //Number of ads it fetches
        minResults: 1, //must be done in batches of 20 (ex: 20, 40, 60, ...) *Note keep scraping to a minimum to avoid detection and bans from Kijiji
    };

    const params = {
        locationId: kijiji.locations.QUEBEC.GREATER_MONTREAL,
        categoryId: kijiji.categories.BUY_AND_SELL.CLOTHING
    };

    // kijiji.search(params, options, (error, ads) => {
    //     if(!error) {
    //         ads.forEach((ad) => {
    //             list.push(`{
    //                 url: "${ad.url}",
    //                 title: "${ad.title}"
    //             }`);
    //         });

    //         const result = "[" + list + "]";
    //         functions.logger.log(result);
    //         return result;

    //     } else {
    //         functions.logger.log(error);
    //         return;
    //     }
    // });

    // const result = "[" + list + "]";
    // functions.logger.log(result);
    // return result;

    //let ads = await kijiji.search....

    return kijiji.search(params, options).then((ads) => {
        ads.forEach((ad) => {
            list.push(`{
                url: "${ad.url}",
                title: "${ad.title}"
            }`);
        });

        const result = "[" + list + "]";
        functions.logger.log(result);
        return result;

    }).catch((error => {
        functions.logger.log(error);
    }));
});

//Run with Firebase serve