package space.apple.three.memes;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by ankit on 26/3/18.
 */

public class SharedPref {
    Context context;

    public SharedPref(Context context) {
        this.context = context;
    }

    void saveLike(String ref) {


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(ref,true);
        editor.apply();

    }
    void saveDislike(String ref) {


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(ref,false);
        editor.apply();

    }

    boolean isLiked(String ref){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean flag= preferences.getBoolean(ref, false);

        return flag;
    }

}

