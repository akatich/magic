package tich.magic.listeners;

import android.media.SoundPool;
import android.view.GestureDetector;
import android.view.MotionEvent;
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
    private TextView score;
    private GameActivity gameActivity;
    private Player player;
    private SoundPool sp;
    private Animation healAnim;
    private Animation damageAnim;
    private Animation ripAnim;
    private ImageView healView;
    private ImageView damageView;
    private ImageView ripView;
    private int lifeOrPoison;
    public static int LIFE = 1;
    public static int POISON = 2;


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
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
            if (lifeOrPoison == LIFE)
                updateLife("+", 1);
            else
                updatePoison("+", 1);
            return false; // Bottom to top
        }  else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
            if (lifeOrPoison == LIFE)
                updateLife("-", 1);
            else
                updatePoison("-", 1);
            return false; // Top to bottom
        }
        return false;
    }

    public void updateLife(String operand, int addedLife)
    {
        int currentLife = Integer.parseInt(player.getScore().getText().toString());
        if (operand.equals("+")) {
            currentLife += addedLife;
            ((RelativeLayout.LayoutParams) healView.getLayoutParams()).addRule(RelativeLayout.ALIGN_LEFT, player.getName().getId());
            ((RelativeLayout.LayoutParams) healView.getLayoutParams()).addRule(RelativeLayout.ALIGN_RIGHT, player.getName().getId());
            ((RelativeLayout.LayoutParams) healView.getLayoutParams()).addRule(RelativeLayout.ALIGN_BOTTOM, player.getName().getId());
            healView.startAnimation(healAnim);
        }
        else {
            currentLife -= addedLife;
            ((RelativeLayout.LayoutParams) damageView.getLayoutParams()).addRule(RelativeLayout.ALIGN_LEFT, player.getName().getId());
            ((RelativeLayout.LayoutParams) damageView.getLayoutParams()).addRule(RelativeLayout.ABOVE, player.getName().getId());

            damageView.startAnimation(damageAnim);
        }
        player.getScore().setText(Integer.toString(currentLife));

        // RIP
        if (currentLife <= 0)
        {
            /*((RelativeLayout.LayoutParams)ripView.getLayoutParams()).addRule(RelativeLayout.ALIGN_LEFT, player.getName().getId());
            ((RelativeLayout.LayoutParams) ripView.getLayoutParams()).addRule(RelativeLayout.ALIGN_BOTTOM, player.getLifeDown().getId());
            ripView.startAnimation(ripAnim);*/
        }

    }

    public void updatePoison(String operand, int addedLife)
    {
        int currentLife = Integer.parseInt(player.getPoison().getText().toString());
        if (operand.equals("+")) {
            currentLife += addedLife;
            ((RelativeLayout.LayoutParams) healView.getLayoutParams()).addRule(RelativeLayout.ALIGN_LEFT, player.getPoison().getId());
            ((RelativeLayout.LayoutParams) healView.getLayoutParams()).addRule(RelativeLayout.ALIGN_RIGHT, player.getPoison().getId());
            ((RelativeLayout.LayoutParams) healView.getLayoutParams()).addRule(RelativeLayout.ALIGN_BOTTOM, player.getPoison().getId());
            healView.startAnimation(healAnim);
        }
        else {
            currentLife -= addedLife;
            ((RelativeLayout.LayoutParams) damageView.getLayoutParams()).addRule(RelativeLayout.ALIGN_LEFT, player.getPoison().getId());
            ((RelativeLayout.LayoutParams) damageView.getLayoutParams()).addRule(RelativeLayout.ABOVE, player.getPoison().getId());
            damageView.startAnimation(damageAnim);
        }
        player.getPoison().setText(Integer.toString(currentLife));

        // RIP
        if (currentLife <= 0)
        {
            /*((RelativeLayout.LayoutParams)ripView.getLayoutParams()).addRule(RelativeLayout.ALIGN_LEFT, player.getName().getId());
            ((RelativeLayout.LayoutParams) ripView.getLayoutParams()).addRule(RelativeLayout.ALIGN_BOTTOM, player.getLifeDown().getId());
            ripView.startAnimation(ripAnim);*/
        }

    }

}

