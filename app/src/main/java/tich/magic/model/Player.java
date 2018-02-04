package tich.magic.model;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.SoundPool;
import android.support.v4.content.res.ResourcesCompat;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tich.magic.GameActivity;
import tich.magic.Preferences;
import tich.magic.R;
import tich.magic.listeners.MyGestureListener;
import tich.magic.listeners.ResurrectAnimationListener;

public class Player {

    protected GameActivity gameActivity;
    protected Typeface typeface;
    protected TextView name;
    protected TextView score;
    protected TextView poison;
    protected ImageView poisonImage;
    protected ImageView avatar;
    protected ImageView parcheminHaut;
    protected ImageView parcheminMilieu;
    protected ImageView parcheminBas;
    protected ImageView winner;
    protected MyGestureListener scoreGestureListener;
    protected boolean isDead = false;
    protected float posNameY;
    protected Animation winnerAnim;
    protected Animation openParcheminMilieuAnim;


    public Player(GameActivity gameActivity, String playerName, Context context, SoundPool sp, int nbOfPlayers, int layoutWidth, int layoutHeight, ImageView parcheminHaut, ImageView parcheminMilieu, ImageView parcheminBas, ImageView stars, ImageView claws)
    {
        this.gameActivity = gameActivity;
        this.typeface = ResourcesCompat.getFont(context, R.font.berkshire_swash);
        this.parcheminHaut = parcheminHaut;
        this.parcheminMilieu = parcheminMilieu;
        this.parcheminBas = parcheminBas;

        createPlayerElements(playerName, nbOfPlayers, layoutWidth, layoutHeight, sp, stars, claws);

        openParcheminMilieuAnim = AnimationUtils.loadAnimation(gameActivity, R.anim.open_parchemin_milieu);
        openParcheminMilieuAnim.setAnimationListener(new ResurrectAnimationListener(getParcheminMilieu(), sp, gameActivity));

        /*
        name.setTag(playerName);
        if (gameActivity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE) {
            name.setOnLongClickListener(new View.OnLongClickListener() {

                public boolean onLongClick(View v) {
                    ClipData.Item item = new ClipData.Item((CharSequence)v.getTag());
                    ClipData dragData = new ClipData((CharSequence)v.getTag(), new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                    View.DragShadowBuilder myShadow = new MyDragShadowBuilder(name);
                    v.startDrag(dragData,  // the data to be dragged
                            myShadow,  // the drag shadow builder
                            null,      // no need to use local data
                            0          // flags (not currently used, set to 0)
                    );
                    return true;
                }
            });
            name.setOnDragListener(new MyDragEventListener(gameActivity));
        }*/
    }

