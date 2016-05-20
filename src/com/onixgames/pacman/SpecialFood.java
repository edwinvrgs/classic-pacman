package com.onixgames.pacman;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

public class SpecialFood extends Food {

	public SpecialFood(Context context, PointF posicion) {
		super(context, posicion);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		
		switch(++change % 2)
		{
			case 1:
				paint.setColor(Color.WHITE);
				break;
			case 0:
				paint.setColor(Color.LTGRAY);
				break;
		}
		
		RectF dst = getRect();
		
		canvas.drawCircle(dst.centerX(), dst.centerY(), Constants.GROW_Y/3.7f, paint);
	}
	
}
