package eskimwier.connecto.GamePlay;

import android.animation.ObjectAnimator;
import android.content.res.TypedArray;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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
    UserPrefs userPrefs;

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

        userPrefs = new UserPrefs(getApplicationContext());
        scoreKeeper.setUserPrefs(userPrefs);
        totalScoreText.setText(Integer.toString(userPrefs.getTotalScore()));

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
        gameScoreText.setText(Integer.toString(0));
        setGameColor(gameColors.getIncompleteColor(), false);
        startGameAnimation();
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
    void setGameColor(GameColors.Skin skin, boolean animate) {
        SquareView.setGridColor(skin, animate);
        int colorId;
        switch (skin) {
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

            winTextAnimation();

        } else {
            this.solved = false;
            setGameColor(gameColors.getIncompleteColor(), false);
            winText.setVisibility(View.INVISIBLE);
        }
    }

    private void winTextAnimation() {

        winText.setVisibility(View.VISIBLE);

        int score = scoreKeeper.getGameScore();
        String toastText = (score >= 0 ? "+" + score : score) + " points!";
        Toast toast = Toast.makeText(this, toastText, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 100);
        toast.show();

        winText.post(new Runnable() {
            @Override
            public void run() {

                gameTable.setVisibility(View.INVISIBLE);
                gameScoreText.setText(Integer.toString(scoreKeeper.puzzleCompleted()));
                totalScoreText.setText(Integer.toString(ScoreKeeper.getTotalScore()));
                setGameColor(gameColors.getCompleteColor(), true);

                final Path path = new Path();
                path.quadTo(50, 50, winText.getX(), winText.getY());
                ObjectAnimator.ofFloat(winText, View.X, View.Y, path).start();

                Animation animation = new RotateAnimation(-5, 5, winText.getWidth()/2, winText.getHeight()/2);
                animation.setInterpolator(new CycleInterpolator(2));
                animation.setDuration(1200);
                winText.startAnimation(animation);

                gameTable.setVisibility(View.VISIBLE);
                winGameAnimation();

            }
        });
    }

    private void startGameAnimation() {
        gameTable.post(new Runnable() {
            @Override
            public void run() {

                int span = 750;

                Animation rotate = new RotateAnimation(0, 360, gameTable.getWidth()/2, gameTable.getHeight()/2);
                rotate.setDuration(span);

                Animation shrink = new ScaleAnimation(0.1f, 1f, 0.1f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                shrink.setDuration(span/3);

                Animation grow = new ScaleAnimation(0.5f, 5f, 0.5f, 5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                grow.setStartOffset(span/3);
                grow.setDuration(span/3);

                Animation shrink2 = new ScaleAnimation(1f, 0.2f, 1f, 0.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                shrink2.setStartOffset(span/2);
                shrink2.setDuration(span/3);
                Animation rotate2 = new RotateAnimation(0, 360, gameTable.getWidth()/2, gameTable.getHeight()/2);
                rotate2.setDuration(span/3);
                rotate2.setStartOffset(span/2);

                AnimationSet set = new AnimationSet(true);
                set.setInterpolator(new AccelerateDecelerateInterpolator());
                set.addAnimation(rotate);
                set.addAnimation(shrink);
                set.addAnimation(grow);
                set.addAnimation(shrink2);
                set.addAnimation(rotate2);
                set.setDuration(span);

                gameTable.startAnimation(set);

            }
        });
    }

    private void winGameAnimation() {
        gameTable.post(new Runnable() {
            @Override
            public void run() {

                Animation rotate = new RotateAnimation(0, 360, gameTable.getWidth()/2, gameTable.getHeight()/2);
                rotate.setInterpolator(new AccelerateDecelerateInterpolator());
                rotate.setDuration(1000);

                AnimationSet set = new AnimationSet(false);
                set.addAnimation(rotate);
                set.setDuration(1000);

                gameTable.startAnimation(set);

            }
        });
    }
}
