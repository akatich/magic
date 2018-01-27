package tich.magic.model;

import android.content.Context;
import android.media.SoundPool;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import tich.magic.GameActivity;

public class Troll extends Player {

    protected ImageView avatarSeparator;
    protected ImageView avatar2;
    protected String[] playerNamesArray;

    public Troll(GameActivity gameActivity, String playerName, Context context, SoundPool sp, int nbOfPlayers, int layoutWidth, int layoutHeight, ImageView parcheminHaut, ImageView parcheminMilieu, ImageView parcheminBas, ImageView stars, ImageView claws)
    {
        super(gameActivity, playerName, context, sp, nbOfPlayers, layoutWidth, layoutHeight, parcheminHaut, parcheminMilieu, parcheminBas, stars, claws);
    }

    @Override
    protected void createPlayerElements(String playerName, int nbOfPlayers, int layoutWidth, int layoutHeight, SoundPool sp, ImageView stars, ImageView claws)
    {
        playerNamesArray = playerName.split(";");
        super.createPlayerElements(playerName, nbOfPlayers, layoutWidth, layoutHeight, sp, stars, claws);
    }

    @Override
    protected void createName(String playerName, int nbOfPlayers)
    {
        super.createName(playerName, nbOfPlayers);

        name.setText(playerNamesArray[0] + " / " + playerNamesArray[1]);
    }

    @Override
    protected void createAvatar(String playerName, int layoutWidth)
    {
        avatarSeparator = new ImageView(gameActivity);
        avatarSeparator.setId(View.generateViewId());
        avatarSeparator.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
        ((RelativeLayout.LayoutParams) avatarSeparator.getLayoutParams()).addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        avatarSeparator.setImageResource(gameActivity.getResources().getIdentifier("avatar_separator", "drawable", gameActivity.getApplicationContext().getPackageName()) );
        avatarSeparator.setAlpha(60);
        ((RelativeLayout.LayoutParams) avatarSeparator.getLayoutParams()).width = (int) (layoutWidth * 0.1);
        ((RelativeLayout.LayoutParams) avatarSeparator.getLayoutParams()).height = (int) (layoutWidth * 0.3);

        avatar = new ImageView(gameActivity);
        avatar.setId(View.generateViewId());
        avatar.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
        ((RelativeLayout.LayoutParams) avatar.getLayoutParams()).addRule(RelativeLayout.LEFT_OF, avatarSeparator.getId());
        ((RelativeLayout.LayoutParams) avatar.getLayoutParams()).addRule(RelativeLayout.ALIGN_TOP, avatarSeparator.getId());
        avatar.setImageResource(gameActivity.getResources().getIdentifier("avatar_" + playerNamesArray[0].toLowerCase(), "drawable", gameActivity.getApplicationContext().getPackageName()) );
        avatar.setAlpha(60);
        ((RelativeLayout.LayoutParams) avatar.getLayoutParams()).width = (int) (layoutWidth * 0.3);
        ((RelativeLayout.LayoutParams) avatar.getLayoutParams()).height = ((RelativeLayout.LayoutParams) avatar.getLayoutParams()).width;

        avatar2 = new ImageView(gameActivity);
        avatar2.setId(View.generateViewId());
        avatar2.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
        ((RelativeLayout.LayoutParams) avatar2.getLayoutParams()).addRule(RelativeLayout.RIGHT_OF, avatarSeparator.getId());
        ((RelativeLayout.LayoutParams) avatar2.getLayoutParams()).addRule(RelativeLayout.ALIGN_TOP, avatarSeparator.getId());
        avatar2.setImageResource(gameActivity.getResources().getIdentifier("avatar_" + playerNamesArray[1].toLowerCase(), "drawable", gameActivity.getApplicationContext().getPackageName()) );
        avatar2.setAlpha(60);
        ((RelativeLayout.LayoutParams) avatar2.getLayoutParams()).width = (int) (layoutWidth * 0.3);
        ((RelativeLayout.LayoutParams) avatar2.getLayoutParams()).height = ((RelativeLayout.LayoutParams) avatar2.getLayoutParams()).width;
    }

    @Override
    public void attachToLayout(RelativeLayout relativeLayout)
    {
        super.attachToLayout(relativeLayout);
        relativeLayout.addView(avatarSeparator);
        relativeLayout.addView(avatar2);
    }
}
