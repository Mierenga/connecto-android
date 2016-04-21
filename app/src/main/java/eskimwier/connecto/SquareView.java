package eskimwier.connecto;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import java.util.Random;

/**
 * Created by eskimwier on 3/12/16.
 */
public class SquareView extends ImageView {

    public enum Color {
        BLACK_AND_WHITE,
        BLUE,
        ORANGE,
        AZTEC,
        NEWS
    }


    private static Color _color = Color.BLACK_AND_WHITE;
    private static int _gridWidth = 0;
    private static int _gridHeight = 0;
    private static SquareView[][] _grid;
    private static Random _random = new Random();


    private Junction _junction = new Junction();
    private int _row = 0;
    private int _col = 0;

    public static void startNewGame(int rows, int cols, Color color) {
        _gridWidth = cols;
        _gridHeight = rows;
        _grid = new SquareView[_gridHeight][_gridWidth];
        _color = color;
    }

    public static void setGridColor(Color color) {
        _color = color;
        for (int i = 0; i < _gridHeight; i++) {
            for (int j = 0; j < _gridWidth; j++) {
                if (_grid[i][j] != null)
                    _grid[i][j].updateInstanceColor();
            }
        }

    }

    public SquareView(Context context, Junction junction, int row, int col) throws InstantiationException {
        super(context);
        checkGridSize();

        setJunction(junction);
        _row = row;
        _col = col;
        _grid[row][col] = this;
    }

    private void checkGridSize() throws  InstantiationException {
        if (_gridHeight < 1 || _gridWidth < 1)
            throw new InstantiationException("Must use static method startNewGame()" +
                    "to set a positive grid size before instantiation of class members");
    }

    private void setJunction(Junction junction) {
        _junction = junction;
        setImageResource(findDrawableFromJunction());
        //rotateClockwise(_random.nextInt(4) * 90);
    }

    private void updateInstanceColor() {
        setImageResource(findDrawableFromJunction());
    }

    public void rotateClockwise(int degrees)
    {
        _junction.rotateJunctionClockwise(degrees);
        setPivotX(getWidth()/2);
        setPivotY(getHeight()/2);
        setRotation(getRotation() + degrees);
    }
    public boolean checkAllNeighbors() {
        Log.d("(" + _row + ", " + _col + ")", "Checking neighbors:");
        if (!checkEasternNeighbor()) return false;
        if (!checkWesternNeighbor()) return false;
        if (!checkNorthernNeighbor()) return false;
        if (!checkSouthernNeighbor()) return false;
        Log.d("ALL", "true");
        return true;
    }

    private boolean checkEasternNeighbor() {
        if (this._junction.east == getEasternJunction()) {
            Log.d("Eastern", "true");
            return true;
        }
        Log.d("Eastern", "false");
        return false;
    }
    private boolean checkWesternNeighbor() {
        if (this._junction.west == getWesternJunction()) {
            Log.d("Western", "true");
            return true;
        }
        Log.d("Western", "false");
        return false;
    }

    private boolean checkNorthernNeighbor() {
        if (this._junction.north == getNorthernJunction()) {
            Log.d("Northern", "true");
            return true;
        }
        Log.d("Northern", "false");
        return false;
    }

    private boolean checkSouthernNeighbor() {
        if (this._junction.south == getSouthernJunction()) {
            Log.d("Southern", "true");
            return true;
        }
        Log.d("Southern", "false");
        return false;
    }

    private boolean getEasternJunction() {
        try {
            int col = this._col + 1;
            Boolean val = _grid[this._row][col]._junction.west;
            Log.d("E", val.toString());
            return val;
        } catch (IndexOutOfBoundsException e) {
            Log.d("E", "false");
            return false;
        }
    }
    private boolean getWesternJunction() {
        try {
            int col = this._col - 1;
            Boolean val = _grid[this._row][col]._junction.east;
            Log.d("W", val.toString());
            return val;
        } catch (IndexOutOfBoundsException e) {
            Log.d("W", "false");
            return false;
        }
    }
    private boolean getNorthernJunction() {
        try {
            int row = this._row - 1;
            Boolean val = _grid[row][this._col]._junction.south;
            Log.d("N", val.toString());
            return val;
        } catch (IndexOutOfBoundsException e) {
            Log.d("N", "false");
            return false;
        }
    }
    private boolean getSouthernJunction() {
        try {
            int row = this._row + 1;
            Boolean val = _grid[row][this._col]._junction.north;
            Log.d("S", val.toString());
            return val;
        } catch (IndexOutOfBoundsException e) {
            Log.d("S", "false");
            return false;
        }
    }

    private int findDrawableFromJunction() {
        switch (_color) {
            case BLUE:
                return findBlueDrawable();
            case ORANGE:
                return findOrangeDrawable();
            case AZTEC:
                return findAztecDrawable();
            case NEWS:
                return findNewsDrawable();
            default:
                return findBlackAndWhiteDrawable();
        }

    }

    private int findBlueDrawable()
    {
        switch (_junction.type)
        {
            case BLANK:
                return R.drawable.blank_blue;
            case TERMINAL:
                return R.drawable.terminal_blue;
            case STRAIGHT:
                return R.drawable.straight_blue;
            case TURN:
                return R.drawable.turn_blue;
            case FORK:
                return R.drawable.fork_blue;
            case CROSS:
                return R.drawable.cross_blue;
            default:
                throw new IllegalArgumentException();
        }
    }
    private int findBlackAndWhiteDrawable()
    {
        switch (_junction.type)
        {
            case BLANK:
                return R.drawable.blank;
            case TERMINAL:
                return R.drawable.terminal;
            case STRAIGHT:
                return R.drawable.straight;
            case TURN:
                return R.drawable.turn;
            case FORK:
                return R.drawable.fork;
            case CROSS:
                return R.drawable.cross;
            default:
                throw new IllegalArgumentException();
        }
    }
    private int findOrangeDrawable()
    {
        switch (_junction.type)
        {
            case BLANK:
                return R.drawable.blank_orange;
            case TERMINAL:
                return R.drawable.terminal_orange;
            case STRAIGHT:
                return R.drawable.straight_orange;
            case TURN:
                return R.drawable.turn_orange;
            case FORK:
                return R.drawable.fork_orange;
            case CROSS:
                return R.drawable.cross_orange;
            default:
                throw new IllegalArgumentException();
        }
    }

    private int findAztecDrawable()
    {
        switch (_junction.type)
        {
            case BLANK:
                return R.drawable.blank_aztec;
            case TERMINAL:
                return R.drawable.terminal_aztec;
            case STRAIGHT:
                return R.drawable.straight_aztec;
            case TURN:
                return R.drawable.turn_aztec;
            case FORK:
                return R.drawable.fork_aztec;
            case CROSS:
                return R.drawable.cross_aztec;
            default:
                throw new IllegalArgumentException();
        }
    }

    private int findNewsDrawable()
    {
        switch (_junction.type)
        {
            case BLANK:
                return R.drawable.blank_news;
            case TERMINAL:
                return R.drawable.terminal_news;
            case STRAIGHT:
                return R.drawable.straight_news;
            case TURN:
                return R.drawable.turn_news;
            case FORK:
                return R.drawable.fork_news;
            case CROSS:
                return R.drawable.cross_news;
            default:
                throw new IllegalArgumentException();
        }
    }

}
