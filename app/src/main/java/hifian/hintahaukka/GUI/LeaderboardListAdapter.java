package hifian.hintahaukka.GUI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import hifian.hintahaukka.Domain.User;
import hifian.hintahaukka.R;

public class LeaderboardListAdapter extends ArrayAdapter<User> {
    Context context;
    int resource;
    List<User> leaderboard;

    public LeaderboardListAdapter(@NonNull Context context, int resource, @NonNull List<User> leaderboard) {
        super(context, resource, leaderboard);
        this.context = context;
        this.resource = resource;
        this.leaderboard = leaderboard;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.leaderboard_item, null);
        TextView positionView = view.findViewById(R.id.leaderboard_position);
        TextView nicknameView = view.findViewById(R.id.leaderboard_nickname);
        TextView pointsView = view.findViewById(R.id.leaderboard_points);

        User user = leaderboard.get(position);

        positionView.setText("" + (position + 1) + ".");
        nicknameView.setText(user.getNickname());
        pointsView.setText(String.valueOf(user.getPoints()));

        return view;
    }
}
