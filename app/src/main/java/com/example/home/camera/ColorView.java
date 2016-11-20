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

import static com.example.home.camera.ColorHelper.getClosestColor;
import static com.example.home.camera.ColorHelper.getColorName;
import static com.example.home.camera.ColorHelper.isComplementaryMatch;
import static com.example.home.camera.ColorHelper.isGrayScaleMatch;


/**
 * Created by home on 31/10/2016.
 */

public class ColorView extends SurfaceView {

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
        speech.speak(getColorName(getClosestColor(color)), TextToSpeech.QUEUE_FLUSH, Bundle.EMPTY, TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
        update();
    }

    public void setColor2(int color) {
        color2 = color;
        speech.speak(getColorName(getClosestColor(color)), TextToSpeech.QUEUE_FLUSH, Bundle.EMPTY, TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
        if(isComplementaryMatch(color1,color2) && isGrayScaleMatch(color2)){
            speech.speak("Match",TextToSpeech.QUEUE_FLUSH, Bundle.EMPTY, TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
        }
        else{
            speech.speak("Not a match.",TextToSpeech.QUEUE_FLUSH, Bundle.EMPTY, TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);

        }
        update();
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
