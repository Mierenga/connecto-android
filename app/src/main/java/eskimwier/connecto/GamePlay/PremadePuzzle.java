package eskimwier.connecto.GamePlay;

import android.util.Log;
import android.view.View;
import android.widget.TableRow;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import eskimwier.connecto.Engine.Junction;
import eskimwier.connecto.Engine.SquareView;

/**
 * Created by mike on 6/22/2016.
 */
public class PremadePuzzle {

    public static final int PUZZLES_AVAILABLE = 11;

    Deque<Integer> puzzles = new ArrayDeque<>();
    Random random = new Random();
    PuzzleActivity activity;

    public PremadePuzzle(PuzzleActivity activity) {
        this.activity = activity;
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

        activity.winText.setVisibility(View.INVISIBLE);
        activity.solved = false;
        activity.setGameColor(activity.gameColors.getIncompleteColor(), false);
        activity.gameTable.removeAllViews();


        try {
            //Log.d("Relative Path", new File("").getAbsolutePath());
            Scanner in = new Scanner(activity.getAssets().open("puzzles/puzzle" + puzzle));
            // THIS LINE IS FOR TESTING WITH SIMPLE PUZZLE:
            //Scanner in = new Scanner(getAssets().open("puzzles/puzzle0"));

            List<String[]> grid = parseInputFile(in);

            int rowNum = grid.size();
            int colNum = grid.get(0).length;

            SquareView.startNewGame(rowNum, colNum, activity.gameColors.getCompleteColor());
            int tileSize = activity.getTileSize(rowNum, colNum);


            for (int i = 0; i < rowNum; i++) {
                TableRow tableRow = new TableRow(activity);
                for (int j = 0; j < colNum; j++) {
                    String input = (grid.get(i))[j].trim();
                    SquareView square = new SquareView(activity, Junction.create(input), i, j, tileSize);
                    square.setOnClickListener(activity);
                    tableRow.addView(square, tileSize, tileSize);
                }
                activity.gameTable.addView(tableRow);
            }

        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (InstantiationException ie) {
            Log.d("Unknown Grid Size", "Must specify grid size prior to instantiation of object");
        }

        activity.performRotations();
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
}
