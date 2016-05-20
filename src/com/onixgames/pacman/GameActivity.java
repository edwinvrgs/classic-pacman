package com.onixgames.pacman;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class GameActivity extends FragmentActivity implements SensorEventListener{

	private Constants VALUES;
	
	private boolean created;
	
	private float x1, y1;
	private float x2, y2;
	private int MIN_DISTANCE = 90;
	
	private float prevX = 0f, prevY = 0f;
	private float  curX = 0, curY = 0;
	private long last_time, actual_time;
	
	private BotonMusica boton_musica;
	
	GameView game_view;	
	Display display;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		requestWindowFeature(Window.FEATURE_NO_TITLE);        
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    
	    VALUES = Constants.getInstance();
	    
		Init();
		AddMusic();
	}
	
	@SuppressWarnings("deprecation")
	private void Init() {

		created = false;
		
		Point screen_size = new Point();
		
		display = getWindowManager().getDefaultDisplay();
		
		try {
			display.getSize(screen_size);
		} catch (java.lang.NoSuchMethodError ignore) { // Older device
			screen_size.x = display.getWidth();
			screen_size.y = display.getHeight();
		}
		
		String jugador = getIntent().getExtras().getString("Jugador");
		String[] aux = jugador.split(":");
		
		game_view = new GameView(this, getApplicationContext(), aux, display); 
    	setContentView(game_view);
    	
    	created = true;
    	
    	boton_musica = game_view.getBotonMusica();
    	
    	VALUES.song.start();
	}
	
	private void AddMusic() {
    	VALUES.song.setLooping(true);
    	VALUES.song.setVolume(80, 80);
	}

	protected void onPause() { 
		super.onPause();
		
		if(created)
			game_view.Pausa();
		
		VALUES.song.pause();
	}
	
	protected void onRestart() {
		super.onRestart();
		
		if(boton_musica.isPlaying())
			VALUES.song.start();
	}
  	
	@Override
	protected void onResume() {
	    super.onResume();
	    
		if(created)
			game_view.Reanudar();
	    
	    SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
	    List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_ACCELEROMETER); 
	    
	    if (sensors.size() > 0)
	        sm.registerListener(this, sensors.get(0), SensorManager.SENSOR_DELAY_GAME);

		if(boton_musica.isPlaying())
			VALUES.song.start();
	}

	@Override
	protected void onStop() {
		super.onStop();
		
		if(created)
			game_view.Chao();
		
		SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);        
	    sm.unregisterListener(this);
	    
		VALUES.song.pause();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		if(!created)
			return super.onTouchEvent(event); 
		
		switch(event.getAction()) {
		
			case MotionEvent.ACTION_DOWN:
				
				x1 = event.getX(); 
				y1 = event.getY();
								
				if(boton_musica.lugar.contains(x1, y1))
					boton_musica.setPlay(!boton_musica.isPlaying());
				
				break;         
			
			case MotionEvent.ACTION_UP:
				
				x2 = event.getX();
				y2 = event.getY();
				
				Float deltaX = Math.abs(x2 - x1);
				Float deltaY = Math.abs(y2 - y1);
				
				int direccion = -1;
				
				if (deltaX > MIN_DISTANCE && deltaX > deltaY)
				{
					if (x2 > x1)
						direccion = Constants.DERECHA;
					else
						direccion = Constants.IZQUIERDA;
				}       
				if (deltaY > MIN_DISTANCE && deltaX < deltaY)
				{
					if (y2 > y1)
						direccion = Constants.ABAJO;  
					else 
						direccion = Constants.ARRIBA; 
				} 
				
				if(direccion != -1)
					game_view.getPlayer().setDireccion(direccion);
				
				break;    
		} 

		return super.onTouchEvent(event);      
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
	
		if(!created)
			return;
		
		curX = event.values[0];
		curY = event.values[1];
		
		actual_time = (long) (event.timestamp / 1*10E9); //en segundos
		
		if(actual_time - last_time > 0.1)
			if(Math.abs(curX - prevX) > 1*10E-2 || Math.abs(curY - prevY) > 1*10E-2)
			{
				if(Math.abs(curX) > Math.abs(curY))
				{
					if(curX > 0)
						game_view.getPlayer().setDireccion(Constants.IZQUIERDA);
					else
						game_view.getPlayer().setDireccion(Constants.DERECHA); 
				} 
				else 
				{
					if (curY > 0)
						game_view.getPlayer().setDireccion(Constants.ABAJO);  
					else 
						game_view.getPlayer().setDireccion(Constants.ARRIBA); 
				} 
			}
		
		prevX = curX;
		prevY = curY;
		
		last_time = actual_time;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}
}

