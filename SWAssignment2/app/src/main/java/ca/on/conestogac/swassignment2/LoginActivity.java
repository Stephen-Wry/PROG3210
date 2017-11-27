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

        Button btnEasy = (Button) findViewById(R.id.buttonEasy);
        Button btnNorm = (Button) findViewById(R.id.buttonNormal);
        Button btnHard = (Button) findViewById(R.id.buttonHard);
        Button btnLogin = (Button) findViewById(R.id.buttonLogin);
        Button btnRegister = (Button) findViewById(R.id.buttonRegister);

        txtLoginName = (EditText) findViewById(R.id.txtLoginName);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

        // Set listeners
        btnEasy.setOnClickListener(this);
        btnNorm.setOnClickListener(this);
        btnHard.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

        Log.d("Reading: ", "Reading all players ...");
        ArrayList<Player> players = db.getPlayers();
        for (Player player : players) {
            String log = "Id: " + player.getId() + " ,Name: " + player.getName() + " ,Password: " + player.getPassword();
            // Writing players to log
            Log.d("Task: : ", log);
        }
    }

    @Override
    public void onClick(View view) {
        int difficulty = 200;
        switch (view.getId()) {
            case R.id.buttonEasy:
                difficulty = 200;
                break;
            case R.id.buttonNormal:
                difficulty = 100;
                break;
            case R.id.buttonHard:
                difficulty = 50;
                break;
            case R.id.buttonLogin:
                Login(100);
                break;
            case R.id.buttonRegister:
                Register();
                break;
        }
    }

    public void Login(int difficulty) {
        String playerName = txtLoginName.getText().toString();
        if (playerName == null || playerName.isEmpty()) {
            // NULL
        } else {
            Log.d("Reading: ", "Reading player...");
            Player player = db.getPlayer(playerName);
            if (player.getPassword().equals(txtPassword.getText().toString())) {
                Intent openMainActivity = new Intent(LoginActivity.this, MainActivity.class);
                openMainActivity.putExtra("difficulty", difficulty);
                openMainActivity.putExtra("playerName", playerName);
                startActivity((openMainActivity));
                finish();
            } else {
                String log = "Id: " + player.getId() + " ,Name: " + player.getName() + " ,Password: " + player.getPassword();
                // Writing players to log
                Log.d("Task: : ", log);
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

