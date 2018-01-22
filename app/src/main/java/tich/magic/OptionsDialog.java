package tich.magic;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

public class OptionsDialog extends DialogFragment {

    private GameActivity activity;
    private int previousStartLife;
    private int selectedStartLife;
    private SoundPool sp;
    private int selectSound;


    public OptionsDialog(GameActivity activity)
    {
        super();
        this.activity = activity;
        sp = activity.getSp();
        selectSound = sp.load(activity, R.raw.select, 1);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        previousStartLife = activity.getStartLife();
        selectedStartLife = activity.getStartLife();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        final View optView = inflater.inflate(R.layout.dialog_options, null);
        builder.setView(optView);
        AlertDialog dialog = builder.create();

        final ToggleButton toggleSound = optView.findViewById(R.id.toggle_sound);
        final ImageView imgSound = optView.findViewById(R.id.img_sound);
        toggleSound.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v)
            {
                if (toggleSound.isChecked())
                {
                    imgSound.setImageResource(R.drawable.avec_son);
                }
                else
                {
                    imgSound.setImageResource(R.drawable.sans_son);
                }
            }
        });
        if (Preferences.getPreferences().hasSound()) {
            toggleSound.setChecked(true);
            imgSound.setImageResource(R.drawable.avec_son);
        }
        else {
            toggleSound.setChecked(false);
            imgSound.setImageResource(R.drawable.sans_son);
        }

        final ToggleButton togglePoison = optView.findViewById(R.id.toggle_poison);
        final ImageView imgPoison = optView.findViewById(R.id.img_poison);
        togglePoison.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v)
            {
                if (togglePoison.isChecked())
                {
                    imgPoison.setImageResource(R.drawable.flacon_poison);
                }
                else
                {
                    imgPoison.setImageResource(R.drawable.sans_flacon_poison);
                }
            }
        });
        if (Preferences.getPreferences().hasPoison()) {
            togglePoison.setChecked(true);
            imgPoison.setImageResource(R.drawable.flacon_poison);
        }
        else {
            togglePoison.setChecked(false);
            imgPoison.setImageResource(R.drawable.sans_flacon_poison);
        }

        final Button buttonLife5 = optView.findViewById(R.id.btn_life_5);
        final Button buttonLife20 = optView.findViewById(R.id.btn_life_20);
        final Button buttonLife30 = optView.findViewById(R.id.btn_life_30);
        buttonLife5.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v)
            {
                buttonLife5.setBackgroundResource(R.drawable.button);
                buttonLife20.setBackgroundResource(R.drawable.button_inactive);
                buttonLife30.setBackgroundResource(R.drawable.button_inactive);
                selectedStartLife = 5;
                if (Preferences.getPreferences().hasSound())
                    sp.play(selectSound, 1, 1, 1, 0, 1f);
            }
        });
        buttonLife20.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v)
            {
                buttonLife5.setBackgroundResource(R.drawable.button_inactive);
                buttonLife20.setBackgroundResource(R.drawable.button);
                buttonLife30.setBackgroundResource(R.drawable.button_inactive);
                selectedStartLife = 20;
                if (Preferences.getPreferences().hasSound())
                    sp.play(selectSound, 1, 1, 1, 0, 1f);
            }
        });
        buttonLife30.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v)
            {
                buttonLife5.setBackgroundResource(R.drawable.button_inactive);
                buttonLife20.setBackgroundResource(R.drawable.button_inactive);
                buttonLife30.setBackgroundResource(R.drawable.button);
                selectedStartLife = 30;
                if (Preferences.getPreferences().hasSound())
                    sp.play(selectSound, 1, 1, 1, 0, 1f);
            }
        });
        switch (previousStartLife)
        {
            case 5:
                buttonLife5.setBackgroundResource(R.drawable.button);
                buttonLife20.setBackgroundResource(R.drawable.button_inactive);
                buttonLife30.setBackgroundResource(R.drawable.button_inactive);
                break;
            case 20:
                buttonLife5.setBackgroundResource(R.drawable.button_inactive);
                buttonLife20.setBackgroundResource(R.drawable.button);
                buttonLife30.setBackgroundResource(R.drawable.button_inactive);
                break;
            case 30:
                buttonLife5.setBackgroundResource(R.drawable.button_inactive);
                buttonLife20.setBackgroundResource(R.drawable.button_inactive);
                buttonLife30.setBackgroundResource(R.drawable.button);
                break;
        }

        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                if (toggleSound.isChecked()) {
                    Preferences.getPreferences().saveOptionSound(true);
                }
                else {
                    Preferences.getPreferences().saveOptionSound(false);
                }

                if (togglePoison.isChecked()) {
                    Preferences.getPreferences().saveOptionPoison(true);
                    activity.displayPoison();
                }
                else {
                    Preferences.getPreferences().saveOptionPoison(false);
                    activity.hidePoison();
                }

                if (selectedStartLife != previousStartLife)
                {
                    activity.resetLife(selectedStartLife);
                    activity.setStartLife(selectedStartLife);
                    previousStartLife = selectedStartLife;
                }
            }
        });
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        dialog.show();

        Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button btnNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        btnPositive.setTextSize(20);
        btnPositive.setTextColor(Color.DKGRAY);
        btnNegative.setTextSize(20);
        btnNegative.setTextColor(Color.DKGRAY);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        layoutParams.weight = 10;
        layoutParams.bottomMargin = 25;
        btnPositive.setLayoutParams(layoutParams);
        btnNegative.setLayoutParams(layoutParams);

        // Create the AlertDialog object and return it
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.parchemin);
        dialog.getWindow().setWindowAnimations(R.style.DialogAnimation);

        return dialog;
    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
