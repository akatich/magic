package tich.magic.listeners;


import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.SoundPool;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import tich.magic.GameActivity;
import tich.magic.R;
import tich.magic.model.Player;

public class LifeUpListener implements View.OnClickListener {

    private GameActivity gameActivity;
    private Player player;
    private Animation healAnim;
    private Animation damageAnim;
    private Animation ripAnim;
    private ImageView healView;
    private ImageView damageView;
    private ImageView ripView;
    private boolean rotated = false;


    public LifeUpListener(GameActivity gameActivity, Player player, Context context, SoundPool sp)
    {
        this.gameActivity = gameActivity;
        this.player = player;
        this.rotated = false;

        //healView = gameActivity.findViewById(R.id.stars);
        healView.getLayoutParams().width = 300;
        healAnim = AnimationUtils.loadAnimation(context, R.anim.heal);
        healAnim.setAnimationListener(new HealAnimationListener(player.getName(), healView, sp, gameActivity));

        //damageView = gameActivity.findViewById(R.id.claws);
        damageView.getLayoutParams().width = 300;
        damageAnim = AnimationUtils.loadAnimation(context, R.anim.damage);
        damageAnim.setAnimationListener(new DamageAnimationListener(player.getName(), damageView, sp, gameActivity));

        //ripView = gameActivity.findViewById(R.id.rip);
        /*ripView = new ImageView(gameActivity);
        ((RelativeLayout)gameActivity.findViewById(R.id.game_layout)).addView(ripView);
        ripView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        ripView.setImageResource(R.drawable.rip);
        ripView.setVisibility(View.INVISIBLE);
        if (gameActivity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE) {
            ripView.getLayoutParams().width = 350;
        }
        if (rotated) {
            ripView.setRotation(180);
        }
        ripAnim = AnimationUtils.loadAnimation(context, R.anim.rip);
        ripAnim.setAnimationListener(new RipAnimationListener(player.getName(), ripView, sp, context));*/
    }

    @Override
    public void onClick(View v)
    {
        updateLife((String)v.getTag(), 1);
    }

    public void updateLife(String operand, int addedLife)
    {
        int currentLife = Integer.parseInt(player.getScore().getText().toString());
        if (operand.equals("+")) {
            currentLife += addedLife;
            ((RelativeLayout.LayoutParams) healView.getLayoutParams()).addRule(RelativeLayout.ALIGN_LEFT, player.getName().getId());
            ((RelativeLayout.LayoutParams) healView.getLayoutParams()).addRule(RelativeLayout.ALIGN_RIGHT, player.getName().getId());
            if (rotated) {
                ((RelativeLayout.LayoutParams) healView.getLayoutParams()).addRule(RelativeLayout.ALIGN_BOTTOM, 0);
                ((RelativeLayout.LayoutParams) healView.getLayoutParams()).addRule(RelativeLayout.ALIGN_TOP, player.getName().getId());
            }
            else {
                ((RelativeLayout.LayoutParams) healView.getLayoutParams()).addRule(RelativeLayout.ALIGN_TOP, 0);
                ((RelativeLayout.LayoutParams) healView.getLayoutParams()).addRule(RelativeLayout.ALIGN_BOTTOM, player.getName().getId());
            }
            healView.startAnimation(healAnim);
        }
        else {
            currentLife -= addedLife;
            if (rotated) {
                ((RelativeLayout.LayoutParams)damageView.getLayoutParams()).addRule(RelativeLayout.ALIGN_LEFT, 0);
                ((RelativeLayout.LayoutParams) damageView.getLayoutParams()).addRule(RelativeLayout.ALIGN_TOP, 0);
                ((RelativeLayout.LayoutParams)damageView.getLayoutParams()).addRule(RelativeLayout.ALIGN_RIGHT, player.getName().getId());
                ((RelativeLayout.LayoutParams) damageView.getLayoutParams()).addRule(RelativeLayout.BELOW, player.getName().getId());
            }
            else {
                ((RelativeLayout.LayoutParams)damageView.getLayoutParams()).addRule(RelativeLayout.ALIGN_RIGHT, 0);
                ((RelativeLayout.LayoutParams) damageView.getLayoutParams()).addRule(RelativeLayout.BELOW, 0);
                ((RelativeLayout.LayoutParams)damageView.getLayoutParams()).addRule(RelativeLayout.ALIGN_LEFT, player.getName().getId());
                ((RelativeLayout.LayoutParams) damageView.getLayoutParams()).addRule(RelativeLayout.ALIGN_TOP, player.getLifeUp().getId());
            }
            damageView.startAnimation(damageAnim);
        }
        player.getScore().setText(Integer.toString(currentLife));

        // RIP
        if (currentLife <= 0)
        {
            ((RelativeLayout.LayoutParams)ripView.getLayoutParams()).addRule(RelativeLayout.ALIGN_LEFT, player.getName().getId());
            if (rotated) {
                ((RelativeLayout.LayoutParams) ripView.getLayoutParams()).addRule(RelativeLayout.ALIGN_BOTTOM, player.getName().getId());
            }
            else
            {
                ((RelativeLayout.LayoutParams) ripView.getLayoutParams()).addRule(RelativeLayout.ALIGN_BOTTOM, player.getLifeDown().getId());
            }
            ripView.startAnimation(ripAnim);
        }

    }

}
