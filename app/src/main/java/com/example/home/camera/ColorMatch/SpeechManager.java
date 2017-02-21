package com.example.home.camera.ColorMatch;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

import java.util.List;
import java.util.Locale;

/**
 * Created by robertfernandes on 2/21/2017.
 */

public class SpeechManager {

    private Activity activity;
    private TextToSpeech speech;

    public SpeechManager(Activity activity) {
        speech = new TextToSpeech(activity, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

            }
        });
        speech.setLanguage(Locale.getDefault());
    }

    private void speak(String message) {
        speech.speak(message, TextToSpeech.QUEUE_FLUSH, Bundle.EMPTY, TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
    }

    private void speakList(List<String> messages) {
        for (String s : messages) {
            speech.speak(s, TextToSpeech.QUEUE_ADD, Bundle.EMPTY, TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
        }
    }
}
