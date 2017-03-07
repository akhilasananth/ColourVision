package com.example.home.camera.colorHelper;

import android.content.Context;

import com.example.home.camera.R;

/**
 * Created by StartGazer on 3/3/2017.
 */

public class EmotionsAlgorithm {
    private String[] colorHex;
    private String[] colorEmotions;
    private Context context;
    private String emotion;

    public EmotionsAlgorithm(Context c, String emotionEntered){
        this.emotion = emotionEntered;
        this.context = c;
        this.colorHex = context.getResources().getStringArray(R.array.values);
        this.colorEmotions = context.getResources().getStringArray(R.array.emotions);
    }

    public boolean isMatch(int c1, int c2){
        return(equalsEmotion(c1)|| equalsEmotion(c2));
    }

    private int getIndex(int color){
        String hexVal = "#" + Integer.toHexString(color).substring(2);
        int index =0;
        for(int i = 0; i<colorHex.length; i++){
            if(colorHex[i].equals(hexVal)){
                index = i;
            }
        }
        return index;
    }


    private boolean equalsEmotion(int color){
        return (colorEmotions[getIndex(color)].equals(emotion));
    }


}
