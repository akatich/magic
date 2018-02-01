package tich.magic.model;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Stats {

    private int totalGamesPlayed;
    private int totalGamesWon;
    DecimalFormat df = new DecimalFormat("#.#");

    public String getRatioGamesWon()
    {
        if(totalGamesPlayed == 0)
            return "0 %";

        float ratio = totalGamesWon * 100 / totalGamesPlayed;
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(ratio) + " %";
    }

    public int getTotalGamesPlayed() {
        return totalGamesPlayed;
    }

    public void setTotalGamesPlayed(int totalGamesPlayed) {
        this.totalGamesPlayed = totalGamesPlayed;
    }

    public int getTotalGamesWon() {
        return totalGamesWon;
    }

    public void setTotalGamesWon(int totalGamesWon) {
        this.totalGamesWon = totalGamesWon;
    }
}
