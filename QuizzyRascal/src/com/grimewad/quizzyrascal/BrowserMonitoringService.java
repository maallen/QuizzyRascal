package com.grimewad.quizzyrascal;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

public class BrowserMonitoringService extends IntentService{
	
	public static final String BROWSER_RUNNING = "Browser Running";
	
	public static final String NOTIFICATION = "com.grimewad.quizzyrascal.ALERT";
	
	public static final String BROWSER_MONITORING_SERVICE = "BrowserMonitoringService";

	public BrowserMonitoringService() {
		super(BROWSER_MONITORING_SERVICE);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		createForegroundNotification();
		while(FullscreenActivity.MONITORING_ACTIVE){
			List<String> installedBrowsers = getInstalledBrowserPackageNames();
		    ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
		    List<RunningTaskInfo> tasks = activityManager.getRunningTasks(1);
		    browserMonitoring:
		    for(String packageName: installedBrowsers){
		    	for(RunningTaskInfo task: tasks){
		    		if(task.topActivity.getPackageName().equals(packageName)) {
		    			activityManager.killBackgroundProcesses(packageName);
			            sendAlert(packageName);
			            break browserMonitoring;
			        }
		    	}
		    }
		    try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {}
		}	
	}
	
	private void createForegroundNotification(){
		NotificationCompat.Builder notificationBuilder =
			    new NotificationCompat.Builder(this)
			    .setSmallIcon(R.drawable.ic_launcher)
			    .setContentTitle("Quizzy Rascal")
			    .setContentText("Monitoring in progress.")
			    .setTicker("Quizzy Rascal browser monitoring started.")
			    .setOngoing(true);
		Notification notification = notificationBuilder.build();
		startForeground(1, notification);
	}
	
	private List<String> getInstalledBrowserPackageNames(){
		
	    PackageManager packageManager = getApplicationContext().getPackageManager();
	    Intent intent = new Intent(Intent.ACTION_VIEW);
	    intent.setData(Uri.parse("http://www.google.com"));
	    List<String> installedBrowserPackageNames = new ArrayList<String>();
	    List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
	            PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo info : list) {
        	installedBrowserPackageNames.add(info.activityInfo.packageName);
        }
        
        return installedBrowserPackageNames;
	         
	}
	
	private void sendAlert(String browserName){
		Intent intent = new Intent(NOTIFICATION);
		intent.putExtra(BROWSER_RUNNING, browserName);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}

}
