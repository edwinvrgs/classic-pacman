package com.onixgames.pacman;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.graphics.Canvas;

@SuppressLint("WrongCall")
public class Loop extends Thread {

  /*  private Blinky blinky;
    private Inky inky;
    private Clyde clyde;
    private Pinky pinky;*/
    
	static final Integer FPS = 15;
	private Boolean running;
	private GameView view;
	//private static Loop instance = null;
	
	public Loop(GameView view, ArrayList<Ghost> fantasmas) {
		
		this.view = view;
		
	  	/*this.blinky = (Blinky) fantasmas.get(0);
    	this.inky = (Inky) fantasmas.get(1);
    	this.clyde = (Clyde) fantasmas.get(2);
    	this.pinky = (Pinky) fantasmas.get(3);*/
	}
	
	public void SetRunning(boolean run){
		this.running = run;
	}
	
	@Override
    public void run() {
		
		Long ticks_PS = (long) (1000 / FPS);
		Long start_time;
		Long sleep_time;
          
        while (running) {
        	
        	Canvas canvas = null;
        	start_time = System.currentTimeMillis();
        	
        	try {
        		canvas = view.getHolder().lockCanvas();
        		
        		synchronized (view.getHolder()) {
        			view.onDraw(canvas);
        		}
        	} finally {
	        	if (canvas != null) {
	        		view.getHolder().unlockCanvasAndPost(canvas);
	        	}
	        	
	            //Buscar();
				
        	}
        	
        	sleep_time = ticks_PS - (System.currentTimeMillis() - start_time);
        
        	try {
        		if (sleep_time > 0)
        			sleep(sleep_time);
        		else
        			sleep(10);
        	} 
        	catch (Exception e) {}
        }
    }
	
	/*private void Buscar() {
		
		blinky.CalcularRuta();
		inky.CalcularRuta();
		clyde.CalcularRuta();
		pinky.CalcularRuta();
	}*/

	/*public static Loop getInstance(GameView game_view) {
		
		if(instance == null)
			instance = new Loop(game_view);
		
		return instance;
	}*/
	
}
