package hifian.hintahaukka.Service;

import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import hifian.hintahaukka.Domain.User;

public class LeaderboardUtils {

    public static List<User> parseLeaderboardFromJSONRespose(String response) {
        List<User> leaderboard = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(response);
            for (int i = 0; i < array.length(); i++) {
                JSONObject userObject = array.getJSONObject(i);

                // Ignore possible users without a nickname
                String nickname;
                try {
                    nickname = userObject.getString("nickname");
                } catch (Exception e) {
                    continue;
                }

                int points = userObject.getInt("points");
                leaderboard.add(new User(nickname, points));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return leaderboard;
    }
}
