package com.onixgames.pacman;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

public abstract class Ghost extends Personaje {
	
	protected final static int MODO_CAZADOR = 18;
	protected final static int MODO_ASUSTADO = 81;
	
	protected final static int BLINKY = 0;
	protected final static int PINKY = 1;
	protected final static int INKY = 2;
	protected final static int CLYDE = 3;
	
	protected int direccion;
	protected int current_frame;
	protected int width;
	protected int height; 
	
	protected boolean scared;
	protected boolean comido;
	protected volatile int modo;
	
	protected PointF pos_regreso;
	protected Bitmap last_image;
	protected LinkedList<PointF> camino;
	protected Integer[] cambio_imagen;
	protected ArrayList<Integer[]> logica_imagenes;
	protected Random random;

	protected volatile LinkedList<PointF> ruta;
	//protected volatile LinkedList<PointF> ruta_aux;
	
	public Ghost(Context context, GameView laberinto, Bitmap imagen, PointF inicial, PointF regreso) {
		super(context, laberinto, imagen, inicial);

		pos_regreso = new PointF(regreso.x * Constants.GROW_X, regreso.y * Constants.GROW_Y);
		
		modo = Ghost.MODO_CAZADOR;
		
		current_frame = 0;
		direccion = 1;
		
		scared = false;
		comido = false;
		
		can_move = false;
		
		last_image = imagen;
		last_direccion = direccion;
		
		width = imagen.getWidth() / 6;
		height = imagen.getHeight();
		
		camino = new LinkedList<PointF>();
		
		logica_imagenes = new ArrayList<Integer[]>();
		
		Integer cambio_1[] = {0, getAncho() * 2, 0, getAncho() * 4};
		logica_imagenes.add(cambio_1);
	
		Integer cambio_2[] = {0, 0, 0, 0};
		logica_imagenes.add(cambio_2);

		Integer cambio_3[] = {getAncho() * 2, getAncho() * 2, getAncho() * 2, getAncho() * 2};
		logica_imagenes.add(cambio_3);
		
		cambio_imagen = logica_imagenes.get(0);

		ruta = new LinkedList<PointF>();
		//ruta_aux = new LinkedList<PointF>();
		random = new Random();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		//Update();
		Dibujar(canvas);
	}

	@Override
	protected void Dibujar(Canvas canvas) {
		
		int srcX = (getAncho() * current_frame) + cambio_imagen[direccion];
		int srcY = getAlto();
		
		Rect src = new Rect(srcX, 0, (srcX)+getAncho(), srcY);
		RectF dst = getRect();
		
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		
        if(imagen != null)
			canvas.drawBitmap(imagen, src, dst,  paint);
	}

	@Override
	protected void Update() {
		
		current_frame = ++current_frame % 2;
		
		if(!can_move)
			return;
		
		if(EstaComido()) {
			if(camino.size() > 0)
				ToHome();
			else
				Tranquilizar();
		}
		else if(EstaAsustado())
			Mover();
	}
	
	/*public void Mover() {
		
		int i = random.nextInt();
		
		PointF llegada = laberinto.getLaberintoActual().getSpecialFood().get(current_frame + i % 4);
		llegada.set(llegada.x * Constants.GROW_X, llegada.y * Constants.GROW_Y);	
	}*/
	
	
	protected abstract void CalcularRuta();
	
	private void ToHome() {
	
		PointF llegada = camino.poll();
		pos_actual.set(llegada);
	}

	@Override
	protected PointF getWay(int direccion) {

		setDireccion(direccion);
		
		if(direccion == Constants.DERECHA && 
				last_direccion != Constants.IZQUIERDA)
			return getMove(new Point(1, 0), pos_actual);
		
		if(direccion == Constants.IZQUIERDA && 
				last_direccion != Constants.DERECHA)
			return getMove(new Point(-1, 0), pos_actual);
		
		if(direccion == Constants.ARRIBA && 
				last_direccion != Constants.ABAJO)
			return getMove(new Point(0, -1), pos_actual);
		
		if(direccion == Constants.ABAJO && 
				last_direccion != Constants.ARRIBA)
			return getMove(new Point(0, 1), pos_actual);
		
		return null;
	}
	
	public void setModo(int modo) {
		
		this.modo = modo;
	}
	
	public synchronized void Asustar() {
	
		CleanPath();
		
		setScared(true);
		setScaredImage();

		setModo(Ghost.MODO_ASUSTADO);
	}	
	
	public void Regresar() {
	
		CleanPath();
		
		setComido(true);
		setEyesImage();
		
		EncontrarCamino();
	}
	
	public synchronized void Tranquilizar() {
	
		CleanPath();
		
		setComido(false);
		setScared(false);
		setNormalImage();
		
		setModo(Ghost.MODO_CAZADOR);
	}
	
	private void CleanPath() {
	
		camino = new LinkedList<PointF>();
		ruta = new LinkedList<PointF>();
	}

	private void setScaredImage() {

		setImagen(VALUES.dead_ghost);
		setCambioImagen(1);
	}
	
	private void setNormalImage() {
		
		setImagen(last_image);
		setCambioImagen(0);
	}
	
	private void setEyesImage() {
		
		setImagen(VALUES.eyes);
		setCambioImagen(0);
	}
	
	public void setCambioImagen(int x) {
		cambio_imagen = logica_imagenes.get(x);
	}
	
	public void setScared(Boolean scared) {
		this.scared = scared;
	}
	
	public Boolean EstaAsustado(){
		return scared;
	}

	public void Reset() {
		
		CleanPath();
		
		pos_actual.set(pos_inicial);
	
		Tranquilizar();	
	}

	public void setComido(boolean comido) {
		this.comido = comido;
	}
	
	public boolean EstaComido() {
		return comido;
	}

	private void EncontrarCamino() {

		RectF table[][] = laberinto.getLaberintoActual().getGrafico();
		
		PointF salida = new PointF();
		salida.set(pos_actual);
		
		PointF llegada = new PointF();
		llegada.set(pos_regreso);
		
		//Busqueda del camino minimo por el algoritmo A Estrella o Profundida
		
		NodoTablero nodo = Busqueda.AStar(table, salida, llegada);
		
		//Hago una ruta
		camino = nodo.getCamino();
	}

	@Override
	public int getAncho() {
		return width;
	}

	@Override
	public int getAlto() {
		return height;
	}
	
	public LinkedList<PointF> getRuta() {
		return ruta;
	}

	public void setRuta(LinkedList<PointF> ruta) {
		this.ruta = ruta;
	}

	/*protected LinkedList<PointF> CombinarCaminos(LinkedList<PointF> base, LinkedList<PointF> agregado) {
		
		HashSet<PointF> aux = new HashSet<PointF>();
		LinkedList<PointF> mezcla = new LinkedList<PointF>();

		aux.addAll(base);
		aux.addAll(agregado);
		
		mezcla.addAll(aux);
		
		return mezcla;
	}*/
}