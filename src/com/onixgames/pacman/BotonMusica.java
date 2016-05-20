package com.onixgames.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class BotonMusica extends Boton {

	private boolean isPlaying;
	
	public BotonMusica(Context context, Bitmap imagen1, Bitmap imagen2) {
		super(context, imagen1, imagen2);
		

		this.isPlaying = true;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
	
		super.onDraw(canvas);

		int i = 0;
		
		if(isPlaying)
			i = 1;
		else
			i = 0;
		
		canvas.drawBitmap(imagen[i], null, lugar, paint);
	}
	
	public boolean isPlaying() {
		return isPlaying;
	}
	
	public void setPlay(boolean play) {
		
		this.isPlaying = play;
		
		if(isPlaying) {
			VALUES.song.start();
		}
		else {
			VALUES.song.pause();
		}
	}

}
