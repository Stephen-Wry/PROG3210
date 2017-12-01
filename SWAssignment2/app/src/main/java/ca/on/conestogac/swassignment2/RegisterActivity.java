package ca.on.conestogac.swassignment2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    AssignmentDB db = new AssignmentDB(this);

    private EditText txtLoginName;
    private EditText txtPassword;
    private CheckBox chkAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button btnRegister = (Button) findViewById(R.id.buttonRegister);
        txtLoginName = (EditText) findViewById(R.id.txtLoginName);
        txtPassword  = (EditText) findViewById(R.id.txtPassword);
        chkAge = (CheckBox) findViewById(R.id.checkAge);

        // Set listeners
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonRegister:
                Register();
                break;
        }
    }

    public void Register() {
        String playerName = txtLoginName.getText().toString();
        String password = txtPassword.getText().toString();
        Boolean duplicateID = false;

        if (playerName == null || playerName.isEmpty()) {
            // TO-DO
        } else if (password == null || password.isEmpty()) {
            // TO-DO
        } else if (!chkAge.isChecked()) {
            // TO-DO
        } else {
            ArrayList<Player> players = db.getPlayers();
            for (Player player : players) {
                if (player.getName().equals(playerName))
                    duplicateID = true;
            }
            if (duplicateID == true) {
                // TO-DO
            } else {
                Player player = new Player (players.size() + 1, playerName, password, 1, 0, 100);
                db.insertPlayer(player);

                Intent openRegisterActivity = new Intent(RegisterActivity.this, LoginActivity.class);
                openRegisterActivity.putExtra("playerName", playerName);
                openRegisterActivity.putExtra("password", password);
                startActivity((openRegisterActivity));
                finish();
            }
        }
    }
}