package com.grimewad.quizzyrascal.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.isA;

import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.google.zxing.Result;

import com.grimewad.quizzyrascal.FullscreenActivity;
import com.grimewad.quizzyrascal.QrReaderActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;


public class QrReaderActivityTest extends ActivityUnitTestCase<QrReaderActivity>{

	private Intent mLaunchIntent;
	
	@Spy
	private QrReaderActivity qrReader;
	
	public QrReaderActivityTest() {
		
		super(QrReaderActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception{
		
		super.setUp();
		MockitoAnnotations.initMocks(this);
		mLaunchIntent = new Intent(getInstrumentation()
                .getTargetContext(), QrReaderActivity.class);		
	}
	
	@MediumTest
	public void testActivityOpensCamera(){
		startActivity(mLaunchIntent, null, null);
		try{
			Camera camera = Camera.open();
			assertTrue(false);
		}catch(RuntimeException e){
			assertTrue(true);
		}
		
		
	}

}
