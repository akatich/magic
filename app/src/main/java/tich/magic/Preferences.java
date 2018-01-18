package tich.magic;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {

    private static SharedPreferences sharedPreferences = null;
    private static Preferences myPreferences = null;
    private final static String PLAYER_NAMES = "tich.magic.player_names";
    private final static String OPTION_SOUND = "tich.magic.sound";
    private final static String OPTION_POISON = "tich.magic.poison";
    private String playerNames;
    private boolean sound;
    private boolean poison;

    private Preferences()
    {
        playerNames = loadPlayerNames();
        sound = loadOptionSound();
        poison = loadOptionPoison();
    }

    public static synchronized Preferences getPreferences()
    {
        if (myPreferences == null)
            myPreferences = new Preferences();
        return myPreferences;
    }

    public static Preferences getPreferences(Activity activity)
    {
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
}
