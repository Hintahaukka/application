package hifian.hintahaukka.Service;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import hifian.hintahaukka.GUI.MainActivity;
import hifian.hintahaukka.R;

/**
 * Class for managing user's personal information
 */
public class UserManager {

    Context context;
    SharedPreferences sharedPreferences;

    /**
     * Creates UserManager for the given context
     * @param context current Context or Activity
     */
    public UserManager(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }

    /**
     * This constructor can be used if UserManager is needed in tests
     * @param spcontext This context is used to create sharedPreferences (e.g. mock context created by SPMockBuilder)
     * @param mockcontext This context is used to access strings (e.g. mock context created by Mockito)
     */
    public UserManager(Context spcontext, Context mockcontext) {
        this.context = mockcontext;
        this.sharedPreferences = spcontext.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }

    /**
     * Gets user's total points from memory
     * @return total points
     */
    public int getPointsTotal() {
        return sharedPreferences.getInt(context.getString(R.string.key_points_total), 0);
    }

    /**
     * Gets user's unused points from memory
     * @return unused points
     */
    public int getPointsUnused() {
        return sharedPreferences.getInt(context.getString(R.string.key_points_unused), 0);
    }

    /**
     * Defines user's rank based on total points
     * @return user's rank
     */
    public String getRank() {
        int pointsTotal = getPointsTotal();
        String rank = context.getString(R.string.rank_1);
        if (pointsTotal > 1000) {
            rank = context.getString(R.string.rank_8);
        } else if (pointsTotal > 500) {
            rank = context.getString(R.string.rank_7);
        } else if (pointsTotal > 350) {
            rank = context.getString(R.string.rank_6);
        } else if (pointsTotal > 230) {
            rank = context.getString(R.string.rank_5);
        } else if (pointsTotal > 140) {
            rank = context.getString(R.string.rank_4);
        } else if (pointsTotal > 70) {
            rank = context.getString(R.string.rank_3);
        } else if (pointsTotal > 25) {
            rank = context.getString(R.string.rank_2);
        }
        return rank;
    }

    /**
     * Gets user's id from memory
     * @return user's unique id
     */
    public String getUserId() {
        return sharedPreferences.getString(context.getString(R.string.key_user_id), null);
    }

    /**
     * Gets user's nickname from memory
     * @return username
     */
    public String getUserName() {
        return sharedPreferences.getString(context.getString(R.string.key_user_name), null);
    }

    /**
     * Saves user's id to memory
     * @param userId User's unique id
     */
    public void setUserId(String userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.key_user_id), userId);
        editor.apply();
    }

    /**
     * Saves user's nickname to memory
     * @param userName User's nickname
     */
    public void setUserName(String userName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.key_user_name), userName);
        editor.apply();
    }

    /**
     * Updates user's points to memory
     * @param pointsTotal total points earned
     * @param pointsUnused unused points
     */
    public void updatePoints(int pointsTotal, int pointsUnused) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(context.getString(R.string.key_points_total), pointsTotal);
        editor.putInt(context.getString(R.string.key_points_unused), pointsUnused);
        editor.apply();
        updatePointsToUIView();
    }

    /**
     * Update's user's points to the navigation drawer header
     */
    public void updatePointsToUIView() {
        String userName = getUserName();
        int pointsTotal = getPointsTotal();
        int pointsUnused = getPointsUnused();
        String rank = getRank();

        NavigationView navigationView;
        try {
            navigationView = ((MainActivity) context).findViewById(R.id.navView);
        } catch (ClassCastException e) {
            // In testing environment, casting to MainActivity causes exception.
            // In that case don't update the points to the UI
            return;
        }

        View headerView = navigationView.getHeaderView(0);
        TextView pointsField = (TextView) headerView.findViewById(R.id.pointsField);
        SpannableString spanString = new SpannableString(userName + "\nTaso: " + rank + "\nPisteet: "+ pointsTotal + "\nKäytettävissä: " + pointsUnused + "\n");
        spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
        pointsField.setText(spanString);
    }

}
