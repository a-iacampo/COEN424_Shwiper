const functions = require('firebase-functions');
const kijiji = require("kijiji-scraper");

//For Firestore
const admin = require('firebase-admin');
const serviceAccount = require('./secrets/ServiceAccountKey.json');
admin.initializeApp({
    credential: admin.credential.cert(serviceAccount)   
});

const db = admin.firestore();

// http request 1
exports.FetchFromScraper = functions.https.onCall(async (data, context) => {
    if (!context.auth) {
        // Throwing an HttpsError so that the client gets the error details.
        throw new functions.https.HttpsError('failed-precondition', 'The function must be called while authenticated.');
    }

    const list = [];

    const options = {
        //Number of ads it fetches
        minResults: 20, //must be done in batches of 20 (ex: 20, 40, 60, ...) *Note keep scraping to a minimum to avoid detection and bans from Kijiji
    };

    const params = {
        locationId: kijiji.locations.QUEBEC.GREATER_MONTREAL,
        categoryId: kijiji.categories.BUY_AND_SELL.CLOTHING
    };

    let ads = await kijiji.search(params, options).then((ads) => {
        ads.forEach((ad) => {
            //TODO
            //Filter and clean title or description extra quotes
            var title = ad.title.replace(/['"]+/g, '');
            var description = ad.description.replace(/['"]+/g, '');
            list.push(`{
                title: "${title}",
                description: "${description}",
                image: "${ad.image}",
                price: "${ad.attributes.price}",
                location: "${ad.attributes.location}",
                url: "${ad.url}"
            }`);
        });

        const result = "[" + list + "]";
        return result;

    }).catch((error => {
        functions.logger.log(error);
        return error;
    }));

    return ads;
});

// // http request 2
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

//Run with Firebase serve