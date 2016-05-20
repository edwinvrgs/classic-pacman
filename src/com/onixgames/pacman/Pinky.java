package com.onixgames.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

public class Pinky extends Ghost {

	public Pinky(Context context, GameView laberinto, Bitmap imagen,
			PointF inicial, PointF regreso) {
		super(context, laberinto, imagen, inicial, regreso);

	}
	
	@Override
	protected void Update() {
		super.Update();
		
		if(GameView.pastillas_comidas > 6)
			can_move = true;
		
		if(!can_move)
			return;
		
		if(!ruta.isEmpty())
			pos_actual = ruta.poll();
	}
	
	@Override
	protected void CalcularRuta() {
		
		int i = random.nextInt();
		
		if(!ruta.isEmpty())
			return;
		
		if(modo == Ghost.MODO_CAZADOR) {
		
			NodoTablero nodo = null;
		
			RectF table[][] = laberinto.getLaberintoActual().getGrafico();
		
			PointF salida = new PointF();
			salida.set(pos_actual);
			
			PointF llegada = new PointF();
			llegada.set(laberinto.getFantasmas().get(Ghost.BLINKY).pos_actual);
			
			nodo = Busqueda.Profundidad(table, salida, llegada);
			ruta = nodo.getCamino();

			return;
		}
		
		/*if(modo == Ghost.MODO_ASUSTADO) {
			
			NodoTablero nodo = null;
			
			RectF table[][] = laberinto.getLaberintoActual().getGrafico();
		
			PointF salida = new PointF();
			salida.set(pos_actual);
			
			PointF llegada = new PointF();
			PointF aux = laberinto.getLaberintoActual().getSpecialFood().get(current_frame + i % 4);
			llegada.set(aux.x * Constants.GROW_X, aux.y * Constants.GROW_Y);
			
			nodo = Busqueda.Profundidad(table, salida, llegada);
			ruta = nodo.getCamino();

			return;
		}*/
	}
}
