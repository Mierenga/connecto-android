package eskimwier.connecto;

import java.util.Random;

/**
 * Created by eskimwier on 3/12/16.
 */
public class Junction {

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
        Random random = new Random();
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
                this.west = true;
                // fall through
            case FORK:
                this.east = true;
                // fall through
            case STRAIGHT:
                this.south = true;
                // fall through
            case TERMINAL:
                this.north = true;
                break;
            case TURN:
                this.north = true;
                this.east = true;
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

    private int getTrueSides()
    {
        int trueSides = 0;
        if (this.north) trueSides++;
        if (this.east) trueSides++;
        if (this.south) trueSides++;
        if (this.west) trueSides++;
        return trueSides;
    }
    public void rotateJunctionClockwise()
    {
        boolean prevNorth = this.north;
        boolean prevSouth = this.south;
        this.north = this.west;
        this.south = this.east;
        this.east = prevNorth;
        this.west = prevSouth;
    }

}
