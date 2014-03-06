package com.grimewad.quizzyrascal;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.grimewad.quizzyrascal.util.SystemUiHider;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class FullscreenActivity extends Activity {
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;
	
	protected static int DEVICE_ID;
	
	public static boolean MONITORING_ACTIVE = false;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;
	
	private BroadcastReceiver receiver = new BroadcastReceiver() {

	    @Override
	    public void onReceive(Context context, Intent intent) {
	      Bundle bundle = intent.getExtras();
	      if (bundle != null) {
	        String runningBrowser = bundle.getString(BrowserMonitoringService.BROWSER_RUNNING);
	        notifyServer(runningBrowser);
	        Toast.makeText(FullscreenActivity.this, "Quizzy Rascal has identified an internet browser (" + runningBrowser +
	        		") is open on your screen! Please close this immediately. The Quiz master has been notified!", Toast.LENGTH_LONG).show();      
	      }
	    }
	  };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_fullscreen);

		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.fullscreen_content);

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
					// Cached values.
					int mControlsHeight;
					int mShortAnimTime;

					@Override
					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
					public void onVisibilityChange(boolean visible) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
							// If the ViewPropertyAnimator API is available
							// (Honeycomb MR2 and later), use it to animate the
							// in-layout UI controls at the bottom of the
							// screen.
							if (mControlsHeight == 0) {
								mControlsHeight = controlsView.getHeight();
							}
							if (mShortAnimTime == 0) {
								mShortAnimTime = getResources().getInteger(
										android.R.integer.config_shortAnimTime);
							}
							controlsView
									.animate()
									.translationY(visible ? 0 : mControlsHeight)
									.setDuration(mShortAnimTime);
						} else {
							// If the ViewPropertyAnimator APIs aren't
							// available, simply show or hide the in-layout UI
							// controls.
							controlsView.setVisibility(visible ? View.VISIBLE
									: View.GONE);
						}

						if (visible && AUTO_HIDE) {
							// Schedule a hide().
							delayedHide(AUTO_HIDE_DELAY_MILLIS);
						}
					}
				});

		// Set up the user interaction to manually show or hide the system UI.
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TOGGLE_ON_CLICK) {
					mSystemUiHider.toggle();
				} else {
					mSystemUiHider.show();
				}
			}
		});

		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		findViewById(R.id.scan_qr_code_button).setOnTouchListener(
				mDelayHideTouchListener);
		
  	  LocalBroadcastManager.getInstance(this).registerReceiver(receiver, 
			  new IntentFilter(BrowserMonitoringService.NOTIFICATION));
		
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}
	
	/**
	 * Scan and decode a QR Code using the device's camera
	 */
	public void scanQrCode(View view){
		
		Intent intent = new Intent(this, QrReaderActivity.class);
		intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
		startActivityForResult(intent, 0);
		
		Toast toast = Toast.makeText(this, "Start scanning QR code", Toast.LENGTH_SHORT);
	    toast.show();	
	    
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		   if (requestCode == 0) {
		      if (resultCode == RESULT_OK) {
		         String qrCodeContents = intent.getStringExtra("SCAN_RESULT");
		         associateWithQuizAccount(qrCodeContents);
		         Toast toast = Toast.makeText(this, "Successfully scanned QR Code", Toast.LENGTH_SHORT);
		         toast.show();
		         MONITORING_ACTIVE = true; 
		         startBrowserMonitoringService();
		      } else if (resultCode == RESULT_CANCELED) {
		    	  Toast toast = Toast.makeText(this, "Failed to scan QR Code. Please try again.", Toast.LENGTH_SHORT);
		    	  toast.show();
		      }
		   }
	}
	
	private void associateWithQuizAccount(String qrCodeContents) {
		/*
		 *  TODO implement logic to connect to server and associate 
		 *  device with Quizmaster account
		 */	
		try {
		    HttpClient httpClient = new DefaultHttpClient();
		    HttpGet get = new HttpGet(
		            "http://192.168.0.22:8080/QuizzyRascalServer/rest/getId");

		    HttpResponse response = httpClient.execute(get);
		    HttpEntity entity = response.getEntity();
		    if(entity != null){
		    	String jsonString = entity.toString();
		    	JSONObject json = new JSONObject(jsonString);
		    	String deviceIdString = (String) json.get("deviceId");
		    	DEVICE_ID = Integer.parseInt(deviceIdString);
		    	Toast.makeText(this, "Device Id set as " + DEVICE_ID, Toast.LENGTH_SHORT).show();
		    }
		    
		} catch (Exception E) {
		    E.printStackTrace();
		}
		
		
	}

	/**
	 * Starts an IntentService that monitors if a browser is running
	 */
	private void startBrowserMonitoringService(){
		Intent browserMonitoringServiceIntent = new Intent (this, BrowserMonitoringService.class);
 		startService(browserMonitoringServiceIntent);
	}
	
	/**
	 * Stops the application monitoring of browser activity
	 */
	public void stopMonitoring(View view){
		MONITORING_ACTIVE = false;
		stopService(new Intent (this, BrowserMonitoringService.class));
		removeNotification();
		/* TODO send signal to quizmaster that 
		 * monitoring has been stopped on this device
		 */
	}

	private void removeNotification() {
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationManager.cancelAll();
		
	}
	
	
	private void notifyServer(String browserName){
		Intent notifyServerServiceIntent = new Intent(this, NotifyServerService.class);
		notifyServerServiceIntent.putExtra("deviceId", DEVICE_ID);
		notifyServerServiceIntent.putExtra("openBrowser", browserName);
		startService(notifyServerServiceIntent);
	}
}
