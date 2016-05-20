package com.onixgames.pacman;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

public final class Constants {
	
	public Point PANTALLA;
	
	public static final int dt = 190;
	
	public static int GROW_X;
	public static int GROW_Y;
	
	public static final int SIZE_X = 15;
	public static final int SIZE_Y = 21;
	
	public static float velocidad;
	
	/*
	public static final Point IZQUIERDA = new Point(-1, 0);
	public static final Point ARRIBA = new Point(0, -1);
	public static final Point DERECHA = new Point(1, 0);
	public static final Point ABAJO = new Point(0, 1);
	*/
	
	public static final int IZQUIERDA = 0;
	public static final int ARRIBA = 1;
	public static final int DERECHA = 2;
	public static final int ABAJO = 3;
	
	public Bitmap ms_pacman;
	public Bitmap pacman;
	
	public Bitmap play;
	public Bitmap pause;
	
	public ArrayList<char[][]> mapas;	
	public static final int hall = 4;
	public static final int door = 5;
	
	public ArrayList<Bitmap> ghosts;
	public Bitmap dead_ghost;
	public Bitmap eyes;
	public Bitmap food;
	public Bitmap fruits;	
	public Bitmap lose;	
	public ArrayList<Bitmap> objetos;
	public Bitmap credits;
	
	public SoundPool sound;
	public int sirena;
	public int death;
	public int eating;
	public int eating_fruit;
	public int eating_ghosts;
	public int eating_original;
	public int extra_lives;
	public MediaPlayer opening_song;
	public MediaPlayer song;
	
	public static Constants instance = null;
	
	public Constants(Resources resources, Context context, Point pantalla) throws IOException {
		
		PANTALLA = pantalla;
	
		GROW_X = pantalla.x / SIZE_X;
		GROW_Y = GROW_X;
		
		velocidad = Constants.GROW_X / 4f;

		CargarMapas(resources);
		CargarImagenes(resources);
		CargarSonidos(resources, context);
	}

	private void CargarImagenes(Resources resources) {
		
		ms_pacman = BitmapFactory.decodeResource(resources, R.drawable.ms_pacman);
		pacman = BitmapFactory.decodeResource(resources, R.drawable.pacman);
		
		ghosts = new ArrayList<Bitmap>();
		
		ghosts.add(BitmapFactory.decodeResource(resources, R.drawable.red_ghost));
		ghosts.add(BitmapFactory.decodeResource(resources, R.drawable.pink_ghost));
		ghosts.add(BitmapFactory.decodeResource(resources, R.drawable.blue_ghost));
		ghosts.add(BitmapFactory.decodeResource(resources, R.drawable.orange_ghost));
		
		dead_ghost = BitmapFactory.decodeResource(resources, R.drawable.easy_ghost);
		eyes = BitmapFactory.decodeResource(resources, R.drawable.eyes);
		
		food = BitmapFactory.decodeResource(resources, R.drawable.food);
		fruits = BitmapFactory.decodeResource(resources, R.drawable.fruits);
		lose = BitmapFactory.decodeResource(resources, R.drawable.lose);

		play = BitmapFactory.decodeResource(resources, R.drawable.media_play);
		pause = BitmapFactory.decodeResource(resources, R.drawable.media_pause);
		
		credits = BitmapFactory.decodeResource(resources, R.drawable.credits); 
	}
	
	private void CargarSonidos(Resources resources, Context context) {
		
		sound = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		
		death = sound.load(context, R.raw.muerte, 0);
		eating = sound.load(context, R.raw.eating, 0);
		eating_fruit = sound.load(context, R.raw.eating_fruit, 0);
		eating_ghosts = sound.load(context, R.raw.eating_ghosts, 0);
		extra_lives = sound.load(context, R.raw.extra_lives, 0);
		
		opening_song = MediaPlayer.create(context, R.raw.opening);
		song = MediaPlayer.create(context, R.raw.song);
	}
	
	public void ReproducirSonido(int id, int repeat) {
		sound.play(id, 0.7f, 0.7f, 0, repeat, 1);
	}
	
	private void CargarMapas(Resources resources) {

		mapas = new ArrayList<char[][]>();
		
		mapas.add(LeerMapa(resources.openRawResource(R.raw.uno)));
		mapas.add(LeerMapa(resources.openRawResource(R.raw.dos)));
		mapas.add(LeerMapa(resources.openRawResource(R.raw.tres)));
		mapas.add(LeerMapa(resources.openRawResource(R.raw.cuatro)));
		
		objetos = new ArrayList<Bitmap>();
		
		objetos.add(BitmapFactory.decodeResource(resources, R.drawable.wall_1));
		objetos.add(BitmapFactory.decodeResource(resources, R.drawable.wall_2));
		objetos.add(BitmapFactory.decodeResource(resources, R.drawable.wall_3));
		objetos.add(BitmapFactory.decodeResource(resources, R.drawable.wall_4));
		objetos.add(BitmapFactory.decodeResource(resources, R.drawable.hall));
		objetos.add(BitmapFactory.decodeResource(resources, R.drawable.door));
	}
	
	
	private char[][] LeerMapa(InputStream stream) {
		
		char[][] mapa = new char[SIZE_Y][SIZE_X];
		String aux = "";
		
		try {
			BufferedReader buffer = new BufferedReader(new InputStreamReader(stream));		
			
			int i = 0;
		
			while(i != SIZE_Y && (aux = buffer.readLine()) != null) {
				mapa[i++] = aux.toCharArray();
			}
			
			buffer.close();
			
		} catch (IOException e) {
			Log.e("Archivo: ", "error en la apertura del archivo");
			e.printStackTrace();
		}
		
		return mapa;
	}
	
	public static Constants getInstance(Resources resources, Context context, Point pantalla) throws IOException{
		
		if(instance == null)
			instance = new Constants(resources, context, pantalla);
		
		return instance;
	}
	
	public static Constants getInstance() {

		return instance;
	}
}
