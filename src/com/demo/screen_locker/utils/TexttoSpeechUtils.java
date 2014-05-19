
package com.demo.screen_locker.utils;

import java.util.Locale;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

import com.demo.screen_locker.StartActivity;

public class TexttoSpeechUtils {

    public static TextToSpeech sTextToSpeech;

    public static void ShutDown() {
        if (sTextToSpeech != null) {
            sTextToSpeech.stop();
            sTextToSpeech = null;
        }
    }

    public static void SpeechText(Context context, final String text) {
        try {
            ShutDown();
            sTextToSpeech = new TextToSpeech(context, new OnInitListener() {

                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        int result = sTextToSpeech.setLanguage(Locale.ENGLISH);
                        if (result == TextToSpeech.LANG_MISSING_DATA
                                || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                            SLog.d(StartActivity.sTag,
                                    "TextToSpeech::onInit Error");
                        } else {
                            SLog.d(StartActivity.sTag,
                                    "TextToSpeech::onInit OK");
                            sTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH,
                                    null);
                        }
                    }
                }
            });
        } catch (Exception e) {

        }

    }
}
