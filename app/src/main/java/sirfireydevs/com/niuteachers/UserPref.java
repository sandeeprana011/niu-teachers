package sirfireydevs.com.niuteachers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import sirfireydevs.com.niuteachers.api.models.Teachers;

/**
 * Created by sandeeprana on 10/09/17.
 * License is only applicable to individuals and non-profits
 * and that any for-profit company must
 * purchase a different license, and create
 * a second commercial license of your
 * choosing for companies
 */

class UserPref {

    public static boolean isLoggedIn(Context context) {
        String teacher = getSharedPreferences(context).getString(Constants.TEACHER, null);
        return teacher != null;
    }

    public static Teachers getTeacher(Context context) {
        String teacher = getSharedPreferences(context).getString(Constants.TEACHER, null);
        if (teacher == null) return null;

        return new Gson().fromJson(teacher, Teachers.class);
    }


    public static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void saveTeacher(Context context, String teachJson) {
        getSharedPreferences(context).edit().putString(Constants.TEACHER, teachJson).commit();
    }

}
