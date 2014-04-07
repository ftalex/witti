//ECE 573 Project
//Team: Witty
//Date: 3/13/14
//Authors: Brianna Heersink, Brian Smith, Alex Warren

package com.witti.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.witti.cloudview.CloudRenderer;
import com.witti.cloudview.CloudSurfaceView;
import com.witti.wittiapp.R;

public class DisplayActivity extends Activity {
    private static final String CAT_TAG = "WITTI_Display";
    private static final String DEBUG = "DEBUG_TAG";
    private static final int PLAY_RATE = 20; //Hz

    private CloudSurfaceView mCloudSurfaceView;
    private CloudRenderer mRenderer;
    private CloudCamera mCamera;

    /*
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
        mRenderer.mTime += .02;
        mRenderer.setCamera((float) (10*Math.cos(mRenderer.mTime)), (float) (10*Math.sin(mRenderer.mTime)), (float) (5+5*Math.sin(.01*mRenderer.mTime)),
                                      0.0f,   0.0f,  0.0f,
                                      0.0f,   0.0f,  1.0f);
            timerHandler.postDelayed(this, 1000 / PLAY_RATE);
        }
    };*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_display);
		

        mCamera = new CloudCamera();
        mCloudSurfaceView = (CloudSurfaceView) findViewById(R.id.cloud_surface_view);
        mCloudSurfaceView.setCamera(mCamera);

  
        // tried doing this in CloudSurfaceView but caused null pointer
        
        mRenderer = new CloudRenderer(mCloudSurfaceView, mCamera);
        mCloudSurfaceView.setEGLContextClientVersion(2);
        mCloudSurfaceView.setRenderer(mRenderer);
        

        //timerHandler.postDelayed(timerRunnable, 1000);
	}

	@Override
    protected void onPause() {
        super.onPause();
        mCloudSurfaceView.onPause();
        //timerHandler.removeCallbacks(timerRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCloudSurfaceView.onResume();
        //timerHandler.postDelayed(timerRunnable, 1000);
    }
    
    public void launchSettingsFromDisplay(View view){
        Intent intent = new Intent(DisplayActivity.this, SettingsActivity.class);
        startActivity(intent);
    	
    }
}
