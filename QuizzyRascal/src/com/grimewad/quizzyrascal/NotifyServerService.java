package com.grimewad.quizzyrascal;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

public class NotifyServerService extends IntentService{
	
	public static final String NOTIFY_SERVER_SERVICE = "NotifyServerService";

	public NotifyServerService() {
		super(NOTIFY_SERVER_SERVICE);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		try {
		    HttpClient httpClient = new DefaultHttpClient();
		    HttpPost post = new HttpPost(
		            "http://192.168.0.22:8080/QuizzyRascalServer/rest/notifyServer");
		    post.setHeader("content-type", "application/json");

		    JSONObject data = new JSONObject();
		    data.put("deviceId", intent.getExtras().get("deviceId"));
		    data.put("openBrowser", intent.getExtras().get("openBrowser"));

		    StringEntity entity = new StringEntity(data.toString());
		    post.setEntity(entity);
		    httpClient.execute(post);
		    
		} catch (Exception E) {
		    E.printStackTrace();
		}
		
	}

}
