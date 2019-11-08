package hifian.hintahaukka.GUI;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import hifian.hintahaukka.R;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;


public class createUsernameFragment extends Fragment {

    private TextView usernameField;
    private Button sendUsernameButton;

    public createUsernameFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_username, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usernameField = (TextView) getView().findViewById(R.id.usernameField);
        sendUsernameButton = (Button) getView().findViewById(R.id.sendUsernameButton);

        sendUsernameButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                /*if (checkIfUserNameIsLongEnough(usernameField.getText().toString()) == false) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Käyttäjätunnuksen tulee olla vähintään 5 merkkiä pitkä!", Toast.LENGTH_LONG);
                }
                if (checkIfUserNameIsTaken(usernameField.getText().toString()) == false) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Käyttäjätunnus on jo käytössä!", Toast.LENGTH_LONG);
                }

                if (checkIfUserNameIsTaken(usernameField.getText().toString()) == true && checkIfUserNameIsLongEnough(usernameField.getText().toString()) == true) {
                    //GET ID
                    //Send username to backend with ID
                }*/

            }
        });

    }


    //Method to check if username is taken
    /*public boolean checkIfUserNameIsTaken(String username) {
        //Will be implemented later
    }*/


    //Method to check username is atleast 5 characters long
    public boolean checkIfUserNameIsLongEnough(String username) {
        if (username.length() < 5) {
            return false;
        }
        return true;
    }

}
