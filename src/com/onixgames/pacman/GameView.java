package com.onixgames.pacman;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.test.PerformanceTestCase;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.GestureDetector.OnDoubleTapListener;
import android.widget.ImageView;
import android.widget.Toast;

public class GameView extends SurfaceView {

	Activity padre;
	
	//Parte grafica
	private SurfaceHolder holder;
	private Loop loop;
	
	//Constantes
	private Constants VALUES;
	
	//Parte logico-grafica (mas logica que grafica)
	private Laberinto laberinto_actual;
	private ArrayList<Ghost> fantasmas;
	private ArrayList<Food> food;
	private Player player;
	private SearchEngine search;
	
	//Parte meramente logica
	private int time;
	private int nivel;
	private int vidas;
	private int puntos;
	private int fantasmas_comidos;
	private boolean juego_finalizado;
	private boolean victoria = false;
	
	//Algo X
	private BotonMusica boton_musica;
	
	public static int pastillas_comidas = 0;
	
	//Misc
	private String nombre_jugador;
	
	public GameView(Activity activity, Context context, String[] datos, Display display) {
		super(context);

		this.padre = activity;
		
		this.VALUES = Constants.getInstance(); 
		this.nombre_jugador = datos[0];
		this.puntos = 0;
		
		this.juego_finalizado = false;
	
		//Pintar boton de musica
		boton_musica = new BotonMusica(getContext(), VALUES.play, VALUES.pause);

		//Nivel y vidas
		if(datos.length > 2) {
			int nivel = Integer.parseInt(datos[1]);
			int vidas = Integer.parseInt(datos[2]);

			if(nivel <= 3 && vidas <= 5)
				Init(nivel, vidas);
			else
				Init(0, 4);
		}
		else
			Init(0, 4);
		
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			public void uncaughtException(Thread thread, Throwable ex) {
				Log.i("HOLA", "ESTO ES UNA EXCEPCION");
				Reanudar();
				}
			}
		);
		
		if(juego_finalizado) {
			DibujarFinal(canvas);
			return;
		}
		
		Update();
		Dibujar(canvas);
	}
	
	private void DibujarFinal(Canvas canvas) {
		
		canvas.drawColor(Color.BLACK);
		
		time++;

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		
		//Establecer la configuracion del paint para el texto
		paint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "pixelmix.ttf"));
		
		int font_size =  VALUES.PANTALLA.y / 18;
		int tamaño = font_size + 5;
		
		int dy = (int) VALUES.PANTALLA.y / (ExtraActivity.PUNTAJES) - 3;
		int pos_x = VALUES.PANTALLA.x / ExtraActivity.PUNTAJES;
		
		if(victoria) {
			
			paint.setTextSize(font_size);
			paint.setColor(Color.GREEN);
			canvas.drawText("ENHORABUENA :-D", 0, tamaño, paint);
			
			paint.setTextSize(font_size - 15);
			paint.setColor(Color.WHITE);
			canvas.drawText("Ahora superate...", 
					pos_x / 3, dy * ExtraActivity.PUNTAJES - (dy / 3), paint);
		} else {
			paint.setTextSize(font_size);
			paint.setColor(Color.RED);
			canvas.drawText("Por poco... :-/", 0, tamaño, paint);
			
			paint.setTextSize(font_size - 15);
			paint.setColor(Color.WHITE);
			canvas.drawText("Sigue intentando...", 
					pos_x / 3, dy * ExtraActivity.PUNTAJES - (dy / 3), paint);
		}
	}
	
	private void Dibujar(Canvas canvas) {
		
		canvas.drawColor(Color.BLACK);
		
		time++;

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		
		//Establecer la configuracion del paint para el texto
		paint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "pixelmix.ttf"));
		
		int font_size =  VALUES.PANTALLA.y / 18;
		int tamaño = font_size + 5;
		
		int dy = (int) VALUES.PANTALLA.y / (ExtraActivity.PUNTAJES) - 3;
		int pos_x = VALUES.PANTALLA.x / ExtraActivity.PUNTAJES;
		
		//Pintar el laberinto
		laberinto_actual.draw(canvas);
		
		//Pintar la comida
		for(Food food : this.food)
			food.draw(canvas);
		
		//Pintar los fantasmas
		for(Ghost fantasma : fantasmas)
			fantasma.draw(canvas);
		
		//Pintar el player
		player.draw(canvas);
		
		dy = (Constants.SIZE_Y * Constants.GROW_Y) + 5;

		int src_width = player.getAncho();
		int src_height = player.getAlto();
		
		//Pintar las vidas
		for(int i = vidas; i > 0; i--)
		{
			int dst_width =  VALUES.PANTALLA.x - (i * tamaño);
			int dst_height = dy;
			
			Rect src = new Rect(src_width, 0, src_width*2, src_height);
			RectF dst = new RectF(dst_width, dst_height, dst_width + tamaño, dst_height + tamaño);
			
			canvas.drawBitmap(player.getImagen(), src, dst, paint);
		}
		
		//Pintar boton de musica
		RectF lugar = new RectF(VALUES.PANTALLA.x - tamaño, dy + tamaño, VALUES.PANTALLA.x, dy + tamaño * 2);
		boton_musica.Dibujar(canvas, lugar, paint);
		
		//Pintar el puntaje		
		paint.setTextSize(font_size);
		paint.setColor(Color.GRAY);
		canvas.drawText(""+nombre_jugador, 10, dy + tamaño, paint);
		
		paint.setTextSize(font_size - 5);
		paint.setColor(Color.WHITE);
		canvas.drawText(""+puntos, 10, dy + tamaño * 2, paint);
	}

	//El motor logico del juego
	private void Update() {
		
		player.Update();
		
		for(Ghost fantasma : fantasmas)
			fantasma.Update();
				
		if(food.isEmpty()) {
			
			VALUES.ReproducirSonido(VALUES.extra_lives, 0);
			
			puntos += 500;
			Init(nivel+1, vidas+1);
		}
		
		ArrayList<Food> terminada = new ArrayList<Food>();
		
		for(Food comida : food)
			if(comida.getRect().intersect(player.getRect())) {
				terminada.add(comida);
				pastillas_comidas++;
			}
		
		for(Food comida : terminada) {
			if(comida instanceof SpecialFood) {
				
				if(boton_musica.isPlaying())
					VALUES.ReproducirSonido(VALUES.eating_fruit, 0);
				
				AsustarFantasmas();
				puntos += 40;
				time = 0;
			}

			if(boton_musica.isPlaying())
				VALUES.ReproducirSonido(VALUES.eating, 0);
			
			food.remove(comida);
			puntos += 10;
		}
		
		boolean death = false;
		
		for(Ghost fantasma : fantasmas) {
			if(fantasma.getRect().intersect(player.getRect()))
				if(!fantasma.EstaAsustado()) {
					vidas--;
					Reset();
					
					death = true;
				}
				else if(!fantasma.EstaComido()) {

					if(boton_musica.isPlaying())
						VALUES.ReproducirSonido(VALUES.eating_ghosts, 0);
					
					fantasma.Regresar();
					fantasmas_comidos++;
					puntos += fantasmas_comidos * 100;
				}
		}
		
		if(time > Constants.dt - (Constants.dt / 3))
			ParpadearFantasmas();
		
		if(time == Constants.dt)
			TranquilizarFantasmas();
		
		if(death)
			if(boton_musica.isPlaying())
				VALUES.ReproducirSonido(VALUES.death, 0);
		
		if(nivel > 3)
			FinalizarJuego("victoria");
		
		if(vidas < 0)
			FinalizarJuego("derrota");
	}

	private void ParpadearFantasmas() {
		
		for(Ghost fantasma : fantasmas)
			if(!fantasma.EstaComido())
				fantasma.setCambioImagen((time % 2) + 1);
	}

	private void AsustarFantasmas() {
		
		for(Ghost fantasma : fantasmas)
			if(!fantasma.EstaComido() && !fantasma.EstaAsustado())
				fantasma.Asustar();
	}

	private void TranquilizarFantasmas() {
		
		ResetValues();
		
		for(Ghost fantasma : fantasmas)
			if(!fantasma.EstaComido())
				fantasma.Tranquilizar();
	}

	private void Reset() {

		ResetValues();
		
		player.Reset();
	
		for(Ghost ghost : fantasmas)
			ghost.Reset();
	}

	private void Init(int nivel, int vidas) {
		
		pastillas_comidas = 0;
		
		setConfigNivel(nivel, vidas);
		ResetValues();
		
		InitPlayer();
		
		player.Parado(true);
		
		InitGhosts();
		InitFood();
		
		StartLoop();
		
		player.Parado(false);
	}
	
	private void ResetValues() {
		
		time = 0;
		fantasmas_comidos = 0;
	}
	
	public void setConfigNivel(int nivel, int vidas) {
		
		this.vidas = vidas;
		this.nivel = nivel;
		
		if(nivel < 4)
			this.laberinto_actual = new Laberinto(super.getContext(), VALUES.mapas.get(nivel), nivel);
	}

	private void InitFood() {

		food = new ArrayList<Food>();
		
		for(PointF point : laberinto_actual.getFood())
			food.add(new Food(super.getContext(), point));
		
		for(int s = 0; s < 4; s++)
			food.add(new SpecialFood(super.getContext(), laberinto_actual.getSpecialFood().get(s)));
	}

	private void InitGhosts() {

		fantasmas = new ArrayList<Ghost>();
		
		fantasmas.add(new Blinky(super.getContext(), this, VALUES.ghosts.get(Ghost.BLINKY), 
				laberinto_actual.getPosFantasmas().get(Ghost.BLINKY), laberinto_actual.getPosRegreso()));
		
		fantasmas.add(new Inky(super.getContext(), this, VALUES.ghosts.get(Ghost.INKY), 
				laberinto_actual.getPosFantasmas().get(Ghost.INKY), laberinto_actual.getPosRegreso()));
		
		fantasmas.add(new Clyde(super.getContext(), this, VALUES.ghosts.get(Ghost.CLYDE), 
				laberinto_actual.getPosFantasmas().get(Ghost.CLYDE), laberinto_actual.getPosRegreso()));
		
		fantasmas.add(new Pinky(super.getContext(), this, VALUES.ghosts.get(Ghost.PINKY), 
				laberinto_actual.getPosFantasmas().get(Ghost.PINKY), laberinto_actual.getPosRegreso()));
		
		search = new SearchEngine(fantasmas);
		search.setRunning(true);
		search.start();
	}

	private void InitPlayer() {
		player = new Player(super.getContext(), this, VALUES.pacman, 
				laberinto_actual.getPosPacman());
	}

	public Laberinto getLaberintoActual() {
		return laberinto_actual;
	}

	public Player getPlayer() {
		return player;
	}
	
	public ArrayList<Ghost> getFantasmas() {
		return fantasmas;
	}

	public int getPuntos() {
		return puntos;
	}
	
	public String getJugador() {
		return nombre_jugador;
	}
	
	public BotonMusica getBotonMusica() {
		return boton_musica;
	}

	public void Pausa() {
		
	}

	public void Reanudar() {
		
	}

	public void Chao() {
		
	}

	private void FinalizarJuego(String string) {
		
		juego_finalizado = true;
		
		GuardarPuntuaciones();
		
		Intent intent = new Intent(padre.getApplicationContext(), ExtraActivity.class);
		intent.putExtra("opcion", "puntajes");
		
		if(string.equals("victoria"))
			victoria = true;
		
		if(string.equals("derrota"))
			victoria = false;
			
		padre.startActivity(intent);
	
	}
	
	private void GuardarPuntuaciones() {

	    SharedPreferences saved = getContext().getSharedPreferences("Puntajes", Activity.MODE_PRIVATE);
	   
	    ArrayList<Integer> puntajes = new ArrayList<Integer>();
	    ArrayList<String> nombres = new ArrayList<String>();
	    
	    int score = getPuntos();
	    String jugador = getJugador();
	    
	    int x = 0;
	    boolean found = false;

	    for(int i = 0; i < ExtraActivity.PUNTAJES; i++)
	    {
	    	Integer puntos = (Integer) (saved.getInt("puntos"+i, 0));
	    	String nombre = saved.getString("nombres"+i, "nadie");
	    	
	    	puntajes.add(puntos);
	    	nombres.add(nombre);	
	    	
	    	if(score >= puntos)
	    	{
	    		x = i;
	    		found = true;
	    	}
	    }
	    
	    SharedPreferences.Editor edit = saved.edit();
	    
	    if(found)
	    {
    		for(int j = 0; j < x; j++)
    	    {
    	    	edit.putInt("puntos"+j, puntajes.get(j+1));
    	    	edit.putString("nombres"+j, nombres.get(j+1));
    	    }

	    	edit.putInt("puntos"+x, score);
    		edit.putString("nombres"+x, jugador);
    		
    		edit.commit();
	    }
	}
	
	private void StartLoop() {
		
		final GameView instance = this;
		holder = getHolder();
		
		holder.addCallback(new SurfaceHolder.Callback() {
				
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				boolean retry = true;
				loop.SetRunning(false);
				
				while (retry) {
                   try {
                         loop.join();
                         retry = false;
                   } catch (InterruptedException e) {
                	   e.printStackTrace();
                   }
				}
			}
		
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				
				loop = new Loop(instance, fantasmas);

				loop.SetRunning(true);
				loop.start();
			}
		
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			
			}
		});
	}

}
