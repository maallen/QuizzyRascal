package com.grimewad.quizzyrascal;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;

public class BrowserMonitoringService extends IntentService{
	
	public static final String BROWSER_RUNNING = "Browser Running";
	
	private static final String NOTIFICATION = "com.grimewad.quizzyrascal.ALERT";

	public BrowserMonitoringService() {
		super("BrowserMoitoringService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		while(FullscreenActivity.MONITORING_ACTIVE){
			List<String> installedBrowsers = getInstalledBrowserPackageNames();
		    ActivityManager activityManager = (ActivityManager) this.getSystemService( ACTIVITY_SERVICE );
		    List<RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
		    browserMonitoring:
		    for(String packageName: installedBrowsers){
		    	for(RunningAppProcessInfo processInfo: procInfos){
		    		if(processInfo.processName.equals(packageName)) {
			            sendAlert(packageName);
			            break browserMonitoring;
			        }
		    	}
		    }
		}	
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
