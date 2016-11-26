package com.example.home.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;

import java.util.Locale;

import static com.example.home.camera.ColorHelper.*;


/**
 * Created by home on 31/10/2016.
 */

public class ColorView extends SurfaceView {
    private static int RED_Correction = 0x2C;
    private static int GREEN_Correction = 0x3C;
    private static int BLUE_Correction = 0x3C;
    private static int WHITE = 0xFF;

    private int color1 = Color.BLACK;
    private int color2 = Color.BLACK;
    private Paint paint = new Paint();

    private TextToSpeech speech;

    public ColorView(Context context) {
        super(context);
        init(context);
    }

    public ColorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ColorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        speech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

            }
        });
        speech.setLanguage(Locale.getDefault());
    }

    public void setColor1(int color) {
        color1 = color;
        Log.println(Log.INFO, "TAG", "Color1 value " + String.format("#%06X", (0xFFFFFF & color1) ));

        color1 = Color.rgb(Math.min(WHITE, Color.red(color1) + RED_Correction), Math.min(WHITE, Color.green(color1) + GREEN_Correction), Math.min(WHITE,Color.blue(color1) + BLUE_Correction));

        Log.println(Log.INFO, "TAG", "Corrected Color1 value " + String.format("#%06X", (0xFFFFFF & color1) ));
        speech.speak(getColorName(getClosestColor(color1)), TextToSpeech.QUEUE_FLUSH, Bundle.EMPTY, TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
        update();
    }

    public void setColor2(int color) {
        color2 = color;
        Log.println(Log.INFO, "TAG", "Color2 value " + String.format("#%06X", (0xFFFFFF & color2)));

        color2 = Color.rgb(Math.min(WHITE, Color.red(color2) + RED_Correction), Math.min(WHITE, Color.green(color2) + GREEN_Correction), Math.min(WHITE,Color.blue(color2) + BLUE_Correction));

        Log.println(Log.INFO, "TAG", "Corrected Color1 value " + String.format("#%06X", (0xFFFFFF & color2) ));
        speech.speak(getColorName(getClosestColor(color2)), TextToSpeech.QUEUE_FLUSH, Bundle.EMPTY, TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
        isMatchTTS();
        update();
    }

    public int getColor1(){
        return(color1);
    }

    public int getColor2(){
        return(color2);
    }

    private void isMatchTTS(){
        if(isComplementaryMatch(color1,color2)
                || isGrayScaleMatch(color1,color2)
                || isAnalogousMatch(color1,color2)
                || isTriadMatch(color1,color2)
                || isWarmMatch(color1,color2)
                || isCoolMatch(color1,color2)
                || isSaturationMatch(color1,color2)){

            //These if statements are just for testing
            if(isComplementaryMatch(color1,color2)){
                boolean b1 = isComplementaryMatch(color1,color2);
                speech.speak("Comp Match",TextToSpeech.QUEUE_FLUSH, Bundle.EMPTY, TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
            }
            else if(isGrayScaleMatch(color1,color2)){
                boolean b2 = isGrayScaleMatch(color1,color2);
                speech.speak("Grey Match",TextToSpeech.QUEUE_FLUSH, Bundle.EMPTY, TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
            }
            else if(isAnalogousMatch(color1,color2)){
                boolean b3 = isAnalogousMatch(color1,color2);
                speech.speak(" Side Match",TextToSpeech.QUEUE_FLUSH, Bundle.EMPTY, TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
            }
            else if(isTriadMatch(color1,color2)){
                boolean b4 = isTriadMatch(color1,color2);
                speech.speak("Triad Match",TextToSpeech.QUEUE_FLUSH, Bundle.EMPTY, TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
            }
            else if(isWarmMatch(color1,color2)){
                boolean b5 = isWarmMatch(color1,color2);
                speech.speak("Warm Match",TextToSpeech.QUEUE_FLUSH, Bundle.EMPTY, TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
            }
            else if(isCoolMatch(color1,color2)){
                boolean b6 = isCoolMatch(color1,color2);
                speech.speak("Cool Match",TextToSpeech.QUEUE_FLUSH, Bundle.EMPTY, TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
            }
            else if(isSaturationMatch(color1,color2)){
                boolean b6 = isSaturationMatch(color1,color2);
                speech.speak("Sat Match",TextToSpeech.QUEUE_FLUSH, Bundle.EMPTY, TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
            }
            //speech.speak("Match",TextToSpeech.QUEUE_FLUSH, Bundle.EMPTY, TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
        }
        else{
            speech.speak("Not a match.",TextToSpeech.QUEUE_FLUSH, Bundle.EMPTY, TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);

        }
    }

    public void update() {
        Canvas canvas = getHolder().lockCanvas();

        if (canvas != null) {
            int width = canvas.getWidth();
            int height = canvas.getHeight();

            paint.setStyle(Paint.Style.FILL);
            paint.setColor(color1);

            canvas.drawRect(0, 0, width / 2, height, paint);

            paint.setColor(color2);
            canvas.drawRect(width / 2, 0, width, height, paint);

            getHolder().unlockCanvasAndPost(canvas);
        }
    }
}
