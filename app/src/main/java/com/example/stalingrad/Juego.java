package com.example.stalingrad;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class Juego extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {
    private Bitmap bmpMapa;
    private Bitmap missile;
    private SurfaceHolder holder;
    private BucleJuego bucle;

    private int x=0,y=1; //Coordenadas x e y para desplazar

    private int maxX=0;
    private int maxY=0;
    private int contadorFrames=0;
    private boolean hacia_abajo=true;
    private static final String TAG = Juego.class.getSimpleName();
    // private int xMario=0, yMario=0;
    private int mapaH, mapaW;
    private int destMapaY;
    private int estado_heli=0;
    private int puntero_heli_sprite =0;
    private int helicopteroW, helocopteroH;
    private int contador_Frames = 0;
    private int yHelicoptero;
    private float posicionHelicoptero[] = new float[2];
    private float velocidadHelicoptero[] = new float[2];

    private float gravedad [] =new float[2];
    private float posicionInicialHelicptero[]= new float[2];

    private int tiempoCrucePantalla = 3;
    private float deltaT;
    private boolean salta = false;




    public Juego(Activity context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
        Display mdisp = context.getWindowManager().getDefaultDisplay();
        bmpMapa = BitmapFactory.decodeResource(getResources(), R.drawable.fondo2);
        missile = BitmapFactory.decodeResource(getResources(), R.drawable.missile);

        mapaH = bmpMapa.getHeight();
        mapaW = bmpMapa.getWidth();

        deltaT = 1f/BucleJuego.MAX_FPS;


        Point mdispSize = new Point();
        mdisp.getSize(mdispSize);


        velocidadHelicoptero[x] = maxX/tiempoCrucePantalla;
        velocidadHelicoptero[y] = 0;

    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        // Para interceptar los eventos de la SurfaceView
        getHolder().addCallback(this);
        Canvas c = holder.lockCanvas();
        maxX =c.getWidth();
        maxY = c.getHeight();
        holder.unlockCanvasAndPost(c);

        helicopteroW = missile.getWidth();
        helocopteroH = missile.getHeight();
        // creamos el game loop


        posicionInicialHelicptero[x] =destMapaY ;


        destMapaY = (maxY-mapaH)/2;

        //  posicionMario[y] = destMapaY + marioH * 9/10;

        posicionInicialHelicptero[y]=  maxX*0.1f;

        posicionHelicoptero[x]= posicionInicialHelicptero[x];
        posicionHelicoptero[y]= posicionInicialHelicptero[y];

        velocidadHelicoptero[x]= maxX/tiempoCrucePantalla;
        velocidadHelicoptero[y]= -velocidadHelicoptero[x]*2;

        gravedad[x] = 0f;
        gravedad[y] = -velocidadHelicoptero[y]*2;

        // se crea la superficie, creamos el game loop
        bucle = new BucleJuego(getHolder(), this);
        setOnTouchListener(this);

        // Hacer la Vista focusable para que pueda capturar eventos
        setFocusable(true);

        //comenzar el bucle
        bucle.start();

    }

    /**
     * Este método actualiza el estado del juego. Contiene la lógica del videojuego
     * generando los nuevos estados y dejando listo el sistema para un repintado.
     */
    public void actualizar() {

        //Vector de velocidad
        // xMario = xMario+mapaW/(bucle.MAX_FPS*3);


        contadorFrames++;
        //Posición marioY
        yHelicoptero = destMapaY+mapaH*9/10- missile.getHeight()*2/3;
        puntero_heli_sprite = helicopteroW/21* estado_heli;
        contadorFrames++;



        //Velocidad
        posicionHelicoptero[x] = posicionHelicoptero[x] + deltaT * velocidadHelicoptero[x];
        posicionHelicoptero[y] = posicionHelicoptero[y] + deltaT * velocidadHelicoptero[y];

//Gravedad
        velocidadHelicoptero[x] = velocidadHelicoptero[x] + deltaT*gravedad[x];
        velocidadHelicoptero[y] = velocidadHelicoptero[y] + deltaT*gravedad[y];

        estado_heli++;

        if (contadorFrames%3==0){

            if (estado_heli >3){
                estado_heli =0;
            }
        }

        if (posicionHelicoptero[x] > maxX+(helicopteroW/21) || posicionHelicoptero[x]<=0) {
            velocidadHelicoptero[x] = velocidadHelicoptero[x] * -1;
        }

        //Rebote
        if(posicionHelicoptero[y]>= posicionInicialHelicptero[y]){
            velocidadHelicoptero[y]= -(maxX/tiempoCrucePantalla)*2;
            posicionHelicoptero[y] = posicionInicialHelicptero[y];

        }

        if(salta){
            // bucle.ejecutandose=false;
            velocidadHelicoptero[y]=-velocidadHelicoptero[x]*2;
            gravedad[y]=-velocidadHelicoptero[y]*2;
            salta = false;
        }


    }

    /**
     * Este método dibuja el siguiente paso de la animación correspondiente
     */

    public void renderizar(Canvas canvas) {
        if(canvas!=null) {

            Paint myPaint = new Paint();
            myPaint.setStyle(Paint.Style.STROKE);

            //Toda el canvas en rojo
            canvas.drawColor(Color.RED);



            //Dibujar mapa
            canvas.drawBitmap(bmpMapa, 0, destMapaY, null);

            //Dibujar muñeco
            //canvas.drawBitmap(mario, xMario, yMario, null);
            //Recortar muñeco
            canvas.drawBitmap(missile, new Rect(puntero_heli_sprite,0, puntero_heli_sprite +helicopteroW/21, helocopteroH *2/3),
                    new Rect( (int) posicionHelicoptero[x], yHelicoptero, (int) posicionHelicoptero[x]+helicopteroW/21, destMapaY+mapaH*9/10), null);

            //dibujar un texto
            myPaint.setStyle(Paint.Style.FILL);
            myPaint.setTextSize(40);
            canvas.drawText("Frames ejecutados:"+contadorFrames, 600, 1000, myPaint);

        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "Juego destruido!");
        // cerrar el thread y esperar que acabe
        boolean retry = true;
        while (retry) {
            try {
                // bucle.fin();
                bucle.join();
                retry = false;
            } catch (InterruptedException e) {

            }
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_UP:
                salta = true;

        }
        return true;
    }
}