    protected void createPlayerElements(String playerName, int nbOfPlayers, int layoutWidth, int layoutHeight, SoundPool sp, ImageView stars, ImageView claws)
    {
        createName(playerName, nbOfPlayers);
        createAvatar(playerName, layoutWidth);
        createScore(layoutHeight);
        createPoison(layoutWidth, layoutHeight, nbOfPlayers);
        createWinner(layoutWidth);

        ((RelativeLayout.LayoutParams) claws.getLayoutParams()).addRule(RelativeLayout.ABOVE, name.getId());
        ((RelativeLayout.LayoutParams) claws.getLayoutParams()).addRule(RelativeLayout.ALIGN_LEFT, name.getId());
        ((RelativeLayout.LayoutParams) stars.getLayoutParams()).addRule(RelativeLayout.ALIGN_LEFT, name.getId());
        ((RelativeLayout.LayoutParams) stars.getLayoutParams()).addRule(RelativeLayout.ALIGN_RIGHT, name.getId());
        ((RelativeLayout.LayoutParams) stars.getLayoutParams()).addRule(RelativeLayout.ALIGN_BOTTOM, name.getId());

        scoreGestureListener = new MyGestureListener(gameActivity, this, sp, stars, claws, MyGestureListener.LIFE);
        final GestureDetector gdtScore = new GestureDetector(scoreGestureListener);
        score.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                gdtScore.onTouchEvent(event);
                return true;
            }
        });

        final GestureDetector gdtPoison = new GestureDetector(new MyGestureListener(gameActivity, this, sp, stars, claws, MyGestureListener.POISON));
        poison.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                gdtPoison.onTouchEvent(event);
                return true;
            }
        });
        poisonImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                gdtPoison.onTouchEvent(event);
                return true;
            }
        });

        final GestureDetector gdtName = new GestureDetector(new MyGestureListener(gameActivity, this, sp, stars, claws, MyGestureListener.NAME));
        name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                gdtName.onTouchEvent(event);
                return true;
            }
        });

        winnerAnim = AnimationUtils.loadAnimation(gameActivity, R.anim.winner);
    }

    protected void createName(String playerName, int nbOfPlayers)
    {
        name = new TextView(gameActivity);
        name.setId(View.generateViewId());
        name.setText(playerName);
        name.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
        ((RelativeLayout.LayoutParams)name.getLayoutParams()).bottomMargin = 100;
        name.setGravity(Gravity.CENTER_HORIZONTAL);
        name.setTypeface(typeface);
        name.setTextSize(38 - nbOfPlayers*3);
        name.setTextColor(Color.DKGRAY);
        ((RelativeLayout.LayoutParams) name.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        ((RelativeLayout.LayoutParams) name.getLayoutParams()).addRule(RelativeLayout.CENTER_HORIZONTAL);
    }

    protected void createAvatar(String playerName, int layoutWidth)
    {
        avatar = new ImageView(gameActivity);
        avatar.setId(View.generateViewId());
        avatar.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
        ((RelativeLayout.LayoutParams) avatar.getLayoutParams()).addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        avatar.setImageResource(gameActivity.getResources().getIdentifier("avatar_" + playerName.toLowerCase(), "drawable", gameActivity.getApplicationContext().getPackageName()) );
        avatar.setAlpha(60);
        ((RelativeLayout.LayoutParams) avatar.getLayoutParams()).width = (int) (layoutWidth * 0.7);
        ((RelativeLayout.LayoutParams) avatar.getLayoutParams()).height = ((RelativeLayout.LayoutParams) avatar.getLayoutParams()).width;
    }

    protected void createScore(int layoutHeight)
    {
        score = new TextView(gameActivity);
        score.setId(View.generateViewId());
        score.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                new Double(layoutHeight * 0.3).intValue()));
        score.setGravity(Gravity.CENTER);
        score.setText(Integer.toString(gameActivity.getStartLife()));
        ((RelativeLayout.LayoutParams) score.getLayoutParams()).topMargin = (int) (layoutHeight * 0.1);
        ((RelativeLayout.LayoutParams) score.getLayoutParams()).addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        score.setTextSize(50);
        score.setTypeface(typeface);

    }

    protected void createPoison(int layoutWidth, int layoutHeight, int nbOfPlayers)
    {
        poison = new TextView(gameActivity);
        poison.setId(View.generateViewId());
        poison.setLayoutParams(new RelativeLayout.LayoutParams(
                new Double(layoutWidth * (0.5 - (float)nbOfPlayers/50)).intValue(),
                new Double(layoutHeight * 0.2).intValue()));
        poison.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
        poison.setText("0");
        ((RelativeLayout.LayoutParams) poison.getLayoutParams()).topMargin = (int) (layoutHeight * 0.4);
        poison.setPadding(0, 0, new Double(layoutWidth * 0.05).intValue(), 0);
        poison.setTextSize(35);
        poison.setTextColor(Color.parseColor("#007F0E"));
        poison.setTypeface(typeface);

        poisonImage = new ImageView(gameActivity);
        poisonImage.setId(View.generateViewId());
        poisonImage.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                new Double(layoutHeight * 0.2).intValue()));
        poisonImage.setScaleType(ImageView.ScaleType.FIT_START);
        ((RelativeLayout.LayoutParams) poisonImage.getLayoutParams()).addRule(RelativeLayout.RIGHT_OF, poison.getId());
        ((RelativeLayout.LayoutParams) poisonImage.getLayoutParams()).addRule(RelativeLayout.ALIGN_BOTTOM, poison.getId());
        poisonImage.setImageResource(R.drawable.flacon_poison);
    }

    protected void createWinner(int layoutWidth)
    {
        winner = new ImageView(gameActivity);
        winner.setId(View.generateViewId());
        winner.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
        ((RelativeLayout.LayoutParams) winner.getLayoutParams()).addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        winner.setImageResource(gameActivity.getResources().getIdentifier("winner", "drawable", gameActivity.getApplicationContext().getPackageName()) );
        winner.setVisibility(View.INVISIBLE);
        ((RelativeLayout.LayoutParams) winner.getLayoutParams()).width = (int) (layoutWidth * 0.7);
        ((RelativeLayout.LayoutParams) winner.getLayoutParams()).height = ((RelativeLayout.LayoutParams) winner.getLayoutParams()).width;
    }

    public void attachToLayout(RelativeLayout relativeLayout)
    {
        relativeLayout.addView(name);
        relativeLayout.addView(avatar);
        relativeLayout.addView(score);
        relativeLayout.addView(poison);
        relativeLayout.addView(poisonImage);
        relativeLayout.addView(winner);
        if (!Preferences.getPreferences().hasPoison())
        {
            hidePoison();
        }
    }

    public void displayPoison()
    {
        poison.setVisibility(View.VISIBLE);
        poisonImage.setVisibility(View.VISIBLE);
    }

    public void hidePoison()
    {
        poison.setVisibility(View.INVISIBLE);
        poisonImage.setVisibility(View.INVISIBLE);
    }

    public void resetLife(int life)
    {
        score.setText(Integer.toString(life));
        scoreGestureListener.startHealAnimation();
        hideWinner();
        if (isDead())
        {
            openParchemin();
        }
    }

    public void displayWinner()
    {
        winner.setVisibility(View.VISIBLE);
        winner.startAnimation(winnerAnim);
        Preferences.getPreferences(gameActivity).updatePlayerStats(gameActivity.getPlayers());
    }

    public void hideWinner()
    {
        winner.setVisibility(View.INVISIBLE);
    }

    public void openParchemin()
    {
        getParcheminMilieu().startAnimation(openParcheminMilieuAnim);

        ObjectAnimator avatarAnimator = ObjectAnimator.ofFloat ( getAvatar() , "alpha" , 1);
        avatarAnimator.setDuration(2000);
        avatarAnimator.start();

        final boolean hasPoison = Preferences.getPreferences().hasPoison();

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat ( getParcheminBas() , "y" , getParcheminHaut().getHeight() + getParcheminMilieu().getHeight());
        objectAnimator.setDuration(2000);
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {
                float animatedValue = (float)updatedAnimation.getAnimatedValue();

                int poisonTop = ((RelativeLayout.LayoutParams)getPoison().getLayoutParams()).topMargin + ((RelativeLayout.LayoutParams)getPoison().getLayoutParams()).height/2;
                if (hasPoison && animatedValue >= poisonTop) {
                    getPoison().setVisibility(View.VISIBLE);
                    getPoisonImage().setVisibility(View.VISIBLE);
                }

                int scoreTop = ((RelativeLayout.LayoutParams)getScore().getLayoutParams()).topMargin + ((RelativeLayout.LayoutParams)getScore().getLayoutParams()).height/2;
                if (animatedValue >= scoreTop) {
                    getScore().setVisibility(View.VISIBLE);
                }

            }
        });
        objectAnimator.start();

        ObjectAnimator nameAnimator = ObjectAnimator.ofFloat ( getName() , "y" , getPosNameY());
        nameAnimator.setDuration(2000);
        nameAnimator.start();

        setDead(false);
    }



    public void die()
    {
        scoreGestureListener.die();
    }

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public TextView getScore() {
        return score;
    }

    public void setScore(TextView score) {
        this.score = score;
    }

    public ImageView getAvatar() {
        return avatar;
    }

    public void setAvatar(ImageView avatar) {
        this.avatar = avatar;
    }

    public TextView getPoison() {
        return poison;
    }

    public void setPoison(TextView poison) {
        this.poison = poison;
    }

    public ImageView getParcheminHaut() {
        return parcheminHaut;
    }

    public void setParcheminHaut(ImageView parcheminHaut) {
        this.parcheminHaut = parcheminHaut;
    }

    public ImageView getParcheminMilieu() {
        return parcheminMilieu;
    }

    public ImageView getPoisonImage() {
        return poisonImage;
    }

    public void setPoisonImage(ImageView poisonImage) {
        this.poisonImage = poisonImage;
    }

    public void setParcheminMilieu(ImageView parcheminMilieu) {
        this.parcheminMilieu = parcheminMilieu;
    }

    public ImageView getParcheminBas() {
        return parcheminBas;
    }

    public void setParcheminBas(ImageView parcheminBas) {
        this.parcheminBas = parcheminBas;
    }

    public MyGestureListener getScoreGestureListener() {
        return scoreGestureListener;
    }

    public void setScoreGestureListener(MyGestureListener scoreGestureListener) {
        this.scoreGestureListener = scoreGestureListener;
    }

    public ImageView getWinner() {
        return winner;
    }

    public void setWinner(ImageView winner) {
        this.winner = winner;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public float getPosNameY() {
        return posNameY;
    }

    public void setPosNameY(float posNameY) {
        this.posNameY = posNameY;
    }
}
