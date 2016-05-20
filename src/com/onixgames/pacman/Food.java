package com.onixgames.pacman;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.widget.ImageView;

public class Food extends ImageView {

	protected PointF posicion;
	protected Constants VALUES;
	protected int width;
	protected int heigth;
	protected int change;
	
	public Food(Context context, PointF posicion) {
		super(context);
		
		change = 0;
		
		this.VALUES = Constants.getInstance(); 
		
		width = VALUES.food.getWidth();
		heigth = VALUES.food.getHeight();
		
		this.posicion = new PointF(posicion.x * Constants.GROW_X, posicion.y * Constants.GROW_Y);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.YELLOW);
		
		RectF dst = getRect();
		
		canvas.drawBitmap(VALUES.food, null, dst, paint);
	}
	
	public RectF getRect()
	{
		float x = posicion.x;
		float y = posicion.y;
		
		float width = x + Constants.GROW_X;
		float height = y + Constants.GROW_Y;
		
		return new RectF(x + 5, y + 5, width - 5, height - 5);
	}
}