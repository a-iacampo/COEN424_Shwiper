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

    const uid = context.auth.uid;
    
    const options = {
        //Number of ads to fetch
        minResults: 40,
    };

    const params = {
        locationId: kijiji.locations.QUEBEC.GREATER_MONTREAL,
        categoryId: kijiji.categories.BUY_AND_SELL.CLOTHING,
        sortType: "DATE_DESCENDING"
    };
    
    const list = [];

    try{
        let ads = await kijiji.search(params, options);
    
        for(let ad of ads) {
            let itemId = ad.url.split('/').pop();

            
            functions.logger.log(itemId);
            // eslint-disable-next-line
            let doc = await db.collection('users').doc(`${uid}`).collection('likedAds').doc(`${itemId}`).get();
            
            if(!doc.exists) {
                //Clean data before sending to client
                var title = ad.title.replace(/['"]+/g, '');
                title = title.replace(/(\r\n|\n|\r)/gm, "\\n");
                var description = ad.description.replace(/['"]+/g, '');
                description = description.replace(/(\r\n|\n|\r)/gm, "\\n");
                var location = ad.attributes.location.replace(/(\r\n|\n|\r)/gm, "\\n");

                
                if(!(ad.image === "")) {
                    list.push(`{
                        title: "${title}",
                        description: "${description}",
                        image: "${ad.image}",
                        price: "${ad.attributes.price}",
                        location: "${location}",
                        url: "${ad.url}"
                    }`);
                }
            }
        }

        const result = "[" + list + "]";
        return result;
    }
    catch(err) {
        functions.logger.log(err);
    }
    return ads;
});

//Store Liked Ads to user's LikedAds collection
exports.storeLikedAd = functions.https.onCall(async (data, context) => {
    if (!context.auth) {
        // Throwing an HttpsError so that the client gets the error details.
        throw new functions.https.HttpsError('failed-precondition', 'The function must be called while authenticated.');
    }

    const uid = context.auth.uid;
    let itemId = data.url.split('/').pop();
    await db.collection('users').doc(`${uid}`).collection('likedAds').doc(`${itemId}`).set(data)
    .catch((error) => {
        functions.logger.log(error);
        return error;
    });
});

//fetch all liked ads from user's LikedAds collection
exports.fetchLikedAds = functions.https.onCall(async (data, context) => {
    if (!context.auth) {
        // Throwing an HttpsError so that the client gets the error details.
        throw new functions.https.HttpsError('failed-precondition', 'The function must be called while authenticated.');
    }
    
    var likedAds = []
    const uid = context.auth.uid;

    const snapshot = await db.collection('users').doc(`${uid}`).collection('likedAds').get().then( (snapshot) => {
        snapshot.forEach(doc => {
            let data = doc.data();
            likedAds.push(`{
                title: "${data.title}",
                description: "${data.description}",
                image: "${data.image}",
                price: "${data.price}",
                location: "${data.location}",
                url: "${data.url}"    
            }`);
        });
        const result = "[" + likedAds + "]";
        return result
        
    })
    .catch((error => {
        functions.logger.log(error);
        return error;
    }));
    
    return snapshot
});

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

//HTTPS onCall fetchFromScraper function
exports.getAd = functions.https.onCall(async (data, context) => {
    if (!context.auth) {
         // Throwing an HttpsError so that the client gets the error details.
         throw new functions.https.HttpsError('failed-precondition', 'The function must be called while authenticated.');
    }
    const adUrl = data.url;

    let ad = await kijiji.Ad.Get(adUrl).then((ad) => {
        var title = ad.title.replace(/['"]+/g, '');
        title = title.replace(/(\r\n|\n|\r)/gm, "\\n");
        var description = ad.description.replace(/['"]+/g, '');
        description = description.replace(/(\r\n|\n|\r)/gm, "\\n");
        var location = ad.attributes.location.replace(/(\r\n|\n|\r)/gm, "\\n");

        return (`{
            title: "${title}",
            description: "${description}",
            images: "${ad.images}",
            price: "${ad.attributes.price}",
            location: "${location}",
            size: "${ad.attributes.size}"
        }`);
    }).catch((error => {
        functions.logger.log(error);
        return error;
    }));

    return ad;
});