package eskimwier.connecto;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by eskimwier on 3/12/16.
 */
public class SquareView extends ImageView {

    private Junction _junction = new Junction();

    public SquareView(Context context)
    {
        super(context);
        setImageResource(findDrawableFromJunction(this._junction));
    }
    public SquareView(Context context, Junction junction)
    {
        super(context);
        _junction = junction;
        setImageResource(findDrawableFromJunction(_junction));
    }

    public void setJunction(Junction junction) {
        _junction = junction;
        findDrawableFromJunction(junction);
    }

    public void rotateImageClockwise()
    {
        _junction.rotateJunctionClockwise();
        setPivotX(getWidth()/2);
        setPivotY(getHeight()/2);
        setRotation(getRotation() + 90);
    }

    private int findDrawableFromJunction(Junction junction)
    {
        switch (junction.type)
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



}
