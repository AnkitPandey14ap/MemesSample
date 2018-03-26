package space.apple.three.memes;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by ankit on 26/3/18.
 */

public class SharedPref {
    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public SharedPref(Context context) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
    }

    void saveLike(String ref) {
        editor.putBoolean(ref,true);
        editor.apply();

    }
    void saveDislike(String ref) {
        editor.putBoolean(ref,false);
        editor.apply();

    }

    boolean isLiked(String ref){
        boolean flag= preferences.getBoolean(ref, false);
        return flag;
    }

    void register(){
        editor.putBoolean("REGISTER",true);
        editor.apply();
    }

    boolean isRegistered(){
        boolean flag= preferences.getBoolean("REGISTER", false);
        return flag;
    }

}

