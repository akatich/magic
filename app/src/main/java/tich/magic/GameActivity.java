package tich.magic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;

import android.animation.ObjectAnimator;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v7.app.AppCompatActivity;
import android.widget.ViewFlipper;

import tich.magic.listeners.ResurrectAnimationListener;
import tich.magic.model.Player;
import tich.magic.model.Troll;

public class GameActivity extends AppCompatActivity {

    private final int REQ_CODE_SPEECH_INPUT = 100;
    private HashMap players;
    private SoundPool sp;
    private ViewFlipper firstPlayer;
    public int flipSound;
    private int playSound;
    private int screenWidth;
    private int screenHeight;
    private int layoutWidth;
    private int layoutHeight;
    private int startLife = 20;
    private TextView txtSpeech;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getDrawable(R.drawable.barre_violette));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initSound();

        // retrieve players info
        players = new HashMap();
        String[] selectedPlayerNames = getIntent().getStringArrayExtra(MainActivity.SELECTED_PLAYERS);
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
        int tailleParcheminHaut = 60;

        if (isTroll())
        {
            Collections.shuffle(Arrays.asList(selectedPlayerNames));
            startLife = 30;
        }
        else if (is5pv())
        {
            startLife = 5;
        }

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displaymetrics);
        screenWidth = displaymetrics.widthPixels;
        screenHeight = displaymetrics.heightPixels;
        layoutWidth = (screenWidth / selectedPlayerNames.length);
        if (isTroll())
            layoutWidth = layoutWidth * 2;
        layoutHeight = screenHeight - actionBarHeight;

        firstPlayer = new ViewFlipper(this);
        firstPlayer.setLayoutParams(new RelativeLayout.LayoutParams(
                screenWidth / 3,
                new Double(layoutHeight * 0.9).intValue()));
        ((RelativeLayout.LayoutParams) firstPlayer.getLayoutParams()).addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        firstPlayer.setAlpha(0);

        RelativeLayout parentGameLayout = findViewById(R.id.parent_game_layout);
        LinearLayout gameLayout = findViewById(R.id.game_layout);

        for (int i=0; i<selectedPlayerNames.length; i++)
        {
            RelativeLayout relativeLayout = new RelativeLayout(this);
            relativeLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    RelativeLayout.LayoutParams.MATCH_PARENT, (float) 1.0));
            ((LinearLayout.LayoutParams)relativeLayout.getLayoutParams()).setMargins(20, 20, 20, 20);

            ImageView parcheminHaut = new ImageView(this);
            parcheminHaut.setId(View.generateViewId());
            parcheminHaut.setBackgroundResource(R.drawable.parchemin_haut);
            parcheminHaut.setLayoutParams(new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    tailleParcheminHaut));

            ImageView parcheminMilieu = new ImageView(this);
            parcheminMilieu.setId(View.generateViewId());
            parcheminMilieu.setBackgroundResource(R.drawable.parchemin_milieu);
            parcheminMilieu.setLayoutParams(new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    layoutHeight - 2*tailleParcheminHaut - 100));


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

            if (!isTroll())
            {
                String name = selectedPlayerNames[i];
                Player p = new Player(this, name, getApplicationContext(), sp, selectedPlayerNames.length, layoutWidth, layoutHeight, parcheminHaut, parcheminMilieu, parcheminBas, stars, claws);
                players.put(name.toLowerCase(), p);
                p.attachToLayout(relativeLayout);
            }
            else
            {
                String name1 = selectedPlayerNames[i++];
                String name2 = selectedPlayerNames[i];
                Player p = new Troll(this, name1 + ";" + name2, getApplicationContext(), sp, selectedPlayerNames.length, layoutWidth, layoutHeight, parcheminHaut, parcheminMilieu, parcheminBas, stars, claws);
                players.put(name1.toLowerCase(), p);
                players.put(name2.toLowerCase(), p);
                p.attachToLayout(relativeLayout);
            }


            relativeLayout.addView(claws);
            relativeLayout.addView(stars);

            gameLayout.addView(relativeLayout);
        }

        parentGameLayout.addView(firstPlayer);

        txtSpeech = new TextView(this);
        txtSpeech.setId(View.generateViewId());
        txtSpeech.setText("test");
        txtSpeech.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
        txtSpeech.setBackgroundColor(Color.WHITE);
        //parentGameLayout.addView(txtSpeech); // <-- for debug only
    }

    public boolean isTroll()
    {
        return Integer.parseInt(getIntent().getStringExtra(MainActivity.GAME_MODE)) == MainActivity.TROLL;
    }

    public boolean is5pv()
    {
        return Integer.parseInt(getIntent().getStringExtra(MainActivity.GAME_MODE)) == MainActivity.PV5;
    }

    private void buildViewFlipperToChooseFirstPlayer()
    {
        firstPlayer.removeAllViews();

        HashMap chosenPlayers = new HashMap();
        Iterator iter = players.keySet().iterator();
        while (iter.hasNext())
        {
            String playerName = (String) iter.next();
            Player p = (Player) players.get(playerName);

            if (!p.isDead() && !chosenPlayers.containsKey(p))
            {
                chosenPlayers.put(p, null);
                // add player data to firstPlayer view
                RelativeLayout playerNameAndAvatar = new RelativeLayout(this);
                Random rand = new Random();
                int r = rand.nextInt(255);
                int g = rand.nextInt(255);
                int b = rand.nextInt(255);
                GradientDrawable shape = new GradientDrawable();
                shape.setCornerRadius(35);
                shape.setColors(new int[]{
                        Color.WHITE,
                        Color.rgb(r, g, b)
                });
                shape.setGradientType(GradientDrawable.RADIAL_GRADIENT);
                shape.setGradientRadius(screenWidth / 4);
                shape.setShape(GradientDrawable.RECTANGLE);
                shape.setStroke(4, Color.BLACK);
                playerNameAndAvatar.setBackground(shape);

                if (!isTroll()) {
                    ImageView playerAvatar = new ImageView(this);
                    playerAvatar.setId(View.generateViewId());
                    playerAvatar.setLayoutParams(new RelativeLayout.LayoutParams(
                            (int) (layoutWidth * 0.6),
                            (int) (layoutWidth * 0.6)));
                    playerAvatar.setImageResource(getResources().getIdentifier("avatar_" + p.getName().getText().toString().toLowerCase(), "drawable", getApplicationContext().getPackageName()));
                    ((RelativeLayout.LayoutParams) playerAvatar.getLayoutParams()).addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                    playerNameAndAvatar.addView(playerAvatar);
                }
                else {
                    ImageView avatarSeparator = new ImageView(this);
                    avatarSeparator.setId(View.generateViewId());
                    avatarSeparator.setLayoutParams(new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT));
                    ((RelativeLayout.LayoutParams) avatarSeparator.getLayoutParams()).addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                    avatarSeparator.setImageResource(this.getResources().getIdentifier("avatar_separator", "drawable", getApplicationContext().getPackageName()));
                    avatarSeparator.setAlpha(60);
                    ((RelativeLayout.LayoutParams) avatarSeparator.getLayoutParams()).width = (int) (layoutWidth * 0.1);
                    ((RelativeLayout.LayoutParams) avatarSeparator.getLayoutParams()).height = (int) (layoutWidth * 0.3);
                    playerNameAndAvatar.addView(avatarSeparator);

                    ImageView avatar = new ImageView(this);
                    avatar.setId(View.generateViewId());
                    avatar.setLayoutParams(new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT));
                    ((RelativeLayout.LayoutParams) avatar.getLayoutParams()).addRule(RelativeLayout.LEFT_OF, avatarSeparator.getId());
                    ((RelativeLayout.LayoutParams) avatar.getLayoutParams()).addRule(RelativeLayout.ALIGN_TOP, avatarSeparator.getId());
                    avatar.setImageResource(getResources().getIdentifier("avatar_" + ((Troll)p).getPlayerNamesArray()[0].toLowerCase(), "drawable", getApplicationContext().getPackageName()));
                    avatar.setAlpha(60);
                    ((RelativeLayout.LayoutParams) avatar.getLayoutParams()).width = (int) (layoutWidth * 0.3);
                    ((RelativeLayout.LayoutParams) avatar.getLayoutParams()).height = ((RelativeLayout.LayoutParams) avatar.getLayoutParams()).width;
                    playerNameAndAvatar.addView(avatar);

                    ImageView avatar2 = new ImageView(this);
                    avatar2.setId(View.generateViewId());
                    avatar2.setLayoutParams(new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT));
                    ((RelativeLayout.LayoutParams) avatar2.getLayoutParams()).addRule(RelativeLayout.RIGHT_OF, avatarSeparator.getId());
                    ((RelativeLayout.LayoutParams) avatar2.getLayoutParams()).addRule(RelativeLayout.ALIGN_TOP, avatarSeparator.getId());
                    avatar2.setImageResource(getResources().getIdentifier("avatar_" + ((Troll)p).getPlayerNamesArray()[1].toLowerCase(), "drawable", getApplicationContext().getPackageName()));
                    avatar2.setAlpha(60);
                    ((RelativeLayout.LayoutParams) avatar2.getLayoutParams()).width = (int) (layoutWidth * 0.3);
                    ((RelativeLayout.LayoutParams) avatar2.getLayoutParams()).height = ((RelativeLayout.LayoutParams) avatar2.getLayoutParams()).width;
                    playerNameAndAvatar.addView(avatar2);
                }

                TextView name = new TextView(this);
                name.setId(View.generateViewId());
                name.setText(p.getName().getText().toString());
                name.setLayoutParams(new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT));
                name.setGravity(Gravity.CENTER_HORIZONTAL);
                name.setTypeface(ResourcesCompat.getFont(this, R.font.berkshire_swash));
                name.setTextSize(50);
                name.setTextColor(Color.DKGRAY);
                ((RelativeLayout.LayoutParams) name.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                playerNameAndAvatar.addView(name);

                firstPlayer.addView(playerNameAndAvatar);
            }
        }
    }

    private void initSound()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            AudioAttributes attr = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            sp = new SoundPool.Builder()
                    .setAudioAttributes(attr)
                    .setMaxStreams(10)
                    .build();
        }
        else
        {
            sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }
        flipSound = sp.load(this, R.raw.select, 1);
        playSound = sp.load(this, R.raw.gong, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //ajoute les entrées de menu_test à l'ActionBar
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //gère le click sur une action de l'ActionBar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_speak:
                updateLifeSpeak();
                return true;
            case R.id.action_first_player:
                chooseFirstPlayer();
                return true;
            case R.id.action_options:
                changeOptions();
                return true;
            case R.id.action_stats:
                displayStats();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void displayStats()
    {
        StatsDialog dialog = new StatsDialog(this);
        dialog.show(getSupportFragmentManager(), "sd");
    }


    public void updateLifeSpeak()
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
                    txtSpeech.setText(result.get(0));
                    try {
                        processSpeech(result.get(0));
                    }
                    catch (Exception e)
                    {

                    }
                }
                break;
            }
        }
    }

    private void processSpeech(String speech)
    {
        String[] parts = speech.split(" ");
        if (parts.length == 2)
        {
            // "tuer machin"
            String action = parts[0].toLowerCase();
            if (action.equals("tuer"))
            {
                String name = spellName(parts[1].toLowerCase());
                ((Player) players.get(name)).die();
                return;
            }
        }
        else if (parts.length == 3)
        {
            // "machin - 3." ou "machin + 5."

            if (parts[0].equals("tu") && parts[1].equals("es"))
            {
                // "tu es machin" au lieu de "tuer machin"
                String name = spellName(parts[2].toLowerCase());
                ((Player) players.get(name)).die();
                return;
            }

            String name = spellName(parts[0].toLowerCase());
            String operand = parts[1];
            String addedLife = parts[2];

            // identify value
            if (addedLife.indexOf(".") > 0)
                addedLife = addedLife.substring(0, addedLife.indexOf("."));

            ((Player) players.get(name)).getScoreGestureListener().updateLife(operand, Integer.parseInt(addedLife));
        }
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
        else if (spellName.equals("huriaux") ||
                (spellName.length()>2 && spellName.substring(0,2).equals("yo")) )
            return "yoyo";
        else if (spellName.equals("mathis") ||
                spellName.equals("matisse"))
            return "mathys";

        return spellName;
    }

    public void chooseFirstPlayer()
    {
        buildViewFlipperToChooseFirstPlayer();

        firstPlayer.setAlpha(1);
        firstPlayer.setFlipInterval(500);
        firstPlayer.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.flip_in));
        firstPlayer.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.flip_out));
        firstPlayer.getOutAnimation().setAnimationListener(new Animation.AnimationListener() {

            int flipCount = 0;
            int flipStop = (int) (6 + Math.random()*10);

            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                flipCount++;
                if (Preferences.getPreferences().hasSound())
                    sp.play(flipSound, 1, 1, 1, 0, 1f);
                if (flipCount > flipStop) {
                    firstPlayer.stopFlipping();
                    if (Preferences.getPreferences().hasSound())
                        sp.play(playSound, 1, 1, 1, 0, 1f);
                    ObjectAnimator flipOutAnimator = ObjectAnimator.ofFloat ( firstPlayer , "alpha" , 0);
                    flipOutAnimator.setDuration(2000);
                    flipOutAnimator.setStartDelay(3000);
                    flipOutAnimator.start();
                }
            }

        });
        firstPlayer.startFlipping();
    }

    public void changeOptions()
    {
        OptionsDialog dialog = new OptionsDialog(this);
        dialog.show(getSupportFragmentManager(), "od");
    }

    public void displayPoison()
    {
        Iterator iter = players.values().iterator();
        while (iter.hasNext()) {
            Player p = (Player) iter.next();
            p.displayPoison();
        }
    }

    public void hidePoison()
    {
        Iterator iter = players.values().iterator();
        while (iter.hasNext()) {
            Player p = (Player) iter.next();
            p.hidePoison();
        }
    }

    public void resetLife(int life)
    {
        Iterator iter = players.values().iterator();
        while (iter.hasNext()) {
            Player p = (Player) iter.next();
            p.resetLife(life);
        }
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

    public ViewFlipper getFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(ViewFlipper firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public int getStartLife() {
        return startLife;
    }

    public void setStartLife(int startLife) {
        this.startLife = startLife;
    }
}
