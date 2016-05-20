package com.onixgames.pacman;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.view.View;

public class PuntajesView extends View {

	private ArrayList<Integer> puntajes;
	private ArrayList<String> nombres;
	
	private Point pantalla;
	private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public PuntajesView(Context context) {
		super(context);
		
		pantalla = Constants.getInstance().PANTALLA;
		
		SharedPreferences saved = getContext().getSharedPreferences("Puntajes", Context.MODE_PRIVATE);
		   
	    puntajes = new ArrayList<Integer>();
	    nombres = new ArrayList<String>();

	    for(int i = 0; i < ExtraActivity.PUNTAJES; i++)
	    {
	    	Integer puntos = (Integer) (saved.getInt("puntos"+i, 0));
	    	String nombre = saved.getString("nombres"+i, "nadie");
	    	
	    	if(puntos > 0)
	    	{	
	    		nombres.add(0, nombre);
	    		puntajes.add(0, puntos);
	    	}
	    }
	}

	@Override
	protected void onDraw(Canvas canvas) {
		
		super.onDraw(canvas);
		
		canvas.drawColor(Color.BLACK);
		
		paint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "pixelmix.ttf"));
		
		int dy = (int) pantalla.y / (ExtraActivity.PUNTAJES) - 3;
		int pos_x = pantalla.x / ExtraActivity.PUNTAJES;
		
		paint.setTextSize(30);
		paint.setColor(Color.WHITE);
		
		if(nombres.isEmpty())
		{
			canvas.drawText("LLEGASTE PRIMERO :'-)", 
					pos_x / 3, dy / 2, paint);	
			
			paint.setTextSize(22);
			paint.setColor(Color.GRAY);
			canvas.drawText("Intenta no morir muy rapido...", 
							pos_x / 3, dy * ExtraActivity.PUNTAJES - (dy / 3), paint);
		}
		else
		{
			int y = 1;
			
			paint.setTextSize(45);
			paint.setColor(Color.WHITE);
			
			for(String nombre : nombres)
				canvas.drawText(nombre, 
						pos_x / 3, (y++) * dy, paint);
			
			y = 1;
			
			paint.setColor(Color.GRAY);
			for(Integer puntos : puntajes)
				canvas.drawText(puntos.toString(), 
						pos_x * 6, (y++) * dy, paint);
			
		}
	}
}
