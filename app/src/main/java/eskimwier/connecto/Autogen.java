package eskimwier.connecto;

import android.util.Log;
import android.view.View;
import android.widget.TableRow;

import java.util.Random;

/**
 * Created by mike on 5/16/2016.
 */
public class Autogen {

    private static String TAG = "Autogen";
    private static Random random = new Random();

    private int rowNum;
    private int colNum;
    private int tileSize;
    private SquareView.Color color;
    private PuzzleActivity activity;

    public Autogen(int w, int h, SquareView.Color color, PuzzleActivity activity) {

        activity.gameTable.removeAllViews();
        activity.solved = false;
        activity.winText.setVisibility(View.INVISIBLE);

        this.rowNum = h;
        this.colNum = w;
        this.tileSize = activity.getTileSize(rowNum, colNum);
        this.activity = activity;
        this.color = color;

    }
    public void start() {
        SquareView.startNewGame(rowNum, colNum, color);
        try {

            fillWithBlanks();
            recursivelyCheckTiles(0, 0);

        } catch (InstantiationException ie) {
            Log.d("Unknown Grid Size", "Must specify grid size prior to instantiation of object");
        }
    }

    private void recursivelyCheckTiles(int i, int j) throws InstantiationException {


        Log.d(TAG, "recursively checking : (" + i + "," + j + ")");

        boolean hasSouthernNeighbor = (i < rowNum -1);
        boolean hasEasternNeighbor = (j < colNum - 1);

        if (j==0) {
            swapThisSquare(i, j);
        }

        if (hasEasternNeighbor) {
            swapEastSquare(i, j);
            recursivelyCheckTiles(i, j+1);
        } else if (hasSouthernNeighbor){
            recursivelyCheckTiles(i+1, 0);
        }
        return;
    }

    private SquareView swapThisSquare(int i, int j) throws  InstantiationException {
        SquareView thisSquare = getRandomSquareView(i, j);
        TableRow thisRow = (TableRow) activity.gameTable.getChildAt(i);
        thisRow.removeAllViews();
        thisRow.addView(thisSquare, tileSize, tileSize);
        return thisSquare;
    }

    private SquareView swapEastSquare(int i, int j) throws InstantiationException {
        TableRow thisRow = (TableRow) activity.gameTable.getChildAt(i);
        SquareView squareEast = getRandomSquareView(i, j + 1);
        thisRow.addView(squareEast, tileSize, tileSize);
        return squareEast;
    }

    private SquareView getRandomSquareView(int i, int j) throws InstantiationException {

        SquareView square;

        if (i==0 && j==0) {
            square = getValidCorner(SquareView.Compass.N, SquareView.Compass.W, i, j);
        } else if (i==0 && j==colNum-1) {
            square = getValidCorner(SquareView.Compass.N, SquareView.Compass.E, i, j);
        } else if (i==rowNum-1 && j==0) {
            square = getValidCorner(SquareView.Compass.S, SquareView.Compass.W, i, j);
        } else if (i==rowNum-1 && j==colNum-1) {
            square = getValidCorner(SquareView.Compass.S, SquareView.Compass.E, i, j);
        } else if (i==0) {
            square = getValidEdge(SquareView.Compass.N, i, j);
        } else if (j==0) {
            square = getValidEdge(SquareView.Compass.W, i, j);
        } else if (i==rowNum-1) {
            square = getValidEdge(SquareView.Compass.S, i, j);
        } else if (j==colNum-1) {
            square = getValidEdge(SquareView.Compass.E, i, j);
        } else {
            square = getValidInner(i, j);
        }

        square.setOnClickListener(activity);
        return square;
    }

    private SquareView getValidInner(int i, int j) throws InstantiationException {

        Log.d(TAG, "getValidInner()");

        Junction.Type juncType = Junction.getRandomJuncType(Junction.JuncPosition.INNER);
        Junction junction = new Junction(juncType);
        SquareView square = new SquareView(activity, junction, i, j, tileSize);

        if (isValidInner(square)) {
            return findValidRandomInnerOrientation(square);
        } else {
            return getValidInner(i, j);
        }
    }

    private SquareView findValidRandomInnerOrientation(SquareView square) {
        while (true) {
            if (square.checkNorthernNeighbor() && square.checkWesternNeighbor()) {

                Log.d(TAG, "found valid inner orientation");
                if (getInnerChance()) {
                    Log.d(TAG, "coin toss heads");
                    return square;
                }
                Log.d(TAG, "coin toss tails");
                square.rotateClockwise(90);
            }
        }
    }

    private boolean isValidInner(SquareView square) {
        for (int r = 0; r < 4; r++) {
            if (square.checkNorthernNeighbor() && square.checkWesternNeighbor()) {

                Log.d(TAG, "found valid inner");
                return true;
            }
            square.rotateClockwise(90);
        }

        Log.d(TAG, "tried invalid inner");
        return false;
    }

    private SquareView getValidCorner(SquareView.Compass vert, SquareView.Compass horiz, int i, int j) throws InstantiationException {

        Log.d(TAG, "getValidCorner()");
        Junction.Type juncType = Junction.getRandomJuncType(Junction.JuncPosition.CORNER);
        Junction junction = new Junction(juncType);
        SquareView square = new SquareView(activity, junction, i, j, tileSize);

        if (isValidCorner(square, vert, horiz)) {
            return findValidRandomCornerOrientation(square, vert, horiz, juncType);
        } else {
            return getValidCorner(vert, horiz, i, j);
        }
    }

