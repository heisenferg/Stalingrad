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

import java.util.ArrayList;

public class Juego extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {
    private Bitmap bmpMapa;
    private Bitmap avion;
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
    private int estadoAvion =1;
    private int puntero_Avion_sprite =0;
    private int avionW, avionH;
    private int contador_Frames = 0;
    private int yAvion;
    private float posicionAvion[] = new float[2];
    private float velocidadAvion[] = new float[2];

    private float gravedad [] =new float[2];
    private float posicionInicialAvion[]= new float[2];

    private int tiempoCrucePantalla = 3;
    private float deltaT;
    private boolean salta = false;
    private boolean hayToque=false;
    private ArrayList<Toque> toques = new ArrayList<Toque>();
    private Control[] controles = new Control[3];
    private final int ARRIBA=0;
    private final int ABAJO=1;
    private final int DISPARO=2;

    public Juego(Activity context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
        Display mdisp = context.getWindowManager().getDefaultDisplay();
        bmpMapa = BitmapFactory.decodeResource(getResources(), R.drawable.fondo);
            if (MainActivity.bando == 1){
                avion = BitmapFactory.decodeResource(getResources(), R.drawable.ger2);
            } else {
                avion = BitmapFactory.decodeResource(getResources(), R.drawable.sov2);
        }
        Log.d("BANDO: " , " es " + MainActivity.bando);
        mapaH = bmpMapa.getHeight();
        mapaW = bmpMapa.getWidth();

        deltaT = 1f/BucleJuego.MAX_FPS;


        Point mdispSize = new Point();
        mdisp.getSize(mdispSize);


        velocidadAvion[x] = maxX/tiempoCrucePantalla;
        velocidadAvion[y] = 0;



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

        avionW = avion.getWidth();
        avionH = avion.getHeight();
        // creamos el game loop


        posicionInicialAvion[x] = maxY*1/5 ;


        destMapaY = (maxY-mapaH)/2;

        //  posicionMario[y] = destMapaY + marioH * 9/10;

        //posicionInicialAvion[y]=  maxX*0.1f;
        posicionInicialAvion[y]=  maxX / 2 - avion.getHeight()/2;

        posicionAvion[x]= posicionInicialAvion[x];
        posicionAvion[y]= posicionInicialAvion[y];

        velocidadAvion[x]= maxX/tiempoCrucePantalla;
        velocidadAvion[y]= -velocidadAvion[x]*2;


        // se crea la superficie, creamos el game loop
        bucle = new BucleJuego(getHolder(), this);
        setOnTouchListener(this);

        // Hacer la Vista focusable para que pueda capturar eventos
        setFocusable(true);

        CargaControles();

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
        //Posición avion
        yAvion = maxY- avion.getHeight()/2;
        puntero_Avion_sprite = avionW/6* estadoAvion;

        //Velocidad
        posicionAvion[x] = posicionAvion[x] + deltaT * velocidadAvion[x];
        posicionAvion[y] = posicionAvion[y] + deltaT * velocidadAvion[y];

//Gravedad
        velocidadAvion[x] = 0;
        velocidadAvion[y] = velocidadAvion[y] + deltaT;


        estadoAvion++;

        if (contadorFrames%3==0){

            if (estadoAvion >3){
                estadoAvion =1;
            }
        }
/*
        if (posicionAvion[x] > maxX+(avionW /21) || posicionAvion[x]<=0) {
            velocidadAvion[x] = velocidadAvion[x] * -1;
        }
*/
  /*      //Rebote
        if(posicionAvion[y]>= posicionInicialAvion[y]){
            velocidadAvion[y]= -(maxX/tiempoCrucePantalla)*2;
            posicionAvion[y] = posicionInicialAvion[y];

        }
*/
        if(salta){
            // bucle.ejecutandose=false;
            velocidadAvion[y]=-velocidadAvion[x]*2;
            gravedad[y]=-velocidadAvion[y]*2;
            salta = false;
        }

        for (int i=0; i<3; i++){
            if (controles[i].pulsado){
                Log.d("Control: ", "Se ha pulsado " + controles[i].nombre);
            }
        }

        if (controles[ARRIBA].pulsado){

        }
        if (controles[ABAJO].pulsado){

        }
        if (controles[DISPARO].pulsado){

        }


    }

    /**
     * Este método dibuja el siguiente paso de la animación correspondiente
     */

    public void renderizar(Canvas canvas) {
        if(canvas!=null) {

            Paint myPaint = new Paint();
            myPaint.setStyle(Paint.Style.STROKE);
            myPaint.setColor(Color.WHITE);

            //Toda el canvas en rojo
            canvas.drawColor(Color.RED);



            //Dibujar mapa
            canvas.drawBitmap(bmpMapa, 0, destMapaY, null);

            //Dibujar muñeco
            //canvas.drawBitmap(mario, xMario, yMario, null);
            //Recortar muñeco
            canvas.drawBitmap(avion, new Rect(puntero_Avion_sprite,0, puntero_Avion_sprite + (avionW /6), avionH *1/5),
                    new Rect( (int) posicionAvion[x], yAvion, (int) (avionW /6 ), destMapaY+mapaH*1/2), null);

            //dibujar un texto
            myPaint.setStyle(Paint.Style.FILL);
            myPaint.setTextSize(40);
            canvas.drawText("Frames ejecutados:"+contadorFrames, 600, 1000, myPaint);


            //Dibujar controles
            myPaint.setAlpha(400);
            for (int i = 0; i<3; i++){
                controles[i].Dibujar(canvas, myPaint);
            }
        }
    }

    public void CargaControles(){
        float aux;

        //ARRIBA
        controles[ARRIBA]=new Control(getContext(),400,maxY/5*4);
        controles[ARRIBA].Cargar(R.drawable.arriba);
        controles[ARRIBA].nombre="IZQUIERDA";
        //ABAJO
        controles[ABAJO]=new Control(getContext(),
                controles[0].Ancho()+controles[0].xCoordenada+50,controles[0].yCoordenada);
        controles[ABAJO].Cargar(R.drawable.abajo);
        controles[ABAJO].nombre="DERECHA";

        //disparo
        aux=5.0f/7.0f*maxX; //en los 5/7 del ancho
        controles[DISPARO]=new Control(getContext(),aux,controles[0].yCoordenada);
        controles[DISPARO].Cargar(R.drawable.fuego);
        controles[DISPARO].nombre="DISPARO";
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
        int index;
        int x,y;

        // Obtener el pointer asociado con la acción
        index = event.getActionIndex();


        x = (int) event.getX(index);
        y = (int) event.getY(index);

        switch(event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                hayToque=true;

                synchronized(this) {
                    toques.add(index, new Toque(index, x, y));
                }

                //se comprueba si se ha pulsado
                for(int i=0;i<3;i++)
                    controles[i].comprueba_Pulsado(x,y);
                break;

            case MotionEvent.ACTION_POINTER_UP:
                synchronized(this) {
                    toques.remove(index);
                }

                //se comprueba si se ha soltado el botón
                for(int i=0;i<3;i++)
                    controles[i].compruebaSoltado(toques);
                break;

            case MotionEvent.ACTION_UP:
                synchronized(this) {
                    toques.clear();
                }
                hayToque=false;
                //se comprueba si se ha soltado el botón
                for(int i=0;i<3;i++)
                    controles[i].compruebaSoltado(toques);
                break;
        }

        return true;
    }
}
