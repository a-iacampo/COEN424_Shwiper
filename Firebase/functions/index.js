const functions = require('firebase-functions');
const kijiji = require("kijiji-scraper");

// http request 1
exports.testScraper = functions.https.onRequest((req, res) => {

    const options = {
        //Number of ads it fetches
        minResults: 20
    };

    const params = {
        locationId: kijiji.locations.QUEBEC.GREATER_MONTREAL,
        categoryId: kijiji.categories.BUY_AND_SELL.CLOTHING,
        sortByName: "priceAsc"
    };

    kijiji.search(params, options).then((ads) => {
        // Use the ads array
        for (let i = 0; i < ads.length; ++i) {
            console.log(ads[i].description);
        }
        res.json(ads);
    }).catch((error => {
        console.log(error);
    }));
});

//Run with Firebase serve