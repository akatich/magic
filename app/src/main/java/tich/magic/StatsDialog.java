package tich.magic;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;

import tich.magic.model.Stats;

public class StatsDialog extends DialogFragment {

    private GameActivity activity;
    private Typeface typeface = null;


    public StatsDialog(GameActivity activity)
    {
        super();
        this.activity = activity;
        typeface = ResourcesCompat.getFont(activity, R.font.berkshire_swash);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        final View optView = inflater.inflate(R.layout.dialog_stats, null);
        builder.setView(optView);
        AlertDialog dialog = builder.create();

        TableLayout tableStats = optView.findViewById(R.id.table_stats);
        HashMap stats = Preferences.getPreferences(activity).getPlayerStats();
        Iterator iter = stats.keySet().iterator();
        try {
            while (iter.hasNext()) {
                String playerName = (String) iter.next();
                Stats playerStats = (Stats) stats.get(playerName);

                if (playerStats!=null) {
                    TableRow tr = new TableRow(activity);
                    TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                    tr.setLayoutParams(params);
                    tr.setGravity(Gravity.CENTER_VERTICAL);
                    tr.setPadding(5, 5, 5, 5);

                    TextView tv = new TextView(activity);
                    tv.setGravity(Gravity.CENTER_HORIZONTAL);
                    tv.setText(playerName);
                    tv.setTypeface(typeface);
                    tv.setTextSize(16);
                    tv.setTextColor(Color.DKGRAY);
                    tr.addView(tv, params);

                    TextView tv2 = new TextView(activity);
                    tv2.setGravity(Gravity.CENTER_HORIZONTAL);
                    tv2.setText(Integer.toString(playerStats.getTotalGamesPlayed()));
                    tv2.setTypeface(typeface);
                    tv2.setTextSize(16);
                    tv2.setTextColor(Color.DKGRAY);
                    tr.addView(tv2, params);

                    TextView tv3 = new TextView(activity);
                    tv3.setGravity(Gravity.CENTER_HORIZONTAL);
                    tv3.setText(Integer.toString(playerStats.getTotalGamesWon()));
                    tv3.setTypeface(typeface);
                    tv3.setTextSize(16);
                    tv3.setTextColor(Color.DKGRAY);
                    tr.addView(tv3, params);

                    TextView tv4 = new TextView(activity);
                    tv4.setGravity(Gravity.CENTER_HORIZONTAL);
                    tv4.setText(playerStats.getRatioGamesWon());
                    tv4.setTypeface(typeface);
                    tv4.setTextSize(16);
                    tv4.setTextColor(Color.DKGRAY);
                    tr.addView(tv4, params);

                    tableStats.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button

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
        btnPositive.setAllCaps(false);
        btnNegative.setTextSize(20);
        btnNegative.setTextColor(Color.DKGRAY);
        btnNegative.setAllCaps(false);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        layoutParams.weight = 10;
        layoutParams.bottomMargin = 30;
        btnPositive.setLayoutParams(layoutParams);
        btnNegative.setLayoutParams(layoutParams);

        // Create the AlertDialog object and return it
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.parchemin_horizontal);
        dialog.getWindow().setWindowAnimations(R.style.DialogAnimation);

        return dialog;
    }
}
