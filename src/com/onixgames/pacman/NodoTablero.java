package com.onixgames.pacman;

import java.util.LinkedList;

import android.graphics.PointF;
import android.graphics.RectF;

public class NodoTablero {

	private RectF tablero[][];
	private LinkedList<PointF> camino;
	
	private float fila;
	private float columna;
	
	private PointF salida;
	private PointF llegada;
	
	private int costo = 0;
	
	public NodoTablero()
	{
		
	}
		
	public NodoTablero(RectF tablero[][], PointF salida, PointF llegada, LinkedList<PointF> camino) {

		this.camino = new LinkedList<PointF>();
		this.tablero = new RectF[Constants.SIZE_Y][Constants.SIZE_X];
		
		for(int i = 0; i < Constants.SIZE_Y; i++)
			for(int j = 0; j < Constants.SIZE_X; j++)
				this.tablero[i][j] = tablero[i][j];
		
		for(PointF point : camino)
			this.camino.add(point);
		
		this.salida = new PointF();
		this.salida.set(salida);
		
		//Cambio
		fila = salida.y;
		columna = salida.x;
		
		//camino.add(new Point(columna, fila));
		
		this.llegada = llegada;
	}
	
	public boolean Iguales(NodoTablero nodo) {
		
		if(fila == nodo.fila && columna == nodo.columna)
			return true;
		else		
			return false;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof NodoTablero)
			return Iguales((NodoTablero) o);
		else
			return false;
	}

	public NodoTablero(RectF tablero[][], float fila, float columna, PointF salida, PointF llegada, int costo, LinkedList<PointF> camino) {
		this(tablero, salida, llegada, camino);
		this.fila = fila;
		this.columna = columna;
		this.costo = costo;
	}
	
	public NodoTablero Clonar() {
		return new NodoTablero(tablero, fila, columna, salida, llegada, costo, camino);
	}
	
	private int getCosto() {
		return costo;
	}
	
	private float getHeuristica() {
		return (llegada.y - fila) + (llegada.x - columna);
	}
	
	public float getTotal() {
		return getCosto() + getHeuristica();
	}
	
	public boolean Mover(int direccion) {
		
		costo++;
		
		RectF rect = null;
		//RectF casilla = null;
		
		float dy, dx;
		
		float width = Constants.GROW_X;
		float height = Constants.GROW_Y;
			
		switch(direccion) {
		
		case Constants.ARRIBA:
		
			dy = fila - Constants.velocidad; 
			
			rect = new RectF(columna, dy, columna + width, dy + height);
			
			if(!Personaje.Block(tablero, rect)) {
				
				fila = dy;

				camino.add(new PointF(columna, fila));
				return true;
			}
			
			break;
			
		case Constants.ABAJO:
			
			dy = fila + Constants.velocidad; 
			
			rect = new RectF(columna, dy, columna + width, dy + height);
			
			if(!Personaje.Block(tablero, rect)) {
				
				fila = dy;
				
				camino.add(new PointF(columna, fila));
				return true;
			}
			
			break;
			
		case Constants.IZQUIERDA:
			
			dx = columna - Constants.velocidad; 
			
			rect = new RectF(dx, fila, dx + width, fila + height);
			
			if(!Personaje.Block(tablero, rect)) {
					
				columna = dx;
				
				camino.add(new PointF(columna, fila));
				return true;
			}
			
			break;
			
		case Constants.DERECHA:

			dx = columna + Constants.velocidad; 
			
			rect = new RectF(dx, fila, dx + width, fila + height);
			
			if(!Personaje.Block(tablero, rect)) {
					
				columna = dx;
				
				camino.add(new PointF(columna, fila));
				return true;
			}
			
			break;
		}
		
		return false;		
	}
	
	public boolean Finish() {
		
		return (fila == llegada.y) && (columna == llegada.x);
	}
	
	public RectF[][] getTablero() {
		
		return tablero;
	}
	
	public LinkedList<PointF> getCamino() {
		return camino;
	}
}
