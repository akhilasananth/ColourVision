package com.example.home.camera.colorHelper;

import android.content.Context;

import com.example.home.camera.R;

/**
 * Created by StartGazer on 3/3/2017.
 */

public class EmotionsAlgorithm {

    public boolean isMatch(String emotion,int color){

        return (ColorHelper.getColorEmotion(color).equals(emotion));
    }


}
