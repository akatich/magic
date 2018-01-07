package tich.magic;



import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import tich.magic.R;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private final String PLAYER_NAMES = "tich.magic.player_names";
    public final static String SELECTED_PLAYERS = "tich.magic.selected_players";
    private Typeface typeface = null;
    private SharedPreferences preferences = null;
    private String allPlayers = "";
    private ArrayList<String> allPlayersList = new ArrayList<String>();
    private LinearLayout playersLayout = null;
    private SoundPool sp;
    int selectSound;
    int playSound;
    int removeSound;
    private View.OnClickListener removePlayerListener = new View.OnClickListener()  {
        @Override
        public void onClick(View v)
        {
            sp.play(removeSound, 1, 1, 1, 0, 1f);

            TableRow r = (TableRow)v.getParent();

            String player = ((TextView)r.getChildAt(2)).getText().toString();
            allPlayersList.remove(player);
            allPlayers = "";
            for (String s : allPlayersList)
            {
                allPlayers += s + ";";
            }
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(PLAYER_NAMES, allPlayers);
            editor.commit();

            ((TableLayout)findViewById(R.id.all_players)).removeView(r);

        }
    };
    private View.OnClickListener selectPlayerListener = new View.OnClickListener()  {
        @Override
        public void onClick(View v)
        {
            sp.play(selectSound, 1, 1, 1, 0, 1f);

            TextView tv = (TextView) v;
            TableRow tr = (TableRow) tv.getParent();

            if (tr.getChildAt(0).getVisibility() == View.INVISIBLE)
            {
                tv.setTextColor(Color.parseColor("#007A3B"));
                tr.getChildAt(0).setVisibility(View.VISIBLE);
            }
            else
            {
                tv.setTextColor(Color.DKGRAY);
                tr.getChildAt(0).setVisibility(View.INVISIBLE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));


        typeface = ResourcesCompat.getFont(this, R.font.berkshire_swash);
        playersLayout = findViewById(R.id.all_players);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        allPlayers = preferences.getString(PLAYER_NAMES, "");
        for (String s : allPlayers.split(";"))
        {
            allPlayersList.add(s);
        }
        updateListPlayers();

        float   startGlowRadius = 6f,         // Glowing starts with this Intensity
                minGlowRadius   = 0f,         // Minimum Glowing Intensity
                maxGlowRadius   = 30f;        // Maximum Glowing Intensity
        GlowingText glowText = new GlowingText(
                this,           // Pass activity Object
                getBaseContext(),   // Context
                findViewById(R.id.button_play),           // TextView
                minGlowRadius,      // Minimum Glow Radius
                maxGlowRadius,      // Maximum Glow Radius
                startGlowRadius,    // Start Glow Radius - Increases to MaxGlowRadius then decreases to MinGlowRadius.
                Color.CYAN,         // Glow Color (int)
                1);                 // Glowing Transition Speed (Range of 1 to 10)  (fast ... slow)

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
        selectSound = sp.load(this, R.raw.select, 1);
        removeSound = sp.load(this, R.raw.remove, 1);
        playSound = sp.load(this, R.raw.gong, 1);

        // remove the focus on new player at start
        findViewById(R.id.rel_play_layout).requestFocus();
    }

    public void createPlayer(View view)
    {
        String newPlayer = ((EditText)findViewById(R.id.new_player)).getText().toString().trim();

        if (!newPlayer.equals("")) {
            SharedPreferences.Editor editor = preferences.edit();
            allPlayers += newPlayer + ";";

            editor.putString(PLAYER_NAMES, allPlayers);
            editor.commit();
            addPlayer(newPlayer);

            ((EditText)findViewById(R.id.new_player)).setText("");
        }
    }

    private void updateListPlayers()
    {
        String[] playersArray = allPlayers.split(";");
        for (String player : playersArray) {
            addPlayer(player);
        }
    }

    private void addPlayer(String player)
    {
        TableRow tr = new TableRow(this);
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        tr.setLayoutParams(params);
        tr.setGravity(Gravity.CENTER_VERTICAL);
        tr.setPadding(5, 5, 5, 5);

        ImageButton img = new ImageButton(this);
        img.setImageResource(R.drawable.greentick);
        img.setVisibility(View.INVISIBLE);
        img.setBackground(null);
        tr.addView(img, params);

        ImageView avatar = new ImageView(this);
        avatar.setImageResource(getResources().getIdentifier("avatar_icon_" + player.toLowerCase(), "drawable", getApplicationContext().getPackageName()) );
        avatar.setBackground(null);
        tr.addView(avatar, params);

        TextView tv = new TextView(this);
        tv.setText(player);
        tv.setTypeface(typeface);
        tv.setTextSize(16);
        tv.setTextColor(Color.DKGRAY);
        tv.setOnClickListener(selectPlayerListener);
        tv.setMinimumWidth(400);
        tr.addView(tv, params);

        ImageButton btn = new ImageButton(this);
        btn.setMinimumWidth(100);
        btn.setImageResource(R.drawable.redcross);
        btn.setBackgroundColor(Color.TRANSPARENT);
        btn.setOnClickListener(removePlayerListener);
        tr.addView(btn, params);

        playersLayout.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
    }

    public void play(View v)
    {
        ArrayList<String> selectedPlayers = new ArrayList<String>();
        for (int i=0; i<playersLayout.getChildCount(); i++)
        {
            TableRow tr = (TableRow) playersLayout.getChildAt(i);
            if (tr.getChildCount()==4 && tr.getChildAt(0).getVisibility() == View.VISIBLE)
            {
                TextView tv = (TextView) tr.getChildAt(2);
                selectedPlayers.add(tv.getText().toString());
            }
        }

        if (selectedPlayers.size() >= 2)
        {
            sp.play(playSound, 1, 1, 1, 0, 1f);

            Intent gameIntent = new Intent(this, GameActivity.class);
            String[] selectedPlayersArray = new String[selectedPlayers.size()];
            selectedPlayersArray = selectedPlayers.toArray(selectedPlayersArray);
            gameIntent.putExtra(SELECTED_PLAYERS, selectedPlayersArray);
            startActivity(gameIntent);
        }
    }
}