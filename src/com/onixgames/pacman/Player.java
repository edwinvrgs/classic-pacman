package com.onixgames.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

public class Player extends Personaje {

	private int current_frame;
	private int direccion_preferida;
	private int comprobar; //0 = false. 1 = true
	
	public Player(Context context, GameView laberinto, Bitmap imagen, PointF inicial) {
		super(context, laberinto, imagen, inicial);
		
		current_frame = 0;
		direccion = 1;
		comprobar = 0;
		check = false;
		direccion_preferida = direccion;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		//Update();
		Dibujar(canvas);
	}

	@Override
	protected void Dibujar(Canvas canvas) {
		
		int srcX = getAncho() * current_frame;
		int srcY = getAlto();
		
		Rect src = new Rect(srcX, 0, (srcX) + getAlto(), srcY);
		RectF dst = getRect();
        		
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		
		canvas.save();
		
		if(direccion != -1)
			canvas.rotate(90 * (direccion), dst.centerX(), dst.centerY());
		
		if(imagen != null)
			canvas.drawBitmap(imagen, src, dst, paint);
		
		canvas.restore();
	}

	@Override
	protected void Update() {
		
		current_frame = ++current_frame % 3;
		
		if(!EstaParado())
			Mover();
	}	

	protected PointF getWay(int direccion) {

		if(direccion == Constants.DERECHA)
			return getMove(new Point(1, 0), pos_actual);
		
		if(direccion == Constants.IZQUIERDA)
			return getMove(new Point(-1, 0), pos_actual);
		
		if(direccion == Constants.ARRIBA)
			return getMove(new Point(0, -1), pos_actual);
		
		if(direccion == Constants.ABAJO)
			return getMove(new Point(0, 1), pos_actual);
		
		return null;
	}

	@Override
	protected void Mover() {
				
		PointF aux = null;
		//PointF aux_pref = null;

		//Parte del movimiento move
		aux = getWay(direccion);
		float x = aux.x;
		float y = aux.y;
		RectF move = new RectF(x, y, x + Constants.GROW_X, y + Constants.GROW_Y);
		
		/*aux_pref = getWay(direccion_preferida);
		float x_pref = aux_pref.x;
		float y_pref = aux_pref.y;
		RectF move_pref = new RectF(x_pref, y_pref, x_pref + Constants.GROW_X, y_pref + Constants.GROW_Y);*/
		
		RectF grafico[][] = this.laberinto.getLaberintoActual().getGrafico();		
		//Parte de la casilla DOOR
		char logico[][] = this.laberinto.getLaberintoActual().getLogico();

		int fila = (int) (y / Constants.GROW_Y);
		int columna = (int) (x / Constants.GROW_X);
		
		char casilla = logico[fila][columna];
		
		/*if(check)
			if(casilla != Laberinto.DOOR && !Block(grafico, move_pref)) {
				check = false;
				pos_actual.set(aux_pref);
				
				setDireccion(direccion_preferida);
				return;
			}*/
		
		if(casilla != Laberinto.DOOR && !Block(grafico, move)) //{
			pos_actual.set(aux);
		/*} else if (Block(grafico, move)) {
			check = true;
			direccion_preferida = last_direccion;
		}*/
			
	}
	
	public Bitmap getImagen() {
		return imagen;
	}
	
	public Integer getDireccion() {
		return direccion;
	}

	@Override
	public int getAncho() {
		return super.getAncho() / 3;
	}

	@Override
	public int getAlto() {
		return super.getAlto();
	}
}
