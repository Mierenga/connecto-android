package eskimwier.connecto.GamePlay;

import java.util.HashMap;
import java.util.Map;

import eskimwier.connecto.Engine.Junction;
import eskimwier.connecto.R;

/**
 * Created by mike on 5/22/2016.
 */
public class GameColors {

    public enum Skin {
        BLACK_AND_WHITE,
        BLUE,
        ORANGE,
        AZTEC,
        MARTIAN,
        NEON,
        NEWS,
        SPECTRO,
        SQUIG,
    }

    public static final GameColors Spectro = new GameColors(Skin.SPECTRO, Skin.SPECTRO);
    public static final GameColors Martian = new GameColors(Skin.MARTIAN, Skin.NEON);
    public static final GameColors Squig = new GameColors(Skin.SQUIG, Skin.SQUIG);
    public static final GameColors Orange = new GameColors(Skin.ORANGE, Skin.ORANGE);
    public static final GameColors Blue = new GameColors(Skin.BLUE, Skin.BLUE);

    private Skin incomplete;
    private Skin complete;

    public GameColors(Skin i, Skin c) {
        incomplete = i;
        complete = c;
    }

    Skin getIncompleteColor() {
        return incomplete;
    }

    Skin getCompleteColor() {
        return complete;
    }

    public static int findDrawable(Skin skin, Junction junction) {
        switch (skin) {
            case BLUE:
                return findBlueDrawable(junction);
            case ORANGE:
                return findOrangeDrawable(junction);
            case AZTEC:
                return findAztecDrawable(junction);
            case MARTIAN:
                return findMartianDrawable(junction);
            case NEON:
                return findNeonDrawable(junction);
            case NEWS:
                return findNewsDrawable(junction);
            case SPECTRO:
                return findSpectroDrawable(junction);
            case SQUIG:
                return findSquigDrawable(junction);
            default:
                return findBlackAndWhiteDrawable(junction);
        }

    }

    private static int findSquigDrawable(Junction j)
    {
        switch (j.type)
        {
            case BLANK:
                return R.drawable.blank_squig;
            case TERMINAL:
                return R.drawable.term_squig;
            case STRAIGHT:
                return R.drawable.strat_squig;
            case TURN:
                return R.drawable.turn_squig;
            case FORK:
                return R.drawable.fork_squig;
            case CROSS:
                return R.drawable.cross_squig;
            default:
                throw new IllegalArgumentException();
        }
    }

    private static int findSpectroDrawable(Junction j)
    {
        switch (j.type)
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

    private static int findBlueDrawable(Junction j)
    {
        switch (j.type)
        {
            case BLANK:
                return R.drawable.blank_blue;
            case TERMINAL:
                return R.drawable.term_blue;
            case STRAIGHT:
                return R.drawable.strat_blue;
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
    private static int findBlackAndWhiteDrawable(Junction j)
    {
        switch (j.type)
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
    private static int findOrangeDrawable(Junction j)
    {
        switch (j.type)
        {
            case BLANK:
                return R.drawable.blank_orange;
            case TERMINAL:
                return R.drawable.term_orange;
            case STRAIGHT:
                return R.drawable.strat_orange;
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

    private static int findAztecDrawable(Junction j)
    {
        switch (j.type)
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

    private static int findMartianDrawable(Junction j)
    {
        switch (j.type)
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

    private static int findNeonDrawable(Junction j)
    {
        switch (j.type)
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

    private static int findNewsDrawable(Junction j)
    {
        switch (j.type)
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
