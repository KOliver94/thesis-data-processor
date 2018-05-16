var MongoClient = require('mongodb').MongoClient;
var url = "mongodb://localhost:27017/";
var async = require('async');
var fs = require('fs');

var microsoft_filtered = [];
var google_filtered = [];

/* Microsoft Store */
function microsoftStoreFilter() {
    MongoClient.connect(url, function (err, db) {
        if (err) throw err;
        var dbo = db.db("microsoft-store");
        var amount = new Array(24).fill(0);

        /* Exclude the _id field from the result: */
        dbo.collection("apps").find({}, {projection: {_id: 0}}).toArray(function (err, result) {
            if (err) throw err;
            result.forEach(function (object) {

                /* Sums the number permissions requested by the app*/
                amount[object.permission.length]++; //console.log is needed after db.close

                /* Collect apps that need:
                *      - more than 7 permissions
                *      - "Uses all system resources" permission
                */
                if (object.permission.length >= 7) {
                    microsoft_filtered.push(object);
                }
                else object.permission.forEach(function (permission) {
                    if (permission == "Uses all system resources")
                        microsoft_filtered.push(object);
                })
            });
            db.close();
            //writeToFile(microsoft_filtered, "microsoft_filtered.txt");
            googlePlaySearch();
        });
    });
}

/* Google Play */
function googlePlayFilter() {
    MongoClient.connect(url, function (err, db) {
        if (err) throw err;
        var dbo = db.db("google-play-test");
        var runned = 0; //used to count how many times did the function run

        /* Getting the name of collections */
        dbo.listCollections().toArray(function (err, collInfos) {
            for (var i = 0; i < collInfos.length; i++) {

                /* Exclude the _id field from the result: */
                dbo.collection(collInfos[i].name).find({}, {projection: {_id: 0}}).toArray(function (err, result) {
                    if (err) throw err;
                    var amount = new Array(50).fill(0);
                    result.forEach(function (object) {

                        /* Sums the number permissions requested by the app */
                        amount[object.permission.length]++;
                        /* It is needed after db.close();
                        *    console.log(collInfos[runned].name);
                        *    console.log(amount);
                        */

                        /* Collect apps that need:
                               - more than 20 permissions
                               - Sensitive permissions
                        */
                        if (object.permission.length >= 20) {
                            google_filtered.push(object);
                        }
                        else object.permission.forEach(function (permission) {
                             if (permission.permission == "body sensors (like heart rate monitors)" ||
                                 permission.permission.indexOf("calendar") > -1 ||
                                 permission.permission.indexOf("contacts") > -1 ||
                                 permission.permission.indexOf("storage") > -1 ||
                                 permission.permission.indexOf("calendar") > -1 ||
                                 permission.permission.indexOf("location") > -1 ||
                                 permission.permission.indexOf("SMS") > -1 ||
                                 permission.permission.indexOf("call") > -1 ||
                                 permission.permission == "take pictures and videos" ||
                                 permission.permission == "record audio" ||

                                )
                                 google_filtered.push(object);
                        })
                    });
                    db.close();
                    runned++;
                });
            }
        });
    });
}

function writeToFile(object, filename) {
    fs.writeFile(filename, JSON.stringify(object, null, 2), function (err) {
        if (err) throw err;
        console.log('Saved!');
    });
}

function googlePlaySearch() {
    var matches = [];
    MongoClient.connect(url, function (err, db) {
        if (err) throw err;
        var dbo = db.db("google-play-test");

        /* Getting the name of collections */
        dbo.listCollections().toArray(function (err, collInfos) {
            for (var i = 0; i < collInfos.length; i++) {

                var actual = collInfos[i].name

                microsoft_filtered.forEach(function (app) {
                    /* Find developer with "LIKE" */
                    dbo.collection(actual).find({"details.developer": app.detail.developer}, {projection: {_id: 0}}).toArray(function (err, result) {
                        if (err) throw err;
                        if (result.length > 0) {
                            console.log(result);
                        }
                        db.close();
                    });
                });
            }
        });
    });
}

microsoftStoreFilter();