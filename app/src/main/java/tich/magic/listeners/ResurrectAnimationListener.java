package tich.magic.listeners;

import android.media.SoundPool;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

import tich.magic.GameActivity;
import tich.magic.Preferences;
import tich.magic.R;

public class ResurrectAnimationListener implements Animation.AnimationListener {

    ImageView parcheminMilieu;
    SoundPool sp;
    //int resurrectSound;
    GameActivity gameActivity;

    public ResurrectAnimationListener(ImageView parcheminMilieu, SoundPool sp, GameActivity gameActivity)
    {
        this.parcheminMilieu = parcheminMilieu;
        this.sp = sp;
        this.gameActivity = gameActivity;
        //resurrectSound = sp.load(gameActivity, R.raw.evil_laugh, 1);
    }

    @Override
    public void onAnimationStart(Animation anim)
    {
        //if (Preferences.getPreferences().hasSound())
        //    sp.play(resurrectSound, 1, 1, 1, 0, 1f);
    }

    @Override
    public void onAnimationRepeat(Animation anim)
    {}

    @Override
    public void onAnimationEnd(Animation anim)
    {
        parcheminMilieu.setVisibility(View.VISIBLE);
    }
}
