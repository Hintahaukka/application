package hifian.hintahaukka.GUI;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import hifian.hintahaukka.Domain.User;
import hifian.hintahaukka.R;
import hifian.hintahaukka.Service.HttpGetTask;
import hifian.hintahaukka.Service.LeaderboardUtils;


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

            List<User> leaderboard = LeaderboardUtils.parseLeaderboardFromJSONRespose(response);
            LeaderboardListAdapter adapter = new LeaderboardListAdapter(getContext(), R.layout.leaderboard_item, leaderboard);
            final ListView listView = getView().findViewById(R.id.list_leaderboard);
            listView.setAdapter(adapter);
        }
    }

}
