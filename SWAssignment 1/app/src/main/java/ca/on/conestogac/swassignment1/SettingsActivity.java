package ca.on.conestogac.swassignment1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txtPlayerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        txtPlayerName = (EditText) findViewById(R.id.player_name);
        Button btnEasy = (Button) findViewById(R.id.buttonEasy);
        Button btnNorm = (Button) findViewById(R.id.buttonNormal);
        Button btnHard = (Button) findViewById(R.id.buttonHard);

        // Set listeners
        btnEasy.setOnClickListener(this);
        btnNorm.setOnClickListener(this);
        btnHard.setOnClickListener(this);
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
        }
        String playerName = txtPlayerName.getText().toString();
        if (playerName == null || playerName.isEmpty()) {
            playerName = "Player";
        }
        Intent openMainActivity = new Intent(SettingsActivity.this, MainActivity.class);
        openMainActivity.putExtra("difficulty", difficulty);
        openMainActivity.putExtra("playerName", playerName);
        startActivity((openMainActivity));
        finish();
    }
}
