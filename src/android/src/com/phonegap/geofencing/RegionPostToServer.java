package com.phonegap.geofencing;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;

import android.os.AsyncTask;
import android.os.Build;

import android.location.LocationManager;

import android.util.Log;

import android.content.SharedPreferences;

public class RegionPostToServer {
    public static final String TAG = "Geofencing"; 
    
    protected static void scheduleRegionChange(SharedPreferences settings, String regionId, String status) {
        PostRegionChangeTask task = new RegionPostToServer.PostRegionChangeTask();
        task.setRegion(settings, regionId, status);
        
        Log.d(TAG, "beforeexecute " +  task.getStatus());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            task.execute();
        Log.d(TAG, "afterexecute " +  task.getStatus());
    }

	protected static class PostRegionChangeTask extends AsyncTask<Object, Integer, Boolean> {
		String regionId;
		String status;
        SharedPreferences settings;

        public void setRegion(SharedPreferences settings, String regionId, String status) {
        	this.regionId = regionId;
        	this.status = status;
            this.settings = settings;
        }
        @Override
        protected Boolean doInBackground(Object...objects) {
            Log.d(TAG, "Executing PostLocationTask#doInBackground");
            
            postRegionChange(settings, regionId, status);
            return true;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            Log.d(TAG, "PostLocationTask#onPostExecture");
        }
    }

	 protected static boolean postRegionChange(SharedPreferences settings, String regionId, String status) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();


 			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		    nameValuePairs.add(new BasicNameValuePair("j_username", settings.getString("user", "user")));
		    nameValuePairs.add(new BasicNameValuePair("j_password", settings.getString("pass", "pass")));

		    HttpPost authRequest = new HttpPost(settings.getString("apiurl", "http://needtoset-apiurl")+"/api/authentication");
//			authRequest.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded;charset=UTF-8");
			authRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			HttpResponse authResponse = httpClient.execute(authRequest);
		    Log.d(TAG, "Auth Response:"+ authResponse.getStatusLine());
		    authResponse.getEntity().consumeContent();

            HttpPost request = new HttpPost(settings.getString("apiurl", "http://needtoset-apiurl")+"/api/crowds/"+regionId+"/"+status);

            Log.d(TAG, "Posting to " + request.getURI().toString());
            HttpResponse response = httpClient.execute(request);
            Log.i(TAG, "Response received: " + response.getStatusLine());
            if (response.getStatusLine().getStatusCode() == 200) {
                return true;
            } else {
                return false;
            }
        } catch (Throwable e) {
            Log.w(TAG, "Exception posting location: " + e);
            e.printStackTrace();
            return false;
        }
    }

}