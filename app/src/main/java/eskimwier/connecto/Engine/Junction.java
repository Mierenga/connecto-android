package eskimwier.connecto.Engine;

import java.util.Random;

/**
 * Created by eskimwier on 3/12/16.
 */
public class Junction {

    static Random random = new Random();

    public enum Type { // Keep in this order to not break other code
        BLANK,
        TERMINAL,
        TURN,
        FORK,
        STRAIGHT,
        CROSS
    }

    public enum JuncPosition {
        CORNER,
        EDGE,
        INNER
    }

    public static Type getRandomJuncType(JuncPosition pos)
    {
        switch (pos) {
            case INNER:
                return getRandomJuncType(6);
            case EDGE:
                return getRandomJuncType(5);
            case CORNER:
                return getRandomJuncType(3);
            default:
                return Type.BLANK;
        }
    }

    private static Type getRandomJuncType(int lessThan) {
        // if we get a terminal in the inner puzzle, roll again to reduce the chances
        //   terminals in inner puzzle
        int t = random.nextInt(lessThan);
        for (int i=0; i<1; i++){
            if (t == Type.TERMINAL.ordinal() || t == Type.BLANK.ordinal()) {
                t = random.nextInt(lessThan);
            } else {
                break;
            }
        }
        return (Type.values())[t];
    }



    public boolean north, east, south, west;
    public Type type;
    public int rotations = 0;

    public Junction(Type type)
    {
        this.type = type;
        switch (type)
        {
            case BLANK:
                break;
            case CROSS:
                this.north = true;
                this.south = true;
                this.east = true;
                this.west = true;
                break;
            case STRAIGHT:
                this.south = true;
                this.north = true;
                break;
            case FORK:
                this.north = true;
                this.west = true;
                this.east = true;
                break;
            case TERMINAL:
                this.north = true;
                break;
            case TURN:
                this.north = true;
                this.west= true;
                break;
        }
    }
    public Junction() {
        this.type = Type.BLANK;
    }

    public boolean isBlank() {
        if (getTrueSides() == 0)
            return true;
        return false;
    }

    public boolean isCross() {
        if (getTrueSides() == 4)
            return true;
        return false;
    }

    public boolean isFork() {
        if (getTrueSides() == 3)
            return true;
        return false;
    }

    public boolean isStraight() {
        if (getTrueSides() == 2) {
            return this.north ^ this.south;
        }
        return false;
    }

    public boolean isTurn() {
        if (getTrueSides() == 2) {
            return !isStraight();
        }
        return false;
    }

    public static Junction create(String str) {
        switch (str.toUpperCase()) {
            case "TURN":
                return new Junction(Type.TURN);
            case "STRAT":
                return new Junction(Type.STRAIGHT);
            case "TERM":
                return new Junction(Type.TERMINAL);
            case "FORK":
                return new Junction(Type.FORK);
            case "CROSS":
                return new Junction(Type.CROSS);
            case "BLANK":
                return new Junction(Type.BLANK);
            default:
                throw new IllegalArgumentException("Invalid Junction.Type [" + str + "]");
        }
    }

    public static Junction create(int i) {
        return new Junction(Type.values()[i]);
    }


    private int getTrueSides()
    {
        int trueSides = 0;
        if (this.north) trueSides++;
        if (this.east) trueSides++;
        if (this.south) trueSides++;
        if (this.west) trueSides++;
        return trueSides;
    }
    public int rotateJunctionClockwise(int degrees) {

        for (int i = 0; i < degrees; i+=90) {
            boolean north = this.north;
            this.north = this.west;
            this.west = this.south;
            this.south = this.east;
            this.east = north;
            this.rotations++;
        }
        return this.rotations % 4;
    }
}
