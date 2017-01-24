package com.example.home.camera;


import android.app.Fragment;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.Locale;

import static com.example.home.camera.colorHelper.ColorHelper.*;

import com.example.home.camera.colorHelper.Matcher;


/**
 * Created by robertfernandes on 1/20/2017.
 */

public class ColorViewFragment extends Fragment {

    private int color1 = Color.BLACK;
    private int color2 = Color.BLACK;

    private SurfaceView colorView1;
    private SurfaceView colorView2;

    private TextToSpeech speech;
   // private double[] correctionValues;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(getActivity());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.color_view_layout, container, false);
        colorView1 = (SurfaceView) view.findViewById(R.id.color1);
        colorView2 = (SurfaceView) view.findViewById(R.id.color2);
        colorView1.setBackgroundColor(Color.BLACK);
        colorView2.setBackgroundColor(Color.BLACK);
        return view;
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

      //  correctionValues =  calculateCorrection(color);
        //color1 = Color.rgb(Math.min(WHITE, (int)(Color.red(color1) * correctionValues[0])), Math.min(WHITE, (int)(Color.green(color1) * correctionValues[1])), Math.min(WHITE,(int)(Color.blue(color1) * correctionValues[2])));

        speech.speak(getColorName(getClosestColor(color1)), TextToSpeech.QUEUE_FLUSH, Bundle.EMPTY, TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
        update();
    }

    public void setColor2(int color) {
        color2 = color;

        Log.println(Log.INFO, "TAG", "Color2 value " + String.format("#%06X", (0xFFFFFF & color2)));

        //correctionValues = calculateCorrection(color);

        //color2 = Color.rgb(Math.min(WHITE, (int)(Color.red(color2) * correctionValues[0])), Math.min(WHITE, (int)(Color.green(color2) * correctionValues[1])), Math.min(WHITE,(int)(Color.blue(color2) * correctionValues[2])));

        //Log.println(Log.INFO, "TAG", "Correction Values " + Arrays.toString(correctionValues));
        speech.speak(getColorName(getClosestColor(color2)), TextToSpeech.QUEUE_FLUSH, Bundle.EMPTY, TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
        update();

        if(new Matcher().isMatch(color1,color2)){
            speech.speak("Match", TextToSpeech.QUEUE_FLUSH, Bundle.EMPTY, TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
        }

        else{
            speech.speak("Not a Match", TextToSpeech.QUEUE_FLUSH, Bundle.EMPTY, TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);

        }
    }

    public int getColor1(){
        return(color1);
    }

    public int getColor2(){
        return(color2);
    }

    private void speak(String sp) {
        speech.speak(sp, TextToSpeech.QUEUE_FLUSH, Bundle.EMPTY, TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
    }

    public void update() {
        colorView1.setBackgroundColor(color1);
        colorView2.setBackgroundColor(color2);
    }

}
