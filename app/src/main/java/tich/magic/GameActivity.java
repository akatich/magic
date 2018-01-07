package tich.magic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v7.app.AppCompatActivity;

import tich.magic.model.Player;

public class GameActivity extends AppCompatActivity {

    private TextView txtSpeechInput;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private HashMap players;
    private SoundPool sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);


        //txtSpeechInput = findViewById(R.id.txt_speech_input);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            AudioAttributes attr = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            sp = new SoundPool.Builder()
                    .setAudioAttributes(attr)
                    .setMaxStreams(5)
                    .build();
        }
        else
        {
            sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }

        // retrieve players info
        players = new HashMap();
        String[] selectedPlayerNames = getIntent().getStringArrayExtra(MainActivity.SELECTED_PLAYERS);
        LinearLayout gameLayout = findViewById(R.id.game_layout);
        for (int i=0; i<selectedPlayerNames.length; i++)
        {
            String name = selectedPlayerNames[i];

            RelativeLayout relativeLayout = new RelativeLayout(this);
            relativeLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    RelativeLayout.LayoutParams.MATCH_PARENT, (float) 1.0));
            ((LinearLayout.LayoutParams)relativeLayout.getLayoutParams()).setMargins(20, 20, 20, 20);
            //relativeLayout.setBackgroundResource(R.drawable.parchemin);
            //relativeLayout.setBackgroundColor(Color.BLUE);

            TypedValue tv = new TypedValue();
            int actionBarHeight = 0;
            if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
            {
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
            }
            int tailleParcheminHaut = 60;

            WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics displaymetrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(displaymetrics);
            int screenWidth = displaymetrics.widthPixels;
            int screenHeight = displaymetrics.heightPixels;
            int layoutWidth = (screenWidth / selectedPlayerNames.length);
            int layoutHeight = screenHeight - actionBarHeight;


            ImageView parcheminHaut = new ImageView(this);
            parcheminHaut.setId(View.generateViewId());
            parcheminHaut.setBackgroundResource(R.drawable.parchemin_haut);
            parcheminHaut.setLayoutParams(new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    tailleParcheminHaut));
            //parcheminHaut.setBackgroundColor(Color.GREEN);

            ImageView parcheminMilieu = new ImageView(this);
            parcheminMilieu.setId(View.generateViewId());
            parcheminMilieu.setBackgroundResource(R.drawable.parchemin_milieu);
            parcheminMilieu.setLayoutParams(new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    layoutHeight - 2*tailleParcheminHaut - 100));
            //parcheminMilieu.setBackgroundColor(Color.WHITE);


            ImageView parcheminBas = new ImageView(this);
            parcheminBas.setId(View.generateViewId());
            parcheminBas.setBackgroundResource(R.drawable.parchemin_bas);
            parcheminBas.setLayoutParams(new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    tailleParcheminHaut));

            ((RelativeLayout.LayoutParams) parcheminMilieu.getLayoutParams()).addRule(RelativeLayout.BELOW, parcheminHaut.getId());
            ((RelativeLayout.LayoutParams) parcheminBas.getLayoutParams()).addRule(RelativeLayout.BELOW, parcheminMilieu.getId());

            relativeLayout.addView(parcheminHaut);
            relativeLayout.addView(parcheminMilieu);
            relativeLayout.addView(parcheminBas);

            ImageView stars = new ImageView(this);
            stars.setLayoutParams(new RelativeLayout.LayoutParams(
                    layoutWidth - 400,
                    RelativeLayout.LayoutParams.WRAP_CONTENT));
            stars.setId(View.generateViewId());
            stars.setImageResource(R.drawable.stars);
            stars.setVisibility(View.INVISIBLE);

            ImageView claws = new ImageView(this);
            claws.setLayoutParams(new RelativeLayout.LayoutParams(
                    200,
                    200));
            ((RelativeLayout.LayoutParams) claws.getLayoutParams()).leftMargin = -50;
            ((RelativeLayout.LayoutParams) claws.getLayoutParams()).bottomMargin = -50;
            claws.setId(View.generateViewId());
            claws.setImageResource(R.drawable.griffes);
            claws.setVisibility(View.INVISIBLE);

            Player p = new Player(this, name, getApplicationContext(), sp, selectedPlayerNames.length, layoutWidth, layoutHeight, stars, claws);

            p.attachToLayout(relativeLayout);
            relativeLayout.addView(claws);
            relativeLayout.addView(stars);

            gameLayout.addView(relativeLayout);

            players.put(name.toLowerCase(), p);
        }
    }



    public void updateLifeSpeak(View view)
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));
                    try {
                        processSpeech(result.get(0));
                    }
                    catch (Exception e)
                    {
                        txtSpeechInput.setText(result.get(0) + " - ERROR");
                    }
                }
                break;
            }

        }
    }

    private void processSpeech(String speech)
    {
        String[] parts = speech.split(" ");
        if (parts.length != 3)
        {
            return;
        }
        String name = spellName(parts[0].toLowerCase());
        String operand = parts[1];
        String addedLife = parts[2];

        // identify value
        if (addedLife.indexOf(".") > 0)
            addedLife = addedLife.substring(0, addedLife.indexOf("."));

        //txtSpeechInput.setText("n=" + name + ",p=" + player + ",o=" + operand + ",v=" + addedLife);

        ((Player)players.get(name)).updateLife(operand, Integer.parseInt(addedLife));
    }

    private String spellName(String spellName)
    {
        if (spellName.length()>2 && spellName.substring(0,2).equals("ri"))
            return "ritchi";
        else if (spellName.equals("6") ||
                spellName.equals("10") ||
                spellName.equals("teach") ||
                spellName.equals("kitsch") ||
                (spellName.length()>2 && spellName.substring(0,2).equals("ti")) )
            return "tich";
        else if (spellName.length()>2 && spellName.substring(0,2).equals("yo"))
            return "yoyo";
        else if (spellName.equals("mathis") ||
                spellName.equals("matisse"))
            return "mathys";

        return spellName;
    }

    public void returnMenu(View v)
    {
        Intent returnIntent = new Intent(this, MainActivity.class);
        startActivity(returnIntent);
    }

    public HashMap getPlayers() {
        return players;
    }

    public void setPlayers(HashMap players) {
        this.players = players;
    }

    public SoundPool getSp() {
        return sp;
    }

    public void setSp(SoundPool sp) {
        this.sp = sp;
    }
}
