package tich.magic.listeners;

import android.content.Context;
import android.media.SoundPool;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tich.magic.R;

public class RipAnimationListener implements Animation.AnimationListener {

    ImageView parcheminMilieu;
    SoundPool sp;
    int ripSound;

    public RipAnimationListener(ImageView parcheminMilieu, SoundPool sp, Context context)
    {
        this.parcheminMilieu = parcheminMilieu;
        this.sp = sp;
        ripSound = sp.load(context, R.raw.evil_laugh, 1);
    }

    @Override
    public void onAnimationStart(Animation anim)
    {
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
