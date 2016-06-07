package eskimwier.connecto;

/**
 * Created by mike on 5/22/2016.
 */
public class DifficultyMode {

    final public static DifficultyMode Simple = new DifficultyMode(2,2);
    final public static DifficultyMode Easy = new DifficultyMode(3,4);
    final public static DifficultyMode Intermediate = new DifficultyMode(6,8);
    final public static DifficultyMode Advanced = new DifficultyMode(8,12);

    private int width;
    private int height;

    public DifficultyMode(int w, int h) {
        width = w;
        height = h;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getArea()
    {
        return width * height;
    }
}
