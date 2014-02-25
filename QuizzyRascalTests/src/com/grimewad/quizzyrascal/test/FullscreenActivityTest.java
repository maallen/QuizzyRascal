package com.grimewad.quizzyrascal.test;

import android.content.Intent;
import android.hardware.Camera;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;

import com.grimewad.quizzyrascal.FullscreenActivity;
import com.grimewad.quizzyrascal.R;

public class FullscreenActivityTest extends ActivityUnitTestCase<FullscreenActivity> {

	private Intent mLaunchIntent;
	
	
	public FullscreenActivityTest(){
		super(FullscreenActivity.class);
	}
	

	@Override
	protected void setUp() throws Exception{
		super.setUp();
		mLaunchIntent = new Intent(getInstrumentation()
                .getTargetContext(), FullscreenActivity.class);
		
		
	}
	
	@SmallTest
	public void testScanButtonOnClickIntentStart() {
		startActivity(mLaunchIntent, null, null);
		final Button scanQrCodeButton = (Button) getActivity().findViewById(R.id.scan_qr_code_button);
		scanQrCodeButton.performClick();
		
		final Intent launchIntent = getStartedActivityIntent();
		final String expectedIntentType = "com.grimewad.quizzyrascal.QrReaderActivity";
		final String actualIntentType = launchIntent.resolveActivity(getInstrumentation().getContext().
				getPackageManager()).getClassName();
		assertNotNull("Intent is not null", launchIntent);	
		assertTrue(actualIntentType.equals(expectedIntentType));
		
	}

}
