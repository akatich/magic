package tich.magic.listeners;

import android.content.Context;
import android.graphics.Color;
import android.media.SoundPool;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import tich.magic.GameActivity;
import tich.magic.Preferences;
import tich.magic.R;

public class HealAnimationListener implements Animation.AnimationListener {

    TextView player;
    ImageView stars;
    SoundPool sp;
    int healSound;
    GameActivity gameActivity;

    public HealAnimationListener(TextView player, ImageView stars, SoundPool sp, GameActivity gameActivity)
    {
        this.player = player;
        this.stars = stars;
        this.sp = sp;
        this.gameActivity = gameActivity;
        healSound = sp.load(gameActivity, R.raw.harpe, 1);
    }

    @Override
    public void onAnimationStart(Animation anim)
    {
        stars.setVisibility(View.VISIBLE);
        if (Preferences.getPreferences().hasSound())
            sp.play(healSound, 1, 1, 1, 0, 1f);
        player.setTextColor(Color.GREEN);
    }

    @Override
    public void onAnimationRepeat(Animation anim)
    {}

    @Override
    public void onAnimationEnd(Animation anim)
    {
        stars.setVisibility(View.INVISIBLE);
        player.setTextColor(Color.DKGRAY);
    }
}
