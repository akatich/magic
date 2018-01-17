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

public class DamageAnimationListener implements Animation.AnimationListener {

    TextView player;
    ImageView claws;
    SoundPool sp;
    int damageSound;
    GameActivity gameActivity;

    public DamageAnimationListener (TextView player, ImageView claws, SoundPool sp, GameActivity gameActivity)
    {
        this.player = player;
        this.claws = claws;
        this.sp = sp;
        this.gameActivity = gameActivity;
        damageSound = sp.load(gameActivity, R.raw.sabre, 1);
    }

    @Override
    public void onAnimationStart(Animation anim)
    {
        claws.setVisibility(View.VISIBLE);
        if (Preferences.getPreferences().hasSound())
            sp.play(damageSound, 1, 1, 1, 0, 1f);
        player.setTextColor(Color.RED);
    }

    @Override
    public void onAnimationRepeat(Animation anim)
    {}

    @Override
    public void onAnimationEnd(Animation anim)
    {
        claws.setVisibility(View.INVISIBLE);
        player.setTextColor(Color.DKGRAY);
    }
}
