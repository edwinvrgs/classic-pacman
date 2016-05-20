package com.onixgames.pacman;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.widget.ImageView;

public abstract class Personaje extends ImageView {

	protected Bitmap imagen;
	
	protected int direccion;
	protected int last_direccion;
	
	protected float velocidad;
	
	protected PointF pos_inicial;
	protected PointF pos_actual;
	
	protected boolean can_move;
	
	protected GameView laberinto;
	protected Constants VALUES;
	
	protected boolean check;
	
	public Personaje(Context context, GameView laberinto, Bitmap imagen, PointF inicial) {
		super(context);
		
		VALUES = Constants.getInstance();
	
		pos_inicial = new PointF(inicial.x * Constants.GROW_X, inicial.y * Constants.GROW_Y);
		
		pos_actual = new PointF();
		pos_actual.set(pos_inicial);
		
		this.imagen = imagen;
		this.laberinto = laberinto;		
		
		this.direccion = 1;
		
		velocidad = Constants.velocidad;
		
		check = false;
	}

	protected abstract void Dibujar(Canvas canvas);
	protected abstract void Update();	
	
	protected void Mover() {
		
		PointF aux = null;
		
		if(!check) {
			Random rnd = new Random();
			int direccion = (int) (rnd.nextDouble() * 4);
			setDireccion(direccion);
		}

		aux = getWay(this.direccion);
		
		if(aux != null) {
			float x = aux.x;
			float y = aux.y;
			
			RectF grafico[][] = this.laberinto.getLaberintoActual().getGrafico();
			RectF rect = new RectF(x, y, x + Constants.GROW_X, y + Constants.GROW_Y);
			
			if(!Block(grafico, rect)) {
				
				pos_actual.set(aux);
				check = true;
			}
			else
				check = false;
		}
	}

	protected abstract PointF getWay(int direccion);
	
	protected static boolean Block(RectF[][] table, RectF move) {
		
		if(move == null)
			return false;
		
		for(int i = 0; i < Constants.SIZE_Y; i++)
			for(int j = 0; j < Constants.SIZE_X; j++)
			{
				RectF casilla = table[i][j];
				
				if(casilla == null)
					continue;
				
				if(move.intersect(casilla))
					return true;
			}
		
		return false;
	}
	
	protected PointF getMove(Point aux, PointF pos) {
		
		float dx = (float) aux.x * velocidad + pos.x; 
		float dy = (float) aux.y * velocidad + pos.y; 	
		
		return new PointF(dx, dy);
	}
	
	public RectF getRect() {
		
		float x = pos_actual.x;
		float y = pos_actual.y;
		
		float width =  x + (Constants.GROW_X);
		float height = y + (Constants.GROW_Y);
		
		return new RectF(x + 3.5f, y + 3.5f, width - 3.5f, height - 3.5f);
	}
	
	public void setDireccion(int direccion) {
		
		this.last_direccion = this.direccion;
		this.direccion = direccion;
	}
	
	public void Reset() {
		pos_actual.set(pos_inicial);
	}
	
	public Bitmap getImagen() {
		return imagen;
	}
	
	public void setImagen(Bitmap imagen) {
		this.imagen = imagen;
	}
	
	public int getAncho() {
		return imagen.getWidth();
	}
	
	public int getAlto() {
		return imagen.getHeight();
	}

	public boolean EstaParado() {
		return can_move;
	}

	public void Parado(boolean can_move) {
		this.can_move = can_move;
	}
}
