package tich.magic.listeners;

import android.content.Context;
import android.media.SoundPool;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tich.magic.GameActivity;
import tich.magic.Preferences;
import tich.magic.R;

public class RipAnimationListener implements Animation.AnimationListener {

    ImageView parcheminMilieu;
    SoundPool sp;
    int ripSound;
    GameActivity gameActivity;

    public RipAnimationListener(ImageView parcheminMilieu, SoundPool sp, GameActivity gameActivity)
    {
        this.parcheminMilieu = parcheminMilieu;
        this.sp = sp;
        this.gameActivity = gameActivity;
        ripSound = sp.load(gameActivity, R.raw.evil_laugh, 1);
    }

    @Override
    public void onAnimationStart(Animation anim)
    {
        if (Preferences.getPreferences().hasSound())
            sp.play(ripSound, 1, 1, 1, 0, 1f);
    }

    @Override
    public void onAnimationRepeat(Animation anim)
    {}

    @Override
    public void onAnimationEnd(Animation anim)
    {
        parcheminMilieu.setVisibility(View.INVISIBLE);
    }
}
