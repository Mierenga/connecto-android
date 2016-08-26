package eskimwier.connecto.GamePlay;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mike on 8/8/16.
 */
public class UserPrefs {

    private Context context;
    private static final String USER_PREFS = "user_prefs";


    public class Keys {
        public static final String skin = "skin";
        public static final String total_score = "total_score";
    }

    public UserPrefs(Context context) {
        this.context = context;
    }

    public void setTotalScore(int score) {
        set(Keys.total_score, score);
    }

    public int getTotalScore() {
        return get(Keys.total_score);
    }

    public void setSkin(GameColors.Skin skin) {
        set(Keys.skin, skin.ordinal());
    }

    public GameColors.Skin getSkin() {
        return GameColors.Skin.values()[get(Keys.skin)];
    }

    private void set(String key, int val) {
        SharedPreferences prefs = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, val);
        editor.commit();
    }

    private int get(String key) {
        SharedPreferences prefs = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        try {
            return prefs.getInt(key, 0);
        } catch (ClassCastException cce) {
            return 0;
        }
    }
}
