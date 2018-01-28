package tich.magic.listeners;

import android.graphics.Color;
import android.media.SoundPool;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import tich.magic.GameActivity;
import tich.magic.MainActivity;
import tich.magic.Preferences;
import tich.magic.R;
import tich.magic.model.Troll;

public class PlayAnimationListener implements Animation.AnimationListener {

    private MainActivity activity;
    private SoundPool sp;
    private int playSound;
    private int gameMode;

    public PlayAnimationListener(MainActivity activity, SoundPool sp, int gameMode)
    {
        this.activity = activity;
        this.sp = sp;
        this.gameMode = gameMode;
        playSound = sp.load(activity, R.raw.gong, 1);
    }

    @Override
    public void onAnimationStart(Animation anim)
    {
        if (Preferences.getPreferences().hasSound())
            sp.play(playSound, 1, 1, 1, 0, 1f);
    }

    @Override
    public void onAnimationRepeat(Animation anim)
    {}

    @Override
    public void onAnimationEnd(Animation anim)
    {
        activity.play(gameMode);
    }
}
