package com.onixgames.pacman;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.View;

public class CreditosView extends View {

	private Point pantalla;
	private Constants VALUES;
	private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public CreditosView(Context context) {
		super(context);
		
		pantalla = Constants.getInstance().PANTALLA;
		VALUES = Constants.getInstance();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		
		super.onDraw(canvas);
		
		canvas.drawColor(Color.BLACK);
		
		paint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "pixelmix.ttf"));
		
		int font_size =  VALUES.PANTALLA.y / 20;
		int tama�o = font_size + 5;
		
		paint.setTextSize(font_size);
		paint.setColor(Color.YELLOW);
		canvas.drawText("Classic Pacman", 0, tama�o, paint);
		
		paint.setTextSize(font_size - 15);
		paint.setColor(Color.WHITE);
		canvas.drawText("a clone", tama�o / 2, tama�o * 2, paint);
		
		paint.setTextSize(font_size - 10);
		paint.setColor(Color.WHITE);
		canvas.drawText("Programado por:", 0, tama�o * 4, paint);
		
		paint.setTextSize(font_size);
		paint.setColor(Color.BLUE);
		canvas.drawText("Edwin Vargas", tama�o / 2, tama�o * 5, paint);
		paint.setTextSize(font_size);
		paint.setColor(Color.BLUE);
		canvas.drawText("Geiver Botello", tama�o / 2, tama�o * 6, paint);

		paint.setTextSize(font_size - 10);
		paint.setColor(Color.WHITE);
		canvas.drawText("Sprites:", 0, tama�o * 8, paint);
		
		paint.setTextSize(font_size - 2);
		paint.setColor(Color.GRAY);
		canvas.drawText("JDASTER64", tama�o / 2, tama�o * 9, paint);

		paint.setTextSize(font_size - 10);
		paint.setColor(Color.WHITE);
		canvas.drawText("Sountrack:", 0, tama�o * 11, paint);
		
		paint.setTextSize(font_size - 10);
		paint.setColor(Color.GRAY);
		canvas.drawText("es.melodyloops.com", tama�o / 2, tama�o * 12, paint);
		
		/*RectF dst = new RectF(0, 0, pantalla.x / 2, pantalla.y / 4);
		canvas.drawBitmap(VALUES.credits, null, dst, paint);*/
	}

}
