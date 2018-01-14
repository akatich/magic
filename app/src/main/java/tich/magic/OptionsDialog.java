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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OptionsDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //LayoutInflater inflater = getActivity().getLayoutInflater();
        //builder.setView(inflater.inflate(R.layout.dialog_options, null));


        //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.berkshire_swash);

        TextView title = new TextView(getActivity());
        //title.setBackgroundColor(Color.BLUE);
        title.setPadding(0, 60, 0, 0);
        title.setText("Options");
        title.setGravity(Gravity.CENTER);
        title.setTextSize(20);
        title.setTypeface(typeface);
        builder.setCustomTitle(title);


        AlertDialog dialog = builder.create();

        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
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
        btnNegative.setTextSize(20);
        btnNegative.setTextColor(Color.DKGRAY);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        layoutParams.weight = 10;
        btnPositive.setLayoutParams(layoutParams);
        btnNegative.setLayoutParams(layoutParams);

        // Create the AlertDialog object and return it
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.parchemin);


        return dialog;
    }

    public void ok()
    {

    }
}
