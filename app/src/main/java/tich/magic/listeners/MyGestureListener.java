package tich.magic.listeners;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.media.SoundPool;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tich.magic.GameActivity;
import tich.magic.R;
import tich.magic.model.Player;

public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 25;
    private GameActivity gameActivity;
    private Player player;
    private SoundPool sp;
    private Animation healAnim;
    private Animation damageAnim;
    private Animation rollParcheminMilieuAnim;
    private Animation openParcheminMilieuAnim;
    private ImageView healView;
    private ImageView damageView;
    private int lifeOrPoison;
    public static int LIFE = 1;
    public static int POISON = 2;
    public static int NAME = 3;


    public MyGestureListener(GameActivity gameActivity, Player player, SoundPool sp, ImageView stars, ImageView claws, int lifeOrPoison){
        super();
        this.gameActivity = gameActivity;
        this.player = player;
        this.sp = sp;
        this.lifeOrPoison = lifeOrPoison;

        healView = stars;
        healAnim = AnimationUtils.loadAnimation(gameActivity, R.anim.heal);
        healAnim.setAnimationListener(new HealAnimationListener(player.getName(), healView, sp, gameActivity));

        damageView = claws;
        damageAnim = AnimationUtils.loadAnimation(gameActivity, R.anim.damage);
        damageAnim.setAnimationListener(new DamageAnimationListener(player.getName(), damageView, sp, gameActivity));

        rollParcheminMilieuAnim = AnimationUtils.loadAnimation(gameActivity, R.anim.roll_parchemin_milieu);
        rollParcheminMilieuAnim.setAnimationListener(new RipAnimationListener(player.getParcheminMilieu(), sp, gameActivity));

        openParcheminMilieuAnim = AnimationUtils.loadAnimation(gameActivity, R.anim.open_parchemin_milieu);
        openParcheminMilieuAnim.setAnimationListener(new ResurrectAnimationListener(player.getParcheminMilieu(), sp, gameActivity));
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY)
        {
            if (lifeOrPoison == LIFE)
                updateLife("+", 1);
            else if (lifeOrPoison == POISON)
                updatePoison("+", 1);
            return false; // Bottom to top
        }
        else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY)
        {
            if (lifeOrPoison == LIFE)
                updateLife("-", 1);
            else if (lifeOrPoison == POISON)
                updatePoison("-", 1);
            else if (lifeOrPoison == NAME)
            {
                // déplier le parchemin
                openParchemin();
            }
            return false; // Top to bottom
        }
        return false;
    }

    private void openParchemin()
    {
        player.getParcheminMilieu().startAnimation(openParcheminMilieuAnim);

        ObjectAnimator avatarAnimator = ObjectAnimator.ofFloat ( player.getAvatar() , "alpha" , 1);
        avatarAnimator.setDuration(2000);
        avatarAnimator.start();

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat ( player.getParcheminBas() , "y" , player.getParcheminHaut().getHeight() + player.getParcheminMilieu().getHeight());
        objectAnimator.setDuration(2000);
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {
                float animatedValue = (float)updatedAnimation.getAnimatedValue();

                int poisonTop = ((RelativeLayout.LayoutParams)player.getPoison().getLayoutParams()).topMargin + ((RelativeLayout.LayoutParams)player.getPoison().getLayoutParams()).height/2;
                if (animatedValue >= poisonTop) {
                    player.getPoison().setVisibility(View.VISIBLE);
                    player.getPoisonImage().setVisibility(View.VISIBLE);
                }

                int scoreTop = ((RelativeLayout.LayoutParams)player.getScore().getLayoutParams()).topMargin + ((RelativeLayout.LayoutParams)player.getScore().getLayoutParams()).height/2;
                if (animatedValue >= scoreTop) {
                    player.getScore().setVisibility(View.VISIBLE);
                }

            }
        });
        objectAnimator.start();

        ObjectAnimator nameAnimator = ObjectAnimator.ofFloat ( player.getName() , "y" , player.getParcheminHaut().getHeight() + player.getParcheminMilieu().getHeight() - 100);
        nameAnimator.setDuration(2000);
        nameAnimator.start();

        player.setDead(false);
    }

    public void updateLife(String operand, int addedLife)
    {
        int currentLife = Integer.parseInt(player.getScore().getText().toString());
        if (operand.equals("+")) {
            currentLife += addedLife;
            ((RelativeLayout.LayoutParams) healView.getLayoutParams()).addRule(RelativeLayout.ALIGN_LEFT, player.getName().getId());
            ((RelativeLayout.LayoutParams) healView.getLayoutParams()).addRule(RelativeLayout.ALIGN_RIGHT, player.getName().getId());
            ((RelativeLayout.LayoutParams) healView.getLayoutParams()).addRule(RelativeLayout.ALIGN_BOTTOM, player.getName().getId());
            startHealAnimation();
        }
        else {
            currentLife -= addedLife;
            ((RelativeLayout.LayoutParams) damageView.getLayoutParams()).addRule(RelativeLayout.ALIGN_LEFT, player.getName().getId());
            ((RelativeLayout.LayoutParams) damageView.getLayoutParams()).addRule(RelativeLayout.ABOVE, player.getName().getId());
            startDamageAnimation();
        }
        player.getScore().setText(Integer.toString(currentLife));

        // RIP
        if (currentLife <= 0)
        {
            die();
        }

    }

    public void startHealAnimation()
    {
        healView.startAnimation(healAnim);
    }

    public void startDamageAnimation()
    {
        damageView.startAnimation(damageAnim);
    }

    public void updatePoison(String operand, int addedPoison)
    {
        int currentPoison = Integer.parseInt(player.getPoison().getText().toString());
        if (operand.equals("+")) {
            currentPoison += addedPoison;
            ((RelativeLayout.LayoutParams) healView.getLayoutParams()).addRule(RelativeLayout.ALIGN_LEFT, player.getPoison().getId());
            ((RelativeLayout.LayoutParams) healView.getLayoutParams()).addRule(RelativeLayout.ALIGN_RIGHT, player.getPoison().getId());
            ((RelativeLayout.LayoutParams) healView.getLayoutParams()).addRule(RelativeLayout.ALIGN_BOTTOM, player.getPoison().getId());
            damageView.startAnimation(damageAnim);
        }
        else {
            currentPoison -= addedPoison;
            ((RelativeLayout.LayoutParams) damageView.getLayoutParams()).addRule(RelativeLayout.ALIGN_LEFT, player.getPoison().getId());
            ((RelativeLayout.LayoutParams) damageView.getLayoutParams()).addRule(RelativeLayout.ABOVE, player.getPoison().getId());
            healView.startAnimation(healAnim);
        }
        player.getPoison().setText(Integer.toString(currentPoison));

        // RIP
        if (currentPoison >= 10)
        {
            die();
        }

    }

    public void die()
    {
        player.getParcheminMilieu().startAnimation(rollParcheminMilieuAnim);

        ObjectAnimator avatarAnimator = ObjectAnimator.ofFloat ( player.getAvatar() , "alpha" , 0);
        avatarAnimator.setDuration(2000);
        avatarAnimator.start();

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat ( player.getParcheminBas() , "y" , player.getParcheminMilieu().getY());
        objectAnimator.setDuration(2000);
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {
                float animatedValue = (float)updatedAnimation.getAnimatedValue();

                int poisonTop = ((RelativeLayout.LayoutParams)player.getPoison().getLayoutParams()).topMargin + ((RelativeLayout.LayoutParams)player.getPoison().getLayoutParams()).height/2;
                if (animatedValue <= poisonTop) {
                    player.getPoison().setVisibility(View.INVISIBLE);
                    player.getPoisonImage().setVisibility(View.INVISIBLE);
                }

                int scoreTop = ((RelativeLayout.LayoutParams)player.getScore().getLayoutParams()).topMargin + ((RelativeLayout.LayoutParams)player.getScore().getLayoutParams()).height/2;
                if (animatedValue <= scoreTop) {
                    player.getScore().setVisibility(View.INVISIBLE);
                }

            }
        });
        objectAnimator.start();

        ObjectAnimator nameAnimator = ObjectAnimator.ofFloat ( player.getName() , "y" , player.getParcheminHaut().getY());
        nameAnimator.setDuration(2000);
        nameAnimator.start();

        player.setDead(true);
    }
}

