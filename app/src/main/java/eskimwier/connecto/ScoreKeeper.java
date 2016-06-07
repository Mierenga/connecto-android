package eskimwier.connecto;

import android.os.Handler;
import android.widget.Chronometer;

import java.util.Timer;

/**
 * Created by eskimwier on 6/5/16.
 */
public class ScoreKeeper {

    private int totalScore;
    private int gameRotations;
    private int gameScore;
    private int puzzleTime;
    private DifficultyMode difficultyMode;
    private Handler handler = new Handler();

    public int getTotalScore()
    {
        return totalScore;
    }

    public int getGameScore()
    {
        return gameScore;
    }

    public void puzzleStarted(DifficultyMode mode)
    {
        difficultyMode = mode;
        puzzleTime = 0;
        gameScore = 0;
        gameRotations = 0;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

            }
        };
        handler.postDelayed(runnable, 1000);

    }

    public void tileRotated() {
        gameScore = ((difficultyMode.getWidth() + difficultyMode.getHeight())
                * difficultyMode.getArea()) / (++gameRotations * puzzleTime);

    }

    public void puzzleCompleted()
    {

        totalScore += gameScore;
        gameRotations = 0;
        puzzleTime = 0;
        gameScore = 0;
        difficultyMode = null;

    }

}
