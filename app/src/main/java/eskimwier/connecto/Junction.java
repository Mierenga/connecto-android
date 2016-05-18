package eskimwier.connecto;

import java.util.Random;

/**
 * Created by eskimwier on 3/12/16.
 */
public class Junction {

    static Random random = new Random();

    public enum Type {
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
                return (Type.values())[random.nextInt(6)];
            case EDGE:
                return (Type.values())[random.nextInt(5)];
            case CORNER:
                return (Type.values())[random.nextInt(3)];
            default:
                return Type.BLANK;
        }
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
        switch (i) {
            case 0:
                return new Junction(Type.BLANK);
            case 1:
                return new Junction(Type.TERMINAL);
            case 2:
                return new Junction(Type.TURN);
            case 3:
                return new Junction(Type.FORK);
            case 4:
                return new Junction(Type.STRAIGHT);
            case 5:
                return new Junction(Type.CROSS);
            default:
                throw new IllegalArgumentException("Invalid Junction.Type. " + i + " is out of range");
        }
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
