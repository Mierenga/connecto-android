package eskimwier.connecto;

import android.content.Context;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

/**
 * Created by eskimwier on 3/12/16.
 */
public class SquareView extends ImageView {

    public enum Compass {
        N,
        S,
        E,
        W
    }


    private static GameColors.Color _color = GameColors.Color.BLACK_AND_WHITE;
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

    public static void startNewGame(int rows, int cols, GameColors.Color color) {
        _gridWidth = cols;
        _gridHeight = rows;
        _grid = new SquareView[_gridHeight][_gridWidth];
        _color = color;
    }

    public static void setGridColor(GameColors.Color color, boolean animate) {

        if (!animate && _color == color) return;

        for (int i = 0; i < _gridHeight; i++) {
            for (int j = 0; j < _gridWidth; j++) {
                if (_grid[i][j] != null) {
                    _grid[i][j].setToModelPosition();
                    if (color != _color) {
                        _grid[i][j].setImageResource(GameColors.findDrawable(color, _grid[i][j]._junction));
                    }
                    if (animate) {
                        _grid[i][j].shrinkGrow();
                    }
                }
            }
        }
        _color = color;
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
        setImageResource(GameColors.findDrawable(_color, _junction));
    }

    private void shrinkGrow() {
        post(new Runnable() {
            @Override
            public void run() {

                Animation shrink = new ScaleAnimation(1f, 0.5f, 1f, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                shrink.setDuration(500);

                Animation grow = new ScaleAnimation(1f, 2f, 1f, 2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                grow.setDuration(500);
                grow.setStartOffset(500);

                AnimationSet set = new AnimationSet(true);
                set.setInterpolator(new AccelerateDecelerateInterpolator());
                set.addAnimation(shrink);
                set.addAnimation(grow);
                set.setDuration(1000);

                startAnimation(set);
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



}
