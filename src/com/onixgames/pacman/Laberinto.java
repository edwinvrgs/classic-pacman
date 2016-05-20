package com.onixgames.pacman;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.widget.ImageView;

public class Laberinto extends ImageView {

	public static final char DOOR = '+';
	public static final char WALL = '#';
	public static final char HALL = '-';
	public static final char SPECIAL_FOOD = '*';
	public static final char PACMAN = 'o';
	public static final char GHOST = 'x';
	public static final char EMPTY = '.';
	public static final char RETURN = '_';
	
	private int nivel;
	private char[][] logico;
	private RectF[][] grafico;
	private ArrayList<PointF> special_food;
	private ArrayList<PointF> food;
	private ArrayList<PointF> pos_fantasmas;
	private PointF pos_regreso;
	private PointF pos_pacman;
	private Constants VALUES;
	
	public Laberinto(Context context, char[][] laberinto, int nivel)
	{
		super(context);
		
		this.nivel = nivel;
		
		VALUES = Constants.getInstance(); 
		logico = laberinto;
		
		pos_fantasmas = new ArrayList<PointF>();
		special_food = new ArrayList<PointF>();
		
		grafico = new RectF[Constants.SIZE_Y][Constants.SIZE_X];
		
		food = new ArrayList<PointF>();
		
		for(int i = 0; i < Constants.SIZE_Y; i++)
			for(int j = 0; j < Constants.SIZE_X; j++)
			{
				float x = j * Constants.GROW_X;
				float y = i * Constants.GROW_Y;

				grafico[i][j] = null;

				switch (logico[i][j]) {
				
				case Laberinto.WALL:
					grafico[i][j] = new RectF(x, y, x + Constants.GROW_X, y + Constants.GROW_Y);
					
					break;
					
				case Laberinto.HALL:
					food.add(new PointF(j, i));
					
					break;

				case  Laberinto.SPECIAL_FOOD:
					special_food.add(new PointF(j, i));
					
					break;

				case Laberinto.GHOST:
					pos_fantasmas.add(new PointF(j, i));
					
					break;
					
				case Laberinto.RETURN:					
					pos_fantasmas.add(new PointF(j, i));
					pos_regreso = new PointF(j, i);
					
					break;
					
				case Laberinto.PACMAN:
					pos_pacman = new PointF(j, i);
					
					break;
				}
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		Paint paint = new Paint();
		paint.setAntiAlias(true);
				
		//Pintar el Laberinto
		for(int i = 0; i < Constants.SIZE_Y; i++)
			for(int j = 0; j < Constants.SIZE_X; j++)
			{
				int x = j * Constants.GROW_X;
				int y = i * Constants.GROW_Y;
				
		        RectF dst = new RectF(x, y, x + Constants.GROW_X, y + Constants.GROW_Y);
				
		        switch (getLogico()[i][j]) {

				case Laberinto.WALL:
					canvas.drawBitmap(VALUES.objetos.get(nivel), null, dst, paint);
					
					break;

				case Laberinto.HALL:
					canvas.drawBitmap(VALUES.objetos.get(Constants.hall), null, dst, paint);
					
					break;

				case Laberinto.DOOR:
					canvas.drawBitmap(VALUES.objetos.get(Constants.door), null, dst, paint);
					
					break;
				}
			}
	}



	public RectF[][] getGrafico() {
		return grafico;
	}

	public ArrayList<PointF> getFood() {
		return food;
	}
	
	public ArrayList<PointF> getSpecialFood() {
		return special_food;
	}
	
	public ArrayList<PointF> getPosFantasmas() {
		return pos_fantasmas;
	}

	public PointF getPosPacman() {
		return pos_pacman;
	}

	public char[][] getLogico() {
		return logico;
	}
	
	public RectF getRect(int x, int y) {
		return grafico[y][x];
	}

	public PointF getPosRegreso() {
		return pos_regreso;
	}
	
}
