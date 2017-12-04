package ca.on.conestogac.swassignment2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    AssignmentDB db = new AssignmentDB(this);

    private EditText txtLoginName;
    private EditText txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = (Button) findViewById(R.id.buttonLogin);
        Button btnRegister = (Button) findViewById(R.id.buttonRegister);

        txtLoginName = (EditText) findViewById(R.id.txtLoginName);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

        // Set listeners
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

        Log.d("Reading: ", "Reading all players ...");
        ArrayList<Player> players = db.getPlayers();
        for (Player player : players) {
            String log = "Id: " + player.getId() + ", Name: " + player.getName() + ", Password: " + player.getPassword();
            Log.d("Player: : ", log);
        }

        Log.d("Reading: ", "Reading all cards ...");
        ArrayList<Card> cards = db.getCards();
        for (Card card : cards) {
            String log = "Id: " + card.getId() + ", Value: " + card.getValue() + ", Type: " + card.getType();
            Log.d("Card: : ", log);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonLogin:
                Login();
                break;
            case R.id.buttonRegister:
                Register();
                break;
        }
    }

    public void Login() {
        String playerName = txtLoginName.getText().toString();
        if (playerName == null || playerName.isEmpty()) {
            // NULL
        } else {
            Log.d("Reading: ", "Reading player...");
            Player player = db.getPlayer(playerName);
            if (player.getPassword().equals(txtPassword.getText().toString())) {
                Intent openMainActivity = new Intent(LoginActivity.this, MainActivity.class);
                openMainActivity.putExtra("playerName", player.getName());
                startActivity((openMainActivity));
                finish();
            } else {
                //wrong password
            }
        }
    }

    public void Register() {
        String playerName = txtLoginName.getText().toString();
        String password = txtPassword.getText().toString();
        Intent openRegisterActivity = new Intent(LoginActivity.this, RegisterActivity.class);
        openRegisterActivity.putExtra("playerName", playerName);
        openRegisterActivity.putExtra("password", password);
        startActivity((openRegisterActivity));
        finish();
    }
}

