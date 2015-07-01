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



import static com.phonegap.geofencing.DGGeofencing.TAG;

public class RegionPostToServer {
    protected static String apiurl = "http://NeedtoSet-apiurl-oninit";
    protected static String user = "";
    protected static String pass = "";
    
    static {
        SharedPreferences  settings = Context.getApplicationContext().getSharedPreferences();
        apiurl = settings.getString("apiurl");
        user = settings.getString("user");
        pass = settings.getString("pass");
    }

    protected static void scheduleRegionChange(String regionId, String status) {
        PostRegionChangeTask task = new RegionPostToServer.PostRegionChangeTask();
        task.setRegion(regionId, status);
        
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
        
        public void setRegion(String regionId, String status) {
        	this.regionId = regionId;
        	this.status = status;
        }
        @Override
        protected Boolean doInBackground(Object...objects) {
            Log.d(TAG, "Executing PostLocationTask#doInBackground");
            
            postRegionChange(regionId, status);
            return true;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            Log.d(TAG, "PostLocationTask#onPostExecture");
        }
    }

	 protected static boolean postRegionChange(String regionId, String status) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();


 			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		    nameValuePairs.add(new BasicNameValuePair("j_username", user));
		    nameValuePairs.add(new BasicNameValuePair("j_password", pass));

		    HttpPost authRequest = new HttpPost(apiurl+"/api/authentication");
//			authRequest.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded;charset=UTF-8");
			authRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			HttpResponse authResponse = httpClient.execute(authRequest);
		    Log.d(TAG, "Auth Response:"+ authResponse.getStatusLine());
		    authResponse.getEntity().consumeContent();

            HttpPost request = new HttpPost(apiurl+"/api/crowds/"+regionId+"/"+status);

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