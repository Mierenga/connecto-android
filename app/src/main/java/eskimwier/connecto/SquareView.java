package eskimwier.connecto;

import android.content.Context;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

/**
 * Created by eskimwier on 3/12/16.
 */
public class SquareView extends ImageView {

    public enum Color {
        BLACK_AND_WHITE,
        BLUE,
        ORANGE,
        AZTEC,
        MARTIAN,
        NEON,
        NEWS,
        SPECTRO
    }
    public enum Compass {
        N,
        S,
        E,
        W
    }


    private static Color _color = Color.BLACK_AND_WHITE;
    private static int _gridWidth = 0;
    private static int _gridHeight = 0;
    private static SquareView[][] _grid;

    private Junction _junction = new Junction();
    private int _row = 0;
    private int _col = 0;

    class Rotation {
        private int _degrees = 0;

        public void rotate(final int deg, boolean updateModel) {

            final int before = (_degrees);
            final int after = (before + deg);

            post(new Runnable() {
                @Override
                public void run() {
                    Animation animation = new RotateAnimation(before, after, getWidth()/2, getHeight()/2);
                    animation.setInterpolator(new DecelerateInterpolator());
                    animation.setDuration(300);
                    animation.setFillAfter(true);
                    animation.setFillEnabled(true);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override public void onAnimationStart(Animation animation) { }
                        @Override public void onAnimationRepeat(Animation animation) { }
                        @Override public void onAnimationEnd(Animation animation) {
                            Log.d("ROTATION", "before: " + Integer.toString(before) + ", after: " + Integer.toString(after));
                        }
                    });
                    startAnimation(animation);
                }
            });

            if (updateModel) {
                _degrees = reduceDegrees(after);
            }

        }

        public int get() {
            return _degrees;
        }

        private int reduceDegrees(int d) {
            if (d >= 360) {
                d -= 360;
                d = reduceDegrees(d);
            }
            return d;
        }

    }
    private Rotation _rotationDegrees = new Rotation();

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
                if (_grid[i][j] != null) {
                    _grid[i][j].updateInstanceColor();
                    _grid[i][j].setToModelPosition();
                }
            }
        }
    }

    public SquareView(Context context, Junction junction, int row, int col, int size) throws InstantiationException {
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
    }

    private void updateInstanceColor() {
        post(new Runnable() {
            @Override
            public void run() {

                setImageResource(findDrawableFromJunction());

                post(new Runnable() {
                    @Override
                    public void run() {
                        Animation shrink = new ScaleAnimation(1f, 0.5f, 1f, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        shrink.setInterpolator(new AccelerateDecelerateInterpolator());
                        shrink.setDuration(500);
                        startAnimation(shrink);
                    }
                });

                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation grow = new ScaleAnimation(0.5f, 1f, 0.5f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        grow.setInterpolator(new AccelerateDecelerateInterpolator());
                        grow.setDuration(500);
                        startAnimation(grow);
                    }
                }, 500);

            }
        });
    }

    public void setToModelPosition() {
        setPivotX(getWidth()/2);
        setPivotY(getHeight()/2);
        setRotation(_rotationDegrees.get());
    }

    public void rotateClockwise(final int degrees) {
        _junction.rotateJunctionClockwise(degrees);
        _rotationDegrees.rotate(degrees, true);
    }

    public boolean checkAllNeighbors() {
        if (!checkEasternNeighbor()) return false;
        if (!checkWesternNeighbor()) return false;
        if (!checkNorthernNeighbor()) return false;
        if (!checkSouthernNeighbor()) return false;
        return true;
    }

    public boolean checkNeighbor(Compass compass) {
        switch (compass) {
            case N:
                return checkNorthernNeighbor();
            case E:
                return checkEasternNeighbor();
            case S:
                return checkSouthernNeighbor();
            case W:
                return checkWesternNeighbor();
        }
        return false;
    }
    public boolean checkEasternNeighbor(boolean doCheck) {
        if (!doCheck) {
            return true;
        } else {
            return checkEasternNeighbor();
        }
    }
    public boolean checkWesternNeighbor(boolean doCheck) {
        if (!doCheck) {
            return true;
        } else {
            return checkWesternNeighbor();
        }
    }
    public boolean checkNorthernNeighbor(boolean doCheck) {
        if (!doCheck) {
            return true;
        } else {
            return checkNorthernNeighbor();
        }
    }
    public boolean checkSouthernNeighbor(boolean doCheck) {
        if (!doCheck) {
            return true;
        } else {
            return checkSouthernNeighbor();
        }
    }

    public boolean checkEasternNeighbor() {
        if (this._junction.east == getEasternJunction()) {
            return true;
        }
        return false;
    }
    public boolean checkWesternNeighbor() {
        if (this._junction.west == getWesternJunction()) {
            return true;
        }
        return false;
    }

    public boolean checkNorthernNeighbor() {
        if (this._junction.north == getNorthernJunction()) {
            return true;
        }
        return false;
    }

    public boolean checkSouthernNeighbor() {
        if (this._junction.south == getSouthernJunction()) {
            return true;
        }
        return false;
    }

    private boolean getEasternJunction() {
        try {
            int col = this._col + 1;
            Boolean val = _grid[this._row][col]._junction.west;
            return val;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }
    private boolean getWesternJunction() {
        try {
            int col = this._col - 1;
            Boolean val = _grid[this._row][col]._junction.east;
            return val;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }
    private boolean getNorthernJunction() {
        try {
            int row = this._row - 1;
            Boolean val = _grid[row][this._col]._junction.south;
            return val;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }
    private boolean getSouthernJunction() {
        try {
            int row = this._row + 1;
            Boolean val = _grid[row][this._col]._junction.north;
            return val;
        } catch (IndexOutOfBoundsException e) {
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
            case MARTIAN:
                return findMartianDrawable();
            case NEON:
                return findNeonDrawable();
            case NEWS:
                return findNewsDrawable();
            case SPECTRO:
                return findSpectroDrawable();
            default:
                return findBlackAndWhiteDrawable();
        }

    }

    private int findSpectroDrawable()
    {
        switch (_junction.type)
        {
            case BLANK:
                return R.drawable.blank_spectro;
            case TERMINAL:
                return R.drawable.term_spectro;
            case STRAIGHT:
                return R.drawable.strat_spectro;
            case TURN:
                return R.drawable.turn_spectro;
            case FORK:
                return R.drawable.fork_spectro;
            case CROSS:
                return R.drawable.cross_spectro;
            default:
                throw new IllegalArgumentException();
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

    private int findMartianDrawable()
    {
        switch (_junction.type)
        {
            case BLANK:
                return R.drawable.blank_martian;
            case TERMINAL:
                return R.drawable.term_martian;
            case STRAIGHT:
                return R.drawable.strat_martian;
            case TURN:
                return R.drawable.turn_martian;
            case FORK:
                return R.drawable.fork_martian;
            case CROSS:
                return R.drawable.cross_martian;
            default:
                throw new IllegalArgumentException();
        }
    }

    private int findNeonDrawable()
    {
        switch (_junction.type)
        {
            case BLANK:
                return R.drawable.blank_neon;
            case TERMINAL:
                return R.drawable.term_neon;
            case STRAIGHT:
                return R.drawable.strat_neon;
            case TURN:
                return R.drawable.turn_neon;
            case FORK:
                return R.drawable.fork_neon;
            case CROSS:
                return R.drawable.cross_neon;
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
