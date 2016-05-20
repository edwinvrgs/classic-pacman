package com.onixgames.pacman;

import java.util.ArrayList;
import java.util.LinkedList;

import android.graphics.PointF;
import android.graphics.RectF;

public class Busqueda {
	
	//Hola

	public static NodoTablero AStar(RectF tablero[][], PointF salida, PointF llegada) {

		NodoTablero actual = new NodoTablero(tablero, salida, llegada, new LinkedList<PointF>());
		
		ArrayList<NodoTablero> abiertos = new ArrayList<NodoTablero>();
		ArrayList<NodoTablero> cerrados = new ArrayList<NodoTablero>();
		
		abiertos.add(actual);
		
		while(abiertos.size() > 0 && !actual.Finish())
		{
			actual = abiertos.get(0);
			
			if(!actual.Finish())
			{
				abiertos.remove(0);
				cerrados.add(actual);
				
				for(int i = 0; i < 4; i++)
				{
					NodoTablero aux = actual.Clonar();
					
					if(aux.Mover(i));
						Insertar(abiertos, cerrados, aux);
				}
			}
		}
		
		if(abiertos.size() == 0)
			return null;
		else
			return actual;
	}
	
	public static NodoTablero Profundidad(RectF tablero[][], PointF salida, PointF llegada) {
		
		NodoTablero actual = new NodoTablero(tablero, salida, llegada, new LinkedList<PointF>());
		
		ArrayList<NodoTablero> abiertos = new ArrayList<NodoTablero>();
		ArrayList<NodoTablero> cerrados = new ArrayList<NodoTablero>();
		
		abiertos.add(actual);
		
		while(abiertos.size() > 0 && !actual.Finish())
		{
			actual = abiertos.get(0);
			
			if(!actual.Finish())
			{
				abiertos.remove(0);
				cerrados.add(actual);
				
				for(int i = 0; i < 4; i++)
				{
					NodoTablero aux = actual.Clonar();
					
					if(aux.Mover(i));
						if(!abiertos.contains(aux) && !cerrados.contains(aux))
							abiertos.add(0, aux);
				
				}
			}
		}
		
		if(abiertos.size() == 0)
			return null;
		else
			return actual;
	}
	
	private static void Insertar(ArrayList<NodoTablero> abiertos, ArrayList<NodoTablero> cerrados, NodoTablero nuevo) {
		
		boolean insertar = true;
		NodoTablero aux;
		
		for(int i = 0; i < abiertos.size(); i++)
		{
			aux = abiertos.get(i);
			
			if(aux.Iguales(nuevo))
			{
				if(aux.getTotal() > nuevo.getTotal())
					abiertos.remove(i);
				else
					insertar = false;
			}
		}	
		
		for(int j = 0; j < cerrados.size(); j++)
		{
			aux = cerrados.get(j);
			
			if(aux.Iguales(nuevo))
			{
				if(aux.getTotal() > nuevo.getTotal())
					cerrados.remove(j);
				else
					insertar = false;
			}
		}
		
		if(insertar)
		{
			boolean bien = false;
			
			for(int i = 0; i < abiertos.size() && !bien; i++)
			{
				aux = abiertos.get(i);
				
				if(aux.getTotal() > nuevo.getTotal())
				{
					abiertos.add(i, nuevo);
					bien = true;
				}
			}
		
			if(!bien)
				abiertos.add(nuevo);
		}
	}
	
}
