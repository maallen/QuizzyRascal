package com.grimewad.quizzyrascal;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.widget.Toast;

public class BrowserMonitoringService extends IntentService{

	public BrowserMonitoringService() {
		super("BrowserMoitoringService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		while(FullscreenActivity.MONITORING_ACTIVE){
			List<String> installedBrowsers = getInstalledBrowserPackageNames();
		    ActivityManager activityManager = (ActivityManager) this.getSystemService( ACTIVITY_SERVICE );
		    List<RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
		    for(int i = 0; i < procInfos.size(); i++){
		    	for(String packageName: installedBrowsers){
			        if(procInfos.get(i).processName.equals(packageName)) {
			            Toast.makeText(getApplicationContext(), "Browser is running", Toast.LENGTH_LONG).show();
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
	
	public final class Constants{
		
		// Defines a custom Intent action
	    public static final String BROADCAST_ACTION =
	        "com.example.android.threadsample.BROADCAST";
	    
	    // Defines the key for the status "extra" in an Intent
	    public static final String EXTENDED_DATA_STATUS =
	        "com.example.android.threadsample.STATUS";
	    
	}

}
