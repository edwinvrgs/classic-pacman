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
		int tamaño = font_size + 5;
		
		paint.setTextSize(font_size);
		paint.setColor(Color.YELLOW);
		canvas.drawText("Classic Pacman", 0, tamaño, paint);
		
		paint.setTextSize(font_size - 15);
		paint.setColor(Color.WHITE);
		canvas.drawText("a clone", tamaño / 2, tamaño * 2, paint);
		
		paint.setTextSize(font_size - 10);
		paint.setColor(Color.WHITE);
		canvas.drawText("Programado por:", 0, tamaño * 4, paint);
		
		paint.setTextSize(font_size);
		paint.setColor(Color.BLUE);
		canvas.drawText("Edwin Vargas", tamaño / 2, tamaño * 5, paint);
		paint.setTextSize(font_size);
		paint.setColor(Color.BLUE);
		canvas.drawText("Geiver Botello", tamaño / 2, tamaño * 6, paint);

		paint.setTextSize(font_size - 10);
		paint.setColor(Color.WHITE);
		canvas.drawText("Sprites:", 0, tamaño * 8, paint);
		
		paint.setTextSize(font_size - 2);
		paint.setColor(Color.GRAY);
		canvas.drawText("JDASTER64", tamaño / 2, tamaño * 9, paint);

		paint.setTextSize(font_size - 10);
		paint.setColor(Color.WHITE);
		canvas.drawText("Sountrack:", 0, tamaño * 11, paint);
		
		paint.setTextSize(font_size - 10);
		paint.setColor(Color.GRAY);
		canvas.drawText("es.melodyloops.com", tamaño / 2, tamaño * 12, paint);
		
		/*RectF dst = new RectF(0, 0, pantalla.x / 2, pantalla.y / 4);
		canvas.drawBitmap(VALUES.credits, null, dst, paint);*/
	}

}