    private boolean isValidCorner(SquareView square, SquareView.Compass vert, SquareView.Compass horiz) {

        if (vert == SquareView.Compass.N && horiz == SquareView.Compass.W) {
            return true;
        }

        boolean checkEast = false;
        boolean checkSouth = false;

        if (vert == SquareView.Compass.S) { checkSouth = true; }
        if (horiz == SquareView.Compass.E) { checkEast = true; }

        for (int r = 0; r < 4; r++) {
            if (square.checkWesternNeighbor() &&
                    square.checkNorthernNeighbor() &&
                    square.checkEasternNeighbor(checkEast) &&
                    square.checkSouthernNeighbor(checkSouth)) {

                Log.d(TAG, "found valid corner");
                return true;
            }
            square.rotateClockwise(90);
        }
        Log.d(TAG, "tried invalid corner");
        return false;
    }

    private SquareView findValidRandomCornerOrientation(SquareView square, SquareView.Compass vert, SquareView.Compass horiz, Junction.Type juncType) {

        boolean checkEast = false;
        boolean checkSouth = false;

        if (vert == SquareView.Compass.S) { checkSouth = true; }
        if (horiz == SquareView.Compass.E) { checkEast = true; }

        while (true) {
            if (square.checkWesternNeighbor() &&
                    square.checkNorthernNeighbor() &&
                    square.checkSouthernNeighbor(checkSouth) &&
                    square.checkEasternNeighbor(checkEast)) {

                Log.d(TAG, "found valid inner orientation");
                if (getCornerChance(juncType)) {
                    Log.d(TAG, "coin toss heads");
                    return square;
                }
                Log.d(TAG, "coin toss tails");
            }
            square.rotateClockwise(90);
        }
    }

    private SquareView getValidEdge(SquareView.Compass edge, int i, int j) throws InstantiationException {

        Log.d(TAG, "getValidEdge()");

        Junction.Type juncType = Junction.getRandomJuncType(Junction.JuncPosition.EDGE);
        Junction junction = new Junction(juncType);
        SquareView square = new SquareView(activity, junction, i, j, tileSize);
        if (isValidEdge(square, edge)) {
            return findValidRandomEdgeOrientation(square, edge, juncType);
        } else {
            return getValidEdge(edge, i, j);
        }
    }

        private boolean isValidEdge(SquareView square, SquareView.Compass edge) {

        boolean checkEast = false;
        boolean checkSouth = false;

        if (edge == SquareView.Compass.S) {
            checkSouth = true;
        } else if (edge == SquareView.Compass.E) {
            checkEast = true;
        }

        for (int r = 0; r < 4; r++) {
            if (square.checkWesternNeighbor() &&
                    square.checkNorthernNeighbor() &&
                    square.checkEasternNeighbor(checkEast) &&
                    square.checkSouthernNeighbor(checkSouth)) {

                Log.d(TAG, "found valid edge");
                return true;
            }
            square.rotateClockwise(90);
        }
        Log.d(TAG, "tried invalid edge");
        return false;
    }

    private SquareView findValidRandomEdgeOrientation(SquareView square, SquareView.Compass edge, Junction.Type juncType) {

        boolean checkEast = false;
        boolean checkSouth = false;

        if (edge == SquareView.Compass.S) {
            checkSouth = true;
        } else if (edge == SquareView.Compass.E) {
            checkEast = true;
        }

        while (true) {
            if (square.checkWesternNeighbor() &&
                    square.checkNorthernNeighbor() &&
                    square.checkSouthernNeighbor(checkSouth) &&
                    square.checkEasternNeighbor(checkEast)) {

                Log.d(TAG, "found valid edge orientation");
                if (getEdgeChance(juncType)) {
                    Log.d(TAG, "coin toss heads");
                    return square;
                }
                Log.d(TAG, "coin toss tails");
            }
            square.rotateClockwise(90);
        }
    }

    private boolean getCornerChance(Junction.Type type) {
        /*
        switch (type) {
            case BLANK:
            case TURN:
                return true; // 1 in 1 chance
            case TERMINAL:
                return random.nextBoolean(); // 1 in 2 chance
        }
        */
        return true;
        //return random.nextBoolean();
    }

    private boolean getEdgeChance(Junction.Type type) {
        /*
        switch (type) {
            case STRAIGHT:
            case FORK:
            case BLANK:
                return true; // 1 in 1 chance
            case TURN:
                return random.nextBoolean(); // 1 in 2 chance
            case TERMINAL:
                return (random.nextInt(3) == 0); // 1 in 3 chance
        }
        */
        return true;
        //return random.nextBoolean();
    }

    private boolean getInnerChance() {
        // could be modified to account for the values of the northern and western junctions
        //return random.nextBoolean();
        return true;
    }


    private void fillWithBlanks() throws InstantiationException {
        for (int i = 0; i < rowNum; i++) {
            TableRow tableRow = new TableRow(activity);
            for (int j = 0; j < colNum; j++) {
                SquareView square = new SquareView(activity, Junction.create("BLANK"), i, j, tileSize);
                square.setOnClickListener(activity);
                tableRow.addView(square, tileSize, tileSize);
            }
            activity.gameTable.addView(tableRow);
        }
    }

}
