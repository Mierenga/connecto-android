package eskimwier.connecto.Campaign;

import android.os.Handler;
import android.util.Log;

import eskimwier.connecto.GamePlay.DifficultyMode;


/**
 * Created by eskimwier on 6/5/16.
 */
public class ScoreKeeper {

    private static int totalScore;
    private int gameRotations;
    private int gameScore;
    private int puzzleTime;
    private DifficultyMode difficultyMode;
    private Handler handler = new Handler();

    public static int getTotalScore()
    {
        return totalScore;
    }

    public int getGameScore()
    {
        return gameScore;
    }

    public ScoreKeeper(DifficultyMode mode)
    {
        difficultyMode = mode;
        puzzleTime = 0;
        gameScore = 0;
        gameRotations = 0;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                puzzleTime++;
                handler.postDelayed(this, 1000);
            }
        };

        handler.postDelayed(runnable, 1000);

    }

    public int tileRotated() {

        int w = difficultyMode.getWidth();
        int h = difficultyMode.getHeight();
        int a = difficultyMode.getArea();
        int r = ++gameRotations;
        int t = puzzleTime > 0 ? puzzleTime : 1;
        double k = difficultyMode.getScoreMultiplier();

        gameScore = (int)(k *((w+h) * a) - ((r/3)+(t*2)));

        Log.d("SCORE", "w:" + w);
        Log.d("SCORE", "h:" + h);
        Log.d("SCORE", "a:" + a);
        Log.d("SCORE", "r:" + r);
        Log.d("SCORE", "t:" + t);
        Log.d("SCORE", "k:" + k);

        return gameScore;

    }

    public int puzzleCompleted() {

        totalScore += gameScore;
        return gameScore;

    }

}
