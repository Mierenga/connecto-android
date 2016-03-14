package eskimwier.connecto;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class PuzzleActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int PUZZLES_AVAILABLE = 2;
    RelativeLayout background;
    TableLayout gameTable;
    TextView winText;
    Button newGame;
    Button prevGame;
    int lastPuzzle = 0;
    Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        background = (RelativeLayout) findViewById(R.id.background);
        newGame = (Button) findViewById(R.id.new_game_button);
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupNewGame();
            }
        });
        prevGame = (Button) findViewById(R.id.last_game_button);
        prevGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupLastGame();
            }
        });
        winText = (TextView) findViewById(R.id.win_text);
        gameTable = (TableLayout) findViewById(R.id.game_table);

        setupNewGame();


    }

    private void setupLastGame() {
        setupGame(lastPuzzle);
    }
    private void setupNewGame() {
        setupGame(random.nextInt(PUZZLES_AVAILABLE));
    }
    private void setupGame(int puzzle) {

        setGameColor(SquareView.Color.BLUE);
        gameTable.removeAllViews();
        Context c = getApplicationContext();
        try {
            Log.d("Relative Path", new File("").getAbsolutePath());
            //Scanner in = new Scanner(new File("app/src/main/assets/puzzles/puzzle" + r));
            Scanner in = new Scanner(getAssets().open("puzzles/puzzle" + puzzle));
            int rowNum = 0;
            while (in.hasNextLine()) {
                String rowStr = in.nextLine();
                if (!rowStr.trim().isEmpty()) {
                    TableRow tableRow = new TableRow(c);
                    String[] items = rowStr.split("\\s+");
                    if (rowNum == 0)
                        SquareView.startNewGame(items.length, SquareView.Color.BLUE);
                    for (int colNum = 0; colNum < items.length; colNum++) {
                        int itemSize = gameTable.getWidth()/items.length;
                        SquareView square = new SquareView(c, Junction.create(items[colNum].trim()), rowNum, colNum);
                        square.setOnClickListener(this);
                        tableRow.addView(square, itemSize, itemSize);
                    }
                    gameTable.addView(tableRow);
                    rowNum++;
                }
            }

        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (InstantiationException ie) {
            Log.d("Unknown Grid Size", "Must specify grid size prior to instantiation of object");
        }
    }

    @Override
    public void onClick(View v) {

        try {

            ((SquareView) v).rotateClockwise();
            if (checkForJunctio()) {
                gameComplete();
            } else {

                setGameColor(SquareView.Color.BLUE);
                winText.setVisibility(View.INVISIBLE);
            }

        } catch (ClassCastException cce) {
            Log.d("Unknown View in onClick", "View is not of expected type SquareView");
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
            default:
                colorId = R.color.colorPrimaryDark;
        }
        background.setBackgroundColor(getResources().getColor(colorId));

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
            Log.d("TableLayout Child", "Child is not of expected type SquareView");
        }
        return false;
    }
    private void gameComplete() {

        setGameColor(SquareView.Color.ORANGE);
        winText.setVisibility(View.VISIBLE);
    }
}
