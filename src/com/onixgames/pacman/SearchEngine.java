package com.onixgames.pacman;

import java.util.ArrayList;

import android.util.Log;

public class SearchEngine extends Thread {

    private Blinky blinky;
    private Inky inky;
    private Clyde clyde;
    private Pinky pinky;
    
    private volatile boolean running;
    
    class Mutex {
    	
    }
    
    public SearchEngine(ArrayList<Ghost> fantasmas) {
    	
    	this.blinky = (Blinky) fantasmas.get(0);
    	this.inky = (Inky) fantasmas.get(1);
    	this.clyde = (Clyde) fantasmas.get(2);
    	this.pinky = (Pinky) fantasmas.get(3);
    	
    	running = false;
    }
	
	@Override
	public void run() {
		
		while(isRunning()){
			synchronized (new Mutex()) {
				try {
					Buscar();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}	
		}
	}

	private void Buscar() throws InterruptedException {
			
		blinky.CalcularRuta();
		inky.CalcularRuta();
		clyde.CalcularRuta();
		pinky.CalcularRuta();
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
	
}
