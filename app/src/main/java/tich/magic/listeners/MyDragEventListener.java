package tich.magic.listeners;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.media.SoundPool;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import tich.magic.GameActivity;
import tich.magic.R;
import tich.magic.model.Player;

public class MyDragEventListener implements View.OnDragListener  {

    private GameActivity gameActivity;
    private SoundPool sp;
    int swapSound;

    public MyDragEventListener(GameActivity ga)
    {
        super();
        this.gameActivity = ga;
        this.sp = ga.getSp();
        swapSound = sp.load(ga, R.raw.swap, 1);
    }


    // This is the method that the system calls when it dispatches a drag event to the
    // listener.
    public boolean onDrag(View v, DragEvent event) {

        // Defines a variable to store the action type for the incoming event
        final int action = event.getAction();

        // Handles each of the expected events
        switch(action) {

            case DragEvent.ACTION_DRAG_STARTED:

                // Determines if this View can accept the dragged data
                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                    // As an example of what your application might do,
                    // applies a blue color tint to the View to indicate that it can accept
                    // data.
                    //v.setBackgroundColor(Color.BLUE);

                    // Invalidate the view to force a redraw in the new tint
                    v.invalidate();

                    // returns true to indicate that the View can accept the dragged data.
                    return true;

                }

                // Returns false. During the current drag and drop operation, this View will
                // not receive events again until ACTION_DRAG_ENDED is sent.
                return false;

            case DragEvent.ACTION_DRAG_ENTERED:

                // Applies a green tint to the View. Return true; the return value is ignored.

                //v.setBackgroundColor(Color.GREEN);
                ((TextView)v).setShadowLayer(5, 10, 10, Color.YELLOW);
                ((TextView)v).setTextSize(50);

                // Invalidate the view to force a redraw in the new tint
                v.invalidate();

                return true;

            case DragEvent.ACTION_DRAG_LOCATION:

                // Ignore the event
                return true;

            case DragEvent.ACTION_DRAG_EXITED:

                // Re-sets the color tint to blue. Returns true; the return value is ignored.
                //v.setBackgroundColor(Color.BLUE);
                ((TextView)v).setShadowLayer(0, 0, 0, Color.DKGRAY);
                ((TextView)v).setTextSize(30);

                // Invalidate the view to force a redraw in the new tint
                v.invalidate();

                return true;

            case DragEvent.ACTION_DROP:

                // Gets the item containing the dragged data
                ClipData.Item item = event.getClipData().getItemAt(0);

                // Gets the text data from the item.
                String dragData = item.getText().toString();

                String destPlayerName = v.getTag().toString();
                String origPlayerName = dragData;

                // switch the 2 players
                //swapPlayers(origPlayerName, destPlayerName);

                // Displays a message containing the dragged data.
                Toast.makeText(v.getContext(), "Dragged data is " + dragData, Toast.LENGTH_LONG);

                // Turns off any color tints
                v.setBackground(null);
                ((TextView)v).setShadowLayer(0, 0, 0, Color.DKGRAY);
                ((TextView)v).setTextSize(30);


                // Invalidates the view to force a redraw
                v.invalidate();

                // Returns true. DragEvent.getResult() will return true.
                return true;

            case DragEvent.ACTION_DRAG_ENDED:

                // Turns off any color tinting
                v.setBackground(null);

                // Invalidates the view to force a redraw
                v.invalidate();

                // Does a getResult(), and displays what happened.
                if (event.getResult()) {
                    Toast.makeText(v.getContext(), "The drop was handled.", Toast.LENGTH_LONG);

                } else {
                    Toast.makeText(v.getContext(), "The drop didn't work.", Toast.LENGTH_LONG);

                }

                // returns true; the value is ignored.
                return true;

            // An unknown action type was received.
            default:
                Log.e("DragDrop Example","Unknown action type received by OnDragListener.");
                break;
        }

        return false;
    }

    /*private void swapPlayers(String origPlayerName, String destPlayerName)
    {
        HashMap players = gameActivity.getPlayers();

        Player origPlayer = (Player)players.get(origPlayerName.toLowerCase());
        Player destPlayer = (Player)players.get(destPlayerName.toLowerCase());

        int tempPosX = origPlayer.getPosX();
        int tempPosY = origPlayer.getPosY();

        origPlayer.setPosition(destPlayer.getPosX(), destPlayer.getPosY());
        destPlayer.setPosition(tempPosX, tempPosY);

        sp.play(swapSound, 1, 1, 1, 0, 1f);
    }*/
}
