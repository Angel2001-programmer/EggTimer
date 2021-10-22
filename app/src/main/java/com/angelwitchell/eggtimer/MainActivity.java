package com.angelwitchell.eggtimer;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.angelwitchell.eggtimer.databinding.ActivityMainBinding;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityMainBinding binding;
    private static final String TAG = "MainActivity";
    private static final String timerState = "TIMER_STATE";
    private static final String textState = "TEXT_STATE";
    private CountDownTimer mCountDownTimer;
    long millis = 0;
    Context mContext;
    String item0;
    String item1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        String[] list = getResources().getStringArray(R.array.values);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        binding.sprMinutes.setAdapter(arrayAdapter);
        binding.sprSeconds.setAdapter(arrayAdapter);
        binding.startButton.setOnClickListener(this);
        binding.stopButton.setOnClickListener(this);

        binding.sprMinutes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                item0 = adapterView.getItemAtPosition(i).toString();
                Log.d(TAG, "onItemSelected: " + item0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.sprSeconds.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> view, View v, int k, long l) {
                item1 = view.getItemAtPosition(k).toString();
                Log.d(TAG, "onItemSelected: " + item1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (savedInstanceState != null) {
            long saveinfo = savedInstanceState.getLong(timerState);
            String textSave = savedInstanceState.getString(textState);
            Log.d(TAG, "onCreate: " + saveinfo + textSave);
            Timer(saveinfo);
        }
    }

    private void Timer(long timeleft){
        mCountDownTimer = new CountDownTimer(timeleft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                millis = millisUntilFinished;

                String ms = String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millis) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(millis) % 60);
                binding.textView.setText(ms);
                binding.imageView.setImageResource(R.drawable.egg);
            }

            @Override
            public void onFinish() {
                MediaPlayer mp = MediaPlayer.create(mContext, R.raw.roosternoise);
                binding.textView.setText(R.string.Timer0);
                binding.imageView.setImageResource(R.drawable.chick);
                mCountDownTimer.cancel();
                mp.start();
            }
        }.start();
    }

    private void ConvertValue() {
            int mins = Integer.parseInt(item0);
            int secs = Integer.parseInt(item1);

            Log.d(TAG, "ConvertValue: " + mins + ":" + secs);

            long minutes = TimeUnit.MINUTES.toMillis(mins);
            long seconds = TimeUnit.SECONDS.toMillis(secs);

            millis = minutes + seconds;
            Log.d(TAG, "ConvertValue: " + millis);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(textState, binding.textView.getText().toString());
        outState.putLong(timerState, millis);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startButton:
                if(mCountDownTimer != null){
                    mCountDownTimer.cancel();
                }
                ConvertValue();
                if(millis == 0){
                    Toast.makeText(this, "Choose a valid amount for either minute or second", Toast.LENGTH_LONG).show();
                } else {
                    Timer(millis);
                }
                break;

            case R.id.stopButton:
                mCountDownTimer.onFinish();
                break;

            default:
                Log.d(TAG, "onClick: " + "Buttons are not working");
                Toast.makeText(this, "Buttons are not working, \n please report to the issue to GooglePlay store", Toast.LENGTH_SHORT).show();
            break;
        }
    }
}