package eskimwier.connecto.Engine;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Path;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import java.util.Random;

import eskimwier.connecto.GamePlay.GameColors;

/**
 * Created by eskimwier on 3/12/16.
 */
public class SquareView extends ImageView {

    private static GameColors.Skin skin = GameColors.Skin.BLACK_AND_WHITE;
    private static int gridWidth = 0;
    private static int gridHeight = 0;
    private static SquareView[][] grid;

    public enum Compass {
        N,
        S,
        E,
        W
    }

    class Rotation {
        private int degrees = 0;

        public void rotate(final int deg, boolean updateModel) {

            final int before = degrees;
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
                degrees = reduceDegrees(after);
            }

        }

        public int get() {
            return degrees;
        }

        private int reduceDegrees(int d) {
            if (d >= 360) {
                d -= 360;
                d = reduceDegrees(d);
            }
            return d;
        }

    }

    private Junction junction = new Junction();
    private int row = 0;
    private int col = 0;
    private Rotation rotationDegrees = new Rotation();

    public static void startNewGame(int rows, int cols, GameColors.Skin color) {
        gridWidth = cols;
        gridHeight = rows;
        grid = new SquareView[gridHeight][gridWidth];
        skin = color;
    }

    public static void setGridColor(GameColors.Skin color, boolean animate) {

        if (!animate && skin == color) return;

        for (int i = 0; i < gridHeight; i++) {
            for (int j = 0; j < gridWidth; j++) {
                if (grid[i][j] != null) {
                    grid[i][j].setToModelPosition();
                    if (color != skin) {
                        grid[i][j].setImageResource(GameColors.findDrawable(color, grid[i][j].junction));
                    }
                    if (animate) {
                        grid[i][j].shrinkGrow();
                    }
                }
            }
        }
        skin = color;
    }

    public SquareView(Context context, Junction junction, int row, int col, int size) throws InstantiationException {
        super(context);
        checkGridSize();

        setJunction(junction);
        this.row = row;
        this.col = col;
        grid[row][col] = this;
    }

    private void checkGridSize() throws  InstantiationException {
        if (gridHeight < 1 || gridWidth < 1)
            throw new InstantiationException("Must use static method startNewGame()" +
                    "to set a positive grid size before instantiation of class members");
    }

    private void setJunction(Junction junction) {
        this.junction = junction;
        setImageResource(GameColors.findDrawable(skin, this.junction));
    }

    private void jumble() {
        final ImageView view = this;
        post(new Runnable() {
            @Override
            public void run() {

                final Path path1 = new Path();
                path1.quadTo(view.getX(), view.getY(), view.getX(), view.getY());
                ObjectAnimator.ofFloat(view, View.X, View.Y, path1).start();
                /*
                final Path path2 = new Path();
                path2.quadTo(view.getX(), view.getY(), view.getX(), view.getY());
                ObjectAnimator.ofFloat(view, View.X, View.Y, path2).start();
                */
            }
        });

    }

    private void spin() {
        post(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                int direction = random.nextBoolean()?1:-1;
                int rotations = random.nextInt(3) + 1;

                Animation animation = new RotateAnimation(0, 360*rotations*direction, getWidth()/2, getHeight()/2);
                animation.setInterpolator(new DecelerateInterpolator());
                animation.setDuration(1000);
                animation.setFillAfter(true);
                animation.setFillEnabled(true);
                startAnimation(animation);
            }
        });
    }

    private void shrinkGrow() {
        post(new Runnable() {
            @Override
            public void run() {

                int span = 500;
                Random random = new Random();
                int direction = random.nextBoolean()?1:-1;
                int rotations = random.nextInt(2) + 1;

                Animation spin = new RotateAnimation(0, 360*rotations*direction, getWidth()/2, getHeight()/2);
                spin.setDuration(span);
                spin.setFillAfter(true);
                spin.setFillEnabled(true);

                Animation shrink = new ScaleAnimation(1f, 0.10f, 1f, 0.10f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                shrink.setDuration(span);

                Animation grow = new ScaleAnimation(1f, 10f, 1f, 10f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                grow.setDuration(span);
                grow.setStartOffset(span);

                AnimationSet set = new AnimationSet(true);
                set.setInterpolator(new AccelerateDecelerateInterpolator());
                set.addAnimation(spin);
                set.addAnimation(shrink);
                set.addAnimation(grow);
                set.setDuration(span*2);

                startAnimation(set);
            }
        });
    }

    public void setToModelPosition() {
        setPivotX(getWidth()/2);
        setPivotY(getHeight()/2);
        setRotation(rotationDegrees.get());
    }

    public void rotateClockwise(final int degrees) {
        junction.rotateJunctionClockwise(degrees);
        rotationDegrees.rotate(degrees, true);
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
        if (this.junction.east == getEasternJunction()) {
            return true;
        }
        return false;
    }
    public boolean checkWesternNeighbor() {
        if (this.junction.west == getWesternJunction()) {
            return true;
        }
        return false;
    }

    public boolean checkNorthernNeighbor() {
        if (this.junction.north == getNorthernJunction()) {
            return true;
        }
        return false;
    }

    public boolean checkSouthernNeighbor() {
        if (this.junction.south == getSouthernJunction()) {
            return true;
        }
        return false;
    }

    private boolean getEasternJunction() {
        try {
            int col = this.col + 1;
            Boolean val = grid[this.row][col].junction.west;
            return val;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }
    private boolean getWesternJunction() {
        try {
            int col = this.col - 1;
            Boolean val = grid[this.row][col].junction.east;
            return val;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }
    private boolean getNorthernJunction() {
        try {
            int row = this.row - 1;
            Boolean val = grid[row][this.col].junction.south;
            return val;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }
    private boolean getSouthernJunction() {
        try {
            int row = this.row + 1;
            Boolean val = grid[row][this.col].junction.north;
            return val;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }



}
