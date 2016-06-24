package eskimwier.connecto.GamePlay;

import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Random;

import eskimwier.connecto.Campaign.ScoreKeeper;
import eskimwier.connecto.Engine.SquareView;
import eskimwier.connecto.R;

public class PuzzleActivity extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout background;
    View gameFrame;
    TableLayout gameTable;
    View winText;
    TextView gameScoreText;
    TextView totalScoreText;

    boolean solved = false;
    Random random = new Random();

    DifficultyMode currentDifficulty = DifficultyMode.Simple;
    GameColors gameColors = GameColors.Spectro;
    ScoreKeeper scoreKeeper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        setupStatusBar();

        background = (RelativeLayout) findViewById(R.id.background);
        gameFrame = findViewById(R.id.game_frame);
        winText = findViewById(R.id.win_text);
        gameTable = (TableLayout) findViewById(R.id.game_table);
        gameScoreText = (TextView) findViewById(R.id.game_score);
        totalScoreText = (TextView) findViewById(R.id.total_score);

        setGameTableHeight();

        setupAutogenGame();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_game:
                setupAutogenGame();
                break;
            case R.id.simple_mode:
                currentDifficulty = DifficultyMode.Simple;
                item.setChecked(true);
                setupAutogenGame();
                break;
            case R.id.easy_mode:
                currentDifficulty = DifficultyMode.Easy;
                item.setChecked(true);
                setupAutogenGame();
                break;
            case R.id.med_mode:
                currentDifficulty = DifficultyMode.Intermediate;
                item.setChecked(true);
                setupAutogenGame();
                break;
            case R.id.hard_mode:
                currentDifficulty = DifficultyMode.Advanced;
                item.setChecked(true);
                setupAutogenGame();
                break;
            case R.id.martian_color:
                gameColors = GameColors.Martian;
                setGameColor(gameColors.getIncompleteColor(), false);
                setupAutogenGame();
                break;
            case R.id.spectro_color:
                gameColors = GameColors.Spectro;
                setGameColor(gameColors.getCompleteColor(), false);
                setupAutogenGame();
                break;
            default:
                super.onOptionsItemSelected(item);

        }
        return true;

    }

    void setGameTableHeight()
    {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        ViewGroup.LayoutParams params = gameFrame.getLayoutParams();
        final TypedArray styledAttributes = getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.actionBarSize});
        int actionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        params.height = size.y - 220 - actionBarSize;
        params.width = size.x - 30;
        gameFrame.setLayoutParams(new RelativeLayout.LayoutParams(params));

    }

    void setupStatusBar() {
        Window window = this.getWindow();
        if (window != null) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
        }
    }

    private void setupAutogenGame() {
        setGameColor(gameColors.getIncompleteColor(), false);
        Autogen autogen = new Autogen(currentDifficulty, gameColors.getIncompleteColor(), this);
        autogen.start();

        performRotations();

        scoreKeeper = new ScoreKeeper(currentDifficulty);

    }

    void performRotations() {
        gameTable.post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < gameTable.getChildCount(); i++) {
                    TableRow row = (TableRow) gameTable.getChildAt(i);
                    for (int j = 0; j < row.getChildCount(); j++) {
                        int rotations = random.nextInt(4);
                        ((SquareView) row.getChildAt(j)).rotateClockwise(90*rotations);
                    }
                }
            }
        });
    }
    public int getTileSize(int rowNum, int colNum) {
        int tileSize = gameFrame.getWidth() / colNum;
        if (tileSize * rowNum > gameFrame.getHeight()) {
            tileSize = gameFrame.getHeight() / rowNum;
        }
        return tileSize;
    }


    @Override
    public void onClick(View v) {

        if (!this.solved) {
            try {

                ((SquareView) v).rotateClockwise(90);
                gameScoreText.setText(Integer.toString(scoreKeeper.tileRotated()));

                if (checkForJunctio() || solved) {
                    togglePuzzleSolved();
                }

            } catch (ClassCastException cce) {
                Log.d("Unknown View in onClick", "View is not of expected type SquareView");
            }
        }

    }
    void setGameColor(GameColors.Color color, boolean animate) {
        SquareView.setGridColor(color, animate);
        int colorId;
        switch (color) {
            case ORANGE:
                colorId = R.color.orange;
                break;
            case BLUE:
                colorId = R.color.blue;
                break;
            case NEWS:
                colorId = R.color.black;
                break;
            case AZTEC:
                colorId = R.color.teal;
                break;
            case MARTIAN:
                colorId = R.color.black;
                break;
            case NEON:
                colorId = R.color.black;
                break;
            case SPECTRO:
                colorId = R.color.dark_gray;
                break;
            default:
                colorId = R.color.colorPrimaryDark;
        }
        background.setBackgroundColor(getResources().getColor(colorId));
        //getSupportActionBar().setBackgroundDrawable(getDrawable(colorId));
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), colorId, null);
        getSupportActionBar().setBackgroundDrawable(drawable);

    }

    public boolean checkForJunctio() {
        try {
            for (int i = 0; i < gameTable.getChildCount(); i++) {
                TableRow row = (TableRow) gameTable.getChildAt(i);
                for (int j = 0; j < row.getChildCount(); j++) {
                    SquareView square = (SquareView) row.getChildAt(j);
                    if (!square.checkAllNeighbors()) {
                        return false;
                    }
                }
            }
            return true;

        } catch (ClassCastException cce) {
            Log.d("TableLayout Child", "ClassCastException: Child is not of expected type SquareView");
        }
        return false;
    }
    private void togglePuzzleSolved() {
        if (!this.solved) {
            this.solved = true;
            gameScoreText.setText(Integer.toString(scoreKeeper.puzzleCompleted()));
            totalScoreText.setText(Integer.toString(ScoreKeeper.getTotalScore()));
            setGameColor(gameColors.getCompleteColor(), true);
            winTextAnimation();

        } else {
            this.solved = false;
            setGameColor(gameColors.getIncompleteColor(), false);
            winText.setVisibility(View.INVISIBLE);
        }
    }

    private void winTextAnimation() {
        winText.setVisibility(View.VISIBLE);
        winText.post(new Runnable() {
            @Override
            public void run() {
                Animation animation = new RotateAnimation(-5, 5, winText.getWidth()/2, winText.getHeight()/2);
                animation.setInterpolator(new CycleInterpolator(2));
                animation.setDuration(1200);
                winText.startAnimation(animation);
            }
        });
    }
}