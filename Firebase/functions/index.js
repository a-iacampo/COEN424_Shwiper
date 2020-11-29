const functions = require('firebase-functions');
const kijiji = require("kijiji-scraper");

//For Firestore
const admin = require('firebase-admin');
const serviceAccount = require('./secrets/ServiceAccountKey.json');
admin.initializeApp({
    credential: admin.credential.cert(serviceAccount)   
});

const db = admin.firestore();


//Initialize App
const firebase = require('firebase/app');
require('firebase/auth');
require('firebase/firestore');
const config = require('./secrets/firebaseConfig.json');
firebase.initializeApp(config);

//HTTPS onCall fetchFromScraper function
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

//Store Liked Ads to user's LikedAds collection
exports.storeLikedAds = functions.https.onCall(async (data, context) => {

    if (!context.auth) {
        // Throwing an HttpsError so that the client gets the error details.
        throw new functions.https.HttpsError('failed-precondition', 'The function must be called while authenticated.');
    }

    const uid = context.auth.uid;
    const url = data.url;

    return await db.collection('users').doc(`${uid}`).collection('LikedAds').set({"urlToAd" : `${uid}`});

});

//fetch all liked ads from user's LikedAds collection
exports.storeLikedAds = functions.https.onCall(async (data, context) => {

    if (!context.auth) {
        // Throwing an HttpsError so that the client gets the error details.
        throw new functions.https.HttpsError('failed-precondition', 'The function must be called while authenticated.');
    }
    
    var likedAds
    const uid = context.auth.uid;

    let doc = await db.collection('users').doc(`${uid}`).collection('LikedAds').then((docs) => {
        docs.forEach((doc) => {
            list.push(doc);
        });
        const result = "[" + likedAds + "]";
        return result;

    }).catch((error => {
        functions.logger.log(error);
        return error;
    }));

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

//HTTPS onCall signup function
exports.signup = functions.https.onCall(async (data, context) => {
    const name = data.name;
    const email = data.email;
    const password = data.password;
    const confirmPassword = data.confirmPassword;

    let result;

    if (password === confirmPassword) {
        result = await firebase.auth().createUserWithEmailAndPassword(email, password).then((user) => {
            return firebase.firestore().collection("users").add({
                name: name,
                email: email,
            }).then(() => {
                return "Successfully created account";
            }).catch((error) => {
                return error.message;
            });
        }).catch((error) => {
            return error.message
        });
    } else {
        result = "Passwords do not match!"
    }

    return result;
});

//HTTPS onCall login function
exports.login = functions.https.onCall(async (data, context) => {
    const email = data.email;
    const password = data.password;

    let result;

    result = await firebase.auth().signInWithEmailAndPassword(email, password).then(() => {
        return "Successfully logged into account";
    }).catch((error) => {
        return error.message
    });

    return result;
});

//HTTPS onCall login function
exports.logout = functions.https.onCall(async (data, context) => {
    let result = await firebase.auth().signOut().then(() => {
        return "Successfully signed out of account";
    }).catch((error) => {
        return error.message
    });

    return result;
});

//Run with Firebase serve