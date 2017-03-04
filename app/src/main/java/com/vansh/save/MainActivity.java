package com.vansh.save;

import android.Manifest;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.Locale;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView txtSpeechInput;
    private ImageView textSpeach;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private SoundMeter mSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensor = new SoundMeter();
        requestCameraPermission();

        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        textSpeach = (ImageView) findViewById(R.id.btnSpeak);

        textSpeach.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();

            }
        });

    }
    public void requestCameraPermission() {
        // Camera permission has not been granted yet. Request it directly.
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{ Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
    }

    // Showing google speech input dialog

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Hi speak something");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    public void recordClap() {
        mSensor.start();

        double startAmplitude = mSensor.getAmplitude();
        Log.d("StartAmp", "starting amplitude: " + startAmplitude);
        boolean ampDiff;
        do {
            Log.d("StartAmp", "waiting while taking in input");
            double finishAmplitude = 0;
            try {
                finishAmplitude = mSensor.getAmplitude();
            } catch (RuntimeException re) {
                Log.e("StartAmp", "unable to get the max amplitude " + re);
            }
            ampDiff = checkAmplitude(startAmplitude, finishAmplitude);
            Log.d("star", "finishing amp: " + finishAmplitude + " difference: " + ampDiff);
        }
        while (ampDiff);

        Toast.makeText(this, "Scream Detected", Toast.LENGTH_SHORT).show();
    }

    private boolean checkAmplitude(double startAmplitude, double finishAmplitude)
    {
        double ampDiff = finishAmplitude - startAmplitude;
        Log.d("diff", "amplitude difference " + ampDiff);
        return (ampDiff <= 0);
    }

    // Receiving speech input

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));
                }
                break;
            }

        }
    }


}