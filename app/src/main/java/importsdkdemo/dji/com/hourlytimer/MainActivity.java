package importsdkdemo.dji.com.hourlytimer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    TextToSpeech t1;
    TextView tv1;
    EditText ed1;
    Button startButton;
    ToggleButton toggleButton;
    String toSpeak;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ed1 = (EditText) findViewById(R.id.editText);
        startButton = (Button) findViewById(R.id.button2);
        tv1 = (TextView) findViewById(R.id.tv1);
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
               // if(status != TextToSpeech.ERROR){
                    t1.setLanguage(Locale.ENGLISH);
               // }
            }
        });

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                  //  set_repeater();
                    tv1.setText("It's On");
                    toSpeak = "It's On!";
                    play_text(toSpeak);
                } else {
                    if(alarmMgr != null){
                        alarmMgr.cancel(alarmIntent);
                    }
                    tv1.setText("It's Off");
                    toSpeak = "It's Off!";
                    play_text(toSpeak);
                }
            }
        });


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread initBkgdThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        toSpeak = ed1.getText().toString();
                        play_text(toSpeak);
                    }
                });
                initBkgdThread.start();
            }
        });

    }
    private void set_repeater(){
        Calendar calRepeat = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("h:mm a");
        String theTime = format.format(calRepeat.getTime());

        calRepeat.setTimeInMillis(System.currentTimeMillis());
        calRepeat.set(Calendar.HOUR_OF_DAY, 14);
        t1.speak("The repeater is now set for" + theTime + " . and repeating every hour on the hour!", TextToSpeech.QUEUE_FLUSH, null);
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calRepeat.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);


    }

    private void play_text(String toSpeak){

        Calendar c = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("h:mm a");
        String theTime = format.format(c.getTime());
        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
    }
}
