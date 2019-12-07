package hifian.hintahaukka.GUI;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import hifian.hintahaukka.Domain.User;
import hifian.hintahaukka.R;
import hifian.hintahaukka.Service.HttpGetTask;


public class LeaderboardFragment extends Fragment {


    public LeaderboardFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        new GetLeaderboardTask().execute();
        return inflater.inflate(R.layout.fragment_leaderboard, container, false);
    }

    private class GetLeaderboardTask extends HttpGetTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.setUrlString("https://hintahaukka.herokuapp.com/test/getLeaderboard");
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            try {
                List<User> leaderboard = new ArrayList<>();
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

                // Create leaderboard ListView with the fetched leaderboard data
                LeaderboardListAdapter adapter = new LeaderboardListAdapter(getContext(), R.layout.leaderboard_item, leaderboard);
                final ListView listView = getView().findViewById(R.id.list_leaderboard);
                listView.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
