package tich.magic;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashMap;
import java.util.Iterator;

import tich.magic.model.Player;
import tich.magic.model.Stats;

public class Preferences {

    private static SharedPreferences sharedPreferences = null;
    private static Preferences myPreferences = null;
    private final static String PLAYER_NAMES = "tich.magic.player_names";
    private final static String PLAYER_STATS = "tich.magic.player_stats";
    private final static String OPTION_SOUND = "tich.magic.sound";
    private final static String OPTION_POISON = "tich.magic.poison";
    private String playerNames;
    private boolean sound;
    private boolean poison;
    private HashMap playerStats;

    private Preferences()
    {
        playerNames = loadPlayerNames();
        sound = loadOptionSound();
        poison = loadOptionPoison();
        loadPlayerStats();
        //resetStats();
    }

    public static synchronized Preferences getPreferences()
    {
        if (myPreferences == null)
            myPreferences = new Preferences();
        return myPreferences;
    }

    public static Preferences getPreferences(Activity activity)
    {
        if (sharedPreferences == null)
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return getPreferences();
    }

    private String loadPlayerNames()
    {
        return sharedPreferences.getString(PLAYER_NAMES, "");
    }

    public String getPlayerNames()
    {
        return playerNames;
    }

    public void savePlayerNames(String playerNames)
    {
        this.playerNames = playerNames;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PLAYER_NAMES, playerNames);
        editor.commit();
    }

    private boolean loadOptionSound()
    {
        String optionSound = sharedPreferences.getString(OPTION_SOUND, "true");
        if (optionSound.equals("true"))
            return true;
        return false;
    }

    public void saveOptionSound(boolean hasSound)
    {
        sound = hasSound;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(OPTION_SOUND, String.valueOf(hasSound));
        editor.commit();
    }

    public boolean hasSound()
    {
        return sound;
    }

    private boolean loadOptionPoison()
    {
        String optionPoison = sharedPreferences.getString(OPTION_POISON, "false");
        if (optionPoison.equals("true"))
            return true;
        return false;
    }

    public void saveOptionPoison(boolean hasPoison)
    {
        poison = hasPoison;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(OPTION_POISON, String.valueOf(hasPoison));
        editor.commit();
    }

    public boolean hasPoison()
    {
        return poison;
    }

    private void loadPlayerStats()
    {
        // stats are stored under format : PLAYER_STATS_playerName = totalGamesPlayed;totalGamesWon
        playerStats = new HashMap();
        for (String playerName : getPlayerNames().split(";"))
        {
            String[] statsArray = sharedPreferences.getString(PLAYER_STATS + "_" + playerName.toLowerCase(), "0;0").split(";");
            Stats stats = new Stats();
            stats.setTotalGamesPlayed(Integer.parseInt(statsArray[0]));
            stats.setTotalGamesWon(Integer.parseInt(statsArray[1]));
            playerStats.put(playerName.toLowerCase(), stats);
        }
    }

    public HashMap getPlayerStats()
    {
        return playerStats;
    }

    public void updatePlayerStats(HashMap players)
    {
        Iterator iter = players.keySet().iterator();
        while (iter.hasNext())
        {
            String playerName = (String) iter.next();
            playerName = playerName.toLowerCase();
            Player p = (Player) players.get(playerName);

            Stats stats = (Stats) playerStats.get(playerName);
            if (stats == null)
                stats = new Stats();
            stats.setTotalGamesPlayed(stats.getTotalGamesPlayed() + 1);
            if (!p.isDead())
            {
                stats.setTotalGamesWon(stats.getTotalGamesWon() + 1);
            }
            savePlayerStats(playerName, stats);
        }
    }

    private void savePlayerStats(String playerName, Stats stats)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PLAYER_STATS + "_" + playerName, stats.getTotalGamesPlayed() + ";" + stats.getTotalGamesWon());
        editor.commit();
        playerStats.put(playerName, stats);
    }

    private void resetStats()
    {
        for (String playerName : getPlayerNames().split(";"))
        {
            String[] statsArray = sharedPreferences.getString(PLAYER_STATS + "_" + playerName.toLowerCase(), "0;0").split(";");
            Stats stats = new Stats();
            playerStats.put(playerName.toLowerCase(), stats);
        }
    }
}
