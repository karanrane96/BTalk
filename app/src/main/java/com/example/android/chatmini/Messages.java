package com.example.android.chatmini;

import android.os.AsyncTask;
import android.util.Log;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.concurrent.ExecutionException;

/**
 * Created by Karan on 15-05-2018.
 */

public class Messages {
    private String message, type;
    private long  time;
    private boolean seen;

    public static String from;

    public Messages(String from) {
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Messages(String message, String type, long time, boolean seen) {
        this.message = message;
        this.type = type;
        this.time = time;
        this.seen = seen;
    }

    public String getMessage() throws ExecutionException, InterruptedException {

        Log.d("frst: ",this.message);
        from = new TranslateAsync(this.message).execute().get();
        return from;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public Messages(){

    }



    //for translation
   public static class TranslateAsync extends AsyncTask<Void, Void, String> {

        private String fromTxt;

        public TranslateAsync(String fromTxt) {
            this.fromTxt = fromTxt;
        }

        @Override
        protected String doInBackground(Void... params) {

                Log.d("txt", fromTxt);

            TranslateOptions options = TranslateOptions.newBuilder().setApiKey("AIzaSyDSrl0xG_FYAoUdp0Jfl2q34oHPgDCrSfo").build();

                Translate trService = options.getService();
                Translation translation = trService.translate(fromTxt,
                        Translate.TranslateOption.sourceLanguage("en"),
                        Translate.TranslateOption.targetLanguage("fr"));
                Log.d("Translated", translation.getTranslatedText());
                return translation.getTranslatedText();

        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("frst: ",result);
            from = result;
        }

    }

    private String translate(String textToTranslate) {

        Log.d("txt", textToTranslate);

            Translate trService = TranslateOptions.getDefaultInstance().getService();
            Translation translation = trService.translate(textToTranslate,
                    Translate.TranslateOption.sourceLanguage("en"),
                    Translate.TranslateOption.targetLanguage("fr"));
            Log.d("Translated", translation.getTranslatedText());
            return translation.getTranslatedText();

    }


}
