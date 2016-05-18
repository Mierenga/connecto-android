package eskimwier.connecto;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class PuzzleActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int PUZZLES_AVAILABLE = 11;
    RelativeLayout background;
    View gameFrame;
    TableLayout gameTable;
    View winText;
    View newGame, prevGame;
    Deque<Integer> puzzles = new ArrayDeque<>();
    boolean solved = false;
    Random random = new Random();

    static class GameColors {
        public static SquareView.Color incomplete = SquareView.Color.MARTIAN;
        public static SquareView.Color complete = SquareView.Color.NEON;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        setupStatusBar();

        background = (RelativeLayout) findViewById(R.id.background);
        gameFrame = findViewById(R.id.game_frame);
        newGame = findViewById(R.id.new_game_button);
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setupNewGame();
                setupAutogenGame();
            }
        });
        prevGame = findViewById(R.id.last_game_button);
        prevGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupLastGame();
            }
        });
        winText = (TextView) findViewById(R.id.win_text);
        gameTable = (TableLayout) findViewById(R.id.game_table);

        //setupNewGame();
        setupAutogenGame();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.game_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //case R.id.
            default:
                super.onOptionsItemSelected(item);

        }
        return true;

    }


    void setupStatusBar() {
        Window window = this.getWindow();
        if (window != null) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
        }
    }

    private void setupLastGame() {
        if (!puzzles.isEmpty()) {
            setupGame(puzzles.pop());
        }

    }
    private void setupNewGame() {
        puzzles.push(random.nextInt(PUZZLES_AVAILABLE));
        setupGame(puzzles.peek());
    }
    private void setupGame(int puzzle) {


        winText.setVisibility(View.INVISIBLE);
        solved = false;
        setGameColor(GameColors.incomplete);
        gameTable.removeAllViews();
        Context c = getApplicationContext();


        try {
            //Log.d("Relative Path", new File("").getAbsolutePath());
            Scanner in = new Scanner(getAssets().open("puzzles/puzzle" + puzzle));
            // THIS LINE IS FOR TESTING WITH SIMPLE PUZZLE:
            //Scanner in = new Scanner(getAssets().open("puzzles/puzzle0"));

            List<String[]> grid = parseInputFile(in);

            int rowNum = grid.size();
            int colNum = grid.get(0).length;

            SquareView.startNewGame(rowNum, colNum, GameColors.incomplete);
            int tileSize = getTileSize(rowNum, colNum);


            for (int i = 0; i < rowNum; i++) {
                TableRow tableRow = new TableRow(c);
                for (int j = 0; j < colNum; j++) {
                    String input = (grid.get(i))[j].trim();
                    SquareView square = new SquareView(c, Junction.create(input), i, j, tileSize);
                    square.setOnClickListener(this);
                    tableRow.addView(square, tileSize, tileSize);
                }
                gameTable.addView(tableRow);
            }

        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (InstantiationException ie) {
            Log.d("Unknown Grid Size", "Must specify grid size prior to instantiation of object");
        }

        performRotations();
    }

    private void setupAutogenGame() {
        Autogen autogen = new Autogen(4, 6, SquareView.Color.MARTIAN, this);
        autogen.start();

        performRotations();
    }

    private void performRotations() {
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

    private List<String[]> parseInputFile(Scanner scanner) {

        List<String[]> grid = new ArrayList<>();
        String row;
        for (int i = 0; scanner.hasNextLine(); i++) {
            row = scanner.nextLine();
            if (!row.trim().isEmpty()) {
                String[] items = row.split("\\s+");
                grid.add(i, items);
            }
        }
        return grid;
    }

    public int getTileSize(int rowNum, int colNum) {
        int tileSize = gameFrame.getWidth()/ colNum;
        if (tileSize * rowNum > gameFrame.getHeight()) {
            tileSize = gameFrame.getHeight()/ rowNum;
        }
        return tileSize;
    }

    @Override
    public void onClick(View v) {

        if (!this.solved) {
            try {

                ((SquareView) v).rotateClockwise(90);

                if (checkForJunctio() || solved) {
                    togglePuzzleSolved();
                }

            } catch (ClassCastException cce) {
                Log.d("Unknown View in onClick", "View is not of expected type SquareView");
            }
        }

    }
    private void setGameColor(SquareView.Color color) {
        SquareView.setGridColor(color);
        int colorId;
        switch (color) {
            case ORANGE:
                colorId = R.color.orange;
                break;
            case BLUE:
                colorId = R.color.blue;
                break;
            case NEWS:
                colorId = R.color.teal;
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
            default:
                colorId = R.color.colorPrimaryDark;
        }
        background.setBackgroundColor(getResources().getColor(colorId));
        getSupportActionBar().setBackgroundDrawable(getDrawable(colorId));

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
            setGameColor(GameColors.complete);
            winText.setVisibility(View.VISIBLE);
            winText.post(new Runnable() {
                @Override
                public void run() {
                    Animation animation = new RotateAnimation(-20, 20, winText.getWidth()/2, winText.getHeight()/2);
                    animation.setInterpolator(new CycleInterpolator(4));
                    animation.setDuration(1200);
                    winText.startAnimation(animation);
                }
            });
        } else {
            this.solved = false;
            setGameColor(GameColors.incomplete);
            winText.setVisibility(View.INVISIBLE);
        }
    }
}
