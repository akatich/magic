package tich.magic.model;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.SoundPool;
import android.support.v4.content.res.ResourcesCompat;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tich.magic.GameActivity;
import tich.magic.R;
import tich.magic.listeners.LifeUpListener;
import tich.magic.listeners.MyGestureListener;

public class Player {

    private GameActivity gameActivity;
    private int id;
    private TextView name;
    private TextView score;
    private TextView poison;
    private ImageView poisonImage;
    private Button lifeUp;
    private Button lifeDown;
    private ImageView avatar;
    private ImageView parcheminHaut;
    private ImageView parcheminMilieu;
    private ImageView parcheminBas;
    private ImageView arrow;
    private LifeUpListener lifeUpListener;
    private MyGestureListener scoreGestureListener;



    public Player(GameActivity gameActivity, String playerName, Context context, SoundPool sp, int nbOfPlayers, int layoutWidth, int layoutHeight, ImageView parcheminHaut, ImageView parcheminMilieu, ImageView parcheminBas, ImageView stars, ImageView claws)
    {
        this.gameActivity = gameActivity;
        Typeface typeface = ResourcesCompat.getFont(context, R.font.berkshire_swash);
        this.parcheminHaut = parcheminHaut;
        this.parcheminMilieu = parcheminMilieu;
        this.parcheminBas = parcheminBas;

        name = new TextView(context);
        //name.setBackgroundColor(Color.GREEN);
        name.setId(View.generateViewId());
        name.setText(playerName);
        name.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
        ((RelativeLayout.LayoutParams)name.getLayoutParams()).bottomMargin = 100;
        name.setGravity(Gravity.CENTER_HORIZONTAL);
        name.setTypeface(typeface);
        name.setTextSize(20);
        name.setTextColor(Color.DKGRAY);
        ((RelativeLayout.LayoutParams) name.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        ((RelativeLayout.LayoutParams) name.getLayoutParams()).addRule(RelativeLayout.CENTER_HORIZONTAL);

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

        score = new TextView(context);
        score.setId(View.generateViewId());
        score.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                new Double(layoutHeight * 0.3).intValue()));
        score.setGravity(Gravity.CENTER);
        score.setText("20");
        ((RelativeLayout.LayoutParams) score.getLayoutParams()).topMargin = (int) (layoutHeight * 0.1);
        ((RelativeLayout.LayoutParams) score.getLayoutParams()).addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        score.setTextSize(50);
        score.setTypeface(typeface);

        poison = new TextView(context);
        poison.setId(View.generateViewId());

        int leftMargin = 20;
        poison.setLayoutParams(new RelativeLayout.LayoutParams(
                new Double(layoutWidth * 0.4).intValue(),
                new Double(layoutHeight * 0.2).intValue()));
        poison.setGravity(Gravity.CENTER);
        poison.setText("0");
        ((RelativeLayout.LayoutParams) poison.getLayoutParams()).topMargin = (int) (layoutHeight * 0.4);
        ((RelativeLayout.LayoutParams) poison.getLayoutParams()).leftMargin = leftMargin;
        poison.setTextSize(25);
        poison.setTextColor(Color.parseColor("#007F0E"));
        poison.setTypeface(typeface);

        poisonImage = new ImageView(gameActivity);
        poisonImage.setId(View.generateViewId());
        poisonImage.setLayoutParams(new RelativeLayout.LayoutParams(
                new Double(layoutWidth * 0.2).intValue(),
                new Double(layoutHeight * 0.2).intValue()));
        poisonImage.setForegroundGravity(Gravity.CENTER);
        ((RelativeLayout.LayoutParams) poisonImage.getLayoutParams()).addRule(RelativeLayout.RIGHT_OF, poison.getId());
        ((RelativeLayout.LayoutParams) poisonImage.getLayoutParams()).addRule(RelativeLayout.ALIGN_BOTTOM, poison.getId());
        poisonImage.setImageResource(R.drawable.poison);

        //((RelativeLayout.LayoutParams) poisonImage.getLayoutParams()).width = (screenWidth / nbOfPlayers) - 200;
        //((RelativeLayout.LayoutParams) poisonImage.getLayoutParams()).height = ((RelativeLayout.LayoutParams) poisonImage.getLayoutParams()).width;

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


        /*lifeUp = new Button(context);
        lifeUp.setId(View.generateViewId());
        lifeUp.setTag("+");
        lifeUp.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
        lifeUp.setBackground(ContextCompat.getDrawable(context, R.drawable.arrow_up));
        ((RelativeLayout.LayoutParams) lifeUp.getLayoutParams()).addRule(RelativeLayout.ABOVE, score.getId());
        ((RelativeLayout.LayoutParams) lifeUp.getLayoutParams()).addRule(RelativeLayout.ALIGN_LEFT, score.getId());
        ((RelativeLayout.LayoutParams) lifeUp.getLayoutParams()).addRule(RelativeLayout.ALIGN_RIGHT, score.getId());
        lifeUpListener = new LifeUpListener(gameActivity, this, context, sp);
        lifeUp.setOnClickListener(lifeUpListener);

        lifeDown = new Button(context);
        lifeDown.setId(View.generateViewId());
        lifeDown.setTag("-");
        lifeDown.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
        lifeDown.setBackground(ContextCompat.getDrawable(context, R.drawable.arrow_down));
        ((RelativeLayout.LayoutParams) lifeDown.getLayoutParams()).addRule(RelativeLayout.BELOW, score.getId());
        ((RelativeLayout.LayoutParams) lifeDown.getLayoutParams()).addRule(RelativeLayout.ALIGN_LEFT, score.getId());
        ((RelativeLayout.LayoutParams) lifeDown.getLayoutParams()).addRule(RelativeLayout.ALIGN_RIGHT, score.getId());
        lifeDown.setOnClickListener(lifeUpListener);

        RelativeLayout layout = gameActivity.findViewById(R.id.game_layout);
        layout.addView(name);
        layout.addView(avatar);
        layout.addView(score);
        layout.addView(lifeUp);
        layout.addView(lifeDown);

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

    public void attachToLayout(RelativeLayout relativeLayout)
    {
        relativeLayout.addView(name);
        relativeLayout.addView(avatar);
        relativeLayout.addView(score);
        relativeLayout.addView(poison);
        relativeLayout.addView(poisonImage);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Button getLifeUp() {
        return lifeUp;
    }

    public void setLifeUp(Button lifeUp) {
        this.lifeUp = lifeUp;
    }

    public Button getLifeDown() {
        return lifeDown;
    }

    public void setLifeDown(Button lifeDown) {
        this.lifeDown = lifeDown;
    }

    public void updateLife(String operand, int addedLife)
    {
        lifeUpListener.updateLife(operand, addedLife);
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

    public ImageView getArrow() {
        return arrow;
    }

    public void setArrow(ImageView arrow) {
        this.arrow = arrow;
    }
}
