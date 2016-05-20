package com.onixgames.pacman;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

public class ExtraActivity extends Activity {

	public static final int PUNTAJES = 10;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		requestWindowFeature(Window.FEATURE_NO_TITLE);        
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    
	    Display display = getWindowManager().getDefaultDisplay();
	    Point screen_size = new Point();
	    
	    display.getSize(screen_size);
	    
	    String opcion = getIntent().getExtras().getString("opcion");
	    
	    if(opcion.equals("puntajes"))
	    	setContentView(new PuntajesView(this)); 
	    
	    if(opcion.equals("creditos"))
	    	setContentView(new CreditosView(this)); 
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
