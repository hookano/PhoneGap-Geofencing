/**
 * Geofencing.js
 *
 * Phonegap Geofencing Plugin
 * Copyright (c) Dov Goldberg 2014
 * http://www.ogonium.com
 * dov.goldberg@ogonium.com
 *
 */

function DGGeofencing() {
}

/*
Params:
NONE
*/
DGGeofencing.prototype.isRunningInBackground = function (params, success, fail) {
   return Cordova.exec(success, fail, "DGGeofencing", "isRunningInBackground", params);
};


/*
 Params:
 NONE
 */
DGGeofencing.prototype.initCallbackForRegionMonitoring = function (params, success, fail) {
    return Cordova.exec(success, fail, "DGGeofencing", "initCallbackForRegionMonitoring", params);
};

/*
 Params:
 #define KEY_REGION_ID       @"fid"
 #define KEY_REGION_LAT      @"latitude"
 #define KEY_REGION_LNG      @"longitude"
 #define KEY_REGION_RADIUS   @"radius"
 //#define KEY_REGION_ACCURACY @"accuracy" // not implemented!
 */
DGGeofencing.prototype.startMonitoringRegion = function (params, success, fail) {
    return Cordova.exec(success, fail, "DGGeofencing", "startMonitoringRegion", params);
};

/*
 Params:
 #define KEY_REGION_ID      @"fid"
 #define KEY_REGION_LAT     @"latitude"
 #define KEY_REGION_LNG     @"longitude"
 */
DGGeofencing.prototype.stopMonitoringRegion = function (params, success, fail) {
    return Cordova.exec(success, fail, "DGGeofencing", "stopMonitoringRegion", params);
};

DGGeofencing.prototype.clearMonitoringRegions = function (params, success, fail) {
    return Cordova.exec(success, fail, "DGGeofencing", "clearMonitoringRegions", params);
};

/*
 Params:
 NONE
 */
DGGeofencing.prototype.startMonitoringSignificantLocationChanges = function (success, fail) {
    return Cordova.exec(success, fail, "DGGeofencing", "startMonitoringSignificantLocationChanges", []);
};

/*
 Params:
 NONE
 */
DGGeofencing.prototype.stopMonitoringSignificantLocationChanges = function (success, fail) {
    return Cordova.exec(success, fail, "DGGeofencing", "stopMonitoringSignificantLocationChanges", []);
};

DGGeofencing.install = function () {
    if (!window.plugins) {
        window.plugins = {};
    }

    window.plugins.DGGeofencing = new DGGeofencing();
    return window.plugins.DGGeofencing;
};

cordova.addConstructor(DGGeofencing.install);