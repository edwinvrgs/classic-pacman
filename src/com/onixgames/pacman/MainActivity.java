package com.onixgames.pacman;

import java.io.IOException;

import com.onixgames.pacman.R;
import com.onixgames.pacman.EditNameDialog.EditNameDialogListener;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.graphics.*;

public class MainActivity extends FragmentActivity implements EditNameDialogListener {

	Constants VALUES;
	
	ImageButton boton_inicio;
	ImageButton boton_puntajes;
	ImageButton boton_creditos;
	
	Intent to_game;
	Intent to_points;
	Intent to_credits;
	
	final int opciones = 0;
	final int opcion_musica = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		requestWindowFeature(Window.FEATURE_NO_TITLE);        
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    
		setContentView(R.layout.activity_main);    
		
		Display display = getWindowManager().getDefaultDisplay();
		Point screen_size = new Point();
		    
		display.getSize(screen_size);
		
		try {
			VALUES = Constants.getInstance(getResources(), this, screen_size);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Init();
		AddEvents();
		AddMusic();
	}

	@Override
	protected void onRestart() {
		
		super.onRestart();
		VALUES.opening_song.start();
	}
	
	private void Init() {
		
		boton_inicio = (ImageButton) findViewById(R.id.boton_inicio);
		boton_puntajes = (ImageButton) findViewById(R.id.boton_puntajes);	
		boton_creditos = (ImageButton) findViewById(R.id.boton_creditos);
	}
	
	private void AddMusic() {
		
		VALUES.opening_song.setLooping(true);
		VALUES.opening_song.setVolume(70,70);
		VALUES.opening_song.start();
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
		VALUES.opening_song.start();
	}

	@Override
	protected void onPause() {
		
		super.onPause();
		VALUES.opening_song.pause();
	}

	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		VALUES.opening_song.stop();
	}

	private void AddEvents() {
		
		OnClickListener listener_inicio = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				to_game = new Intent(getApplicationContext(), GameActivity.class);
				
		        showEditDialog();
			}
		};
		
		OnClickListener listener_puntajes = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				to_points = new Intent(getApplicationContext(), ExtraActivity.class);
				to_points.putExtra("opcion", "puntajes");
				
				startActivity(to_points);
			}
		};
		
		OnClickListener listener_creditos = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				to_credits = new Intent(getApplicationContext(), ExtraActivity.class);
				to_credits.putExtra("opcion", "creditos");
				
				startActivity(to_credits);
			}
		};
		
		boton_inicio.setOnClickListener(listener_inicio);
		boton_puntajes.setOnClickListener(listener_puntajes);
		boton_creditos.setOnClickListener(listener_creditos);
	}
	
	private void showEditDialog() {
		
        android.app.FragmentManager fm = getFragmentManager();
        EditNameDialog editNameDialog = new EditNameDialog();
        
       	editNameDialog.show(fm, "dialog");
	}

    public void onFinishEditDialog(String inputText) {
    	
    	//VALUES.opening_song.pause();
    	
    	to_game.putExtra("Jugador", inputText);
    	startActivity(to_game);
    }  
    
    /*public boolean onTouchEvent(MotionEvent event) {
				
		switch(event.getAction()) {
		
			case MotionEvent.ACTION_DOWN:
				
				float x1 = event.getX(); 
				float y1 = event.getY();
				
				RectF rect = new RectF(VALUES.PANTALLA.x / 2, VALUES.PANTALLA.y / 2, 0, 0);
								
				if(rect.contains(x1, y1))
					
				
				break;  
		}
		
		return super.onTouchEvent(event);
    }*/
}