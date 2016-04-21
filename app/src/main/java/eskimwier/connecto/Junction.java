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
        STRAIGHT,
        FORK,
        CROSS,
        TURN
    }

    public static Type getRandomJuncType()
    {
        return (Type.values())[random.nextInt((Type.values()).length)];
    }

    public boolean north, east, south, west;
    public Type type;

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

    private int getTrueSides()
    {
        int trueSides = 0;
        if (this.north) trueSides++;
        if (this.east) trueSides++;
        if (this.south) trueSides++;
        if (this.west) trueSides++;
        return trueSides;
    }
    public void rotateJunctionClockwise(int degrees) {

        for (int i = 0; i < degrees; i+=90) {
            boolean prevNorth = this.north;
            boolean prevSouth = this.south;
            this.north = this.west;
            this.south = this.east;
            this.east = prevNorth;
            this.west = prevSouth;
        }
    }

}
