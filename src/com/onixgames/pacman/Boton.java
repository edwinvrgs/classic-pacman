package com.onixgames.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.widget.ImageView;

public class Boton extends ImageView {

	protected Bitmap[] imagen;
	protected RectF lugar;
	protected Paint paint;
	
	Constants VALUES = Constants.getInstance();
	
	public Boton(Context context, Bitmap imagen1, Bitmap imagen2) {
		super(context);
		
		this.imagen = new Bitmap[2];
		this.imagen[0] = imagen1;
		this.imagen[1] = imagen2;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
	}
	
	protected void Dibujar(Canvas canvas, RectF lugar, Paint paint){
		
		this.lugar = lugar;
		this.paint = paint;
		
		this.draw(canvas);
	}
}