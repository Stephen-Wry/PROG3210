package ca.on.conestogac.swassignment2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

    AssignmentDB db = new AssignmentDB(this);

    private TextView txtValidation;
    private EditText txtID, txtName, txtPassword, txtLevel, txtXP, txtCash, txtGetPlayer;
    private RadioButton rdoCreate, rdoUpdate, rdoDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        rdoCreate = (RadioButton) findViewById(R.id.rdoCreate);
        rdoUpdate = (RadioButton) findViewById(R.id.rdoUpdate);
        rdoDelete = (RadioButton) findViewById(R.id.rdoDelete);
        Button btnExecute = (Button) findViewById(R.id.btnExecute);
        Button btnPlay = (Button) findViewById(R.id.btnPlay);
        Button btnReturn = (Button) findViewById(R.id.btnReturn);
        Button btnGet = (Button) findViewById(R.id.btnGet);

        txtGetPlayer = (EditText) findViewById(R.id.txtGetID);
        txtID = (EditText) findViewById(R.id.txtID);
        txtName = (EditText) findViewById(R.id.txtName);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtLevel = (EditText) findViewById(R.id.txtLevel);
        txtXP = (EditText) findViewById(R.id.txtXP);
        txtCash = (EditText) findViewById(R.id.txtCash);
        txtValidation = (TextView) findViewById(R.id.txtValidation);

        // Set listeners
        btnExecute.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnReturn.setOnClickListener(this);
        btnGet.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnExecute:
                executeCommand();
                break;
            case R.id.btnPlay:
                playGame();
                break;
            case R.id.btnReturn:
                returnToLogin();
                break;
            case R.id.btnGet:
                getPlayer(txtGetPlayer.getText().toString());
                break;
        }
    }

    public void executeCommand() {
        txtValidation.setText("");
        String id = txtID.getText().toString();
        String name = txtName.getText().toString();
        String password = txtPassword.getText().toString();
        String level = txtLevel.getText().toString();
        String xp = txtXP.getText().toString();
        String cash = txtCash.getText().toString();

        Boolean valid = true;
        ArrayList<Player> players = db.getPlayers();

        if (password.isEmpty())
            password = "password";
        if (level.isEmpty())
            level = "1";
        if (xp.isEmpty())
            xp = "0";
        if (cash.isEmpty())
            cash = "100";

        if (rdoCreate.isChecked()) {
            if (id.isEmpty()) {
                txtValidation.setText("ID value must be specified");
                valid = false;
            } else {
                for (Player player : players) {
                    if (player.getId() == Integer.parseInt(id)) {
                        txtValidation.setText("ID value is already in use");
                        valid = false;
                    }
                }
            }
            if (name.isEmpty()) {
                txtValidation.setText("NAME value must be specified");
                valid = false;
            } else {
                for (Player player : players) {
                    if (player.getName().equals(name)) {
                        txtValidation.setText("NAME value is already in use");
                        valid = false;
                    }
                }
            }
            if (valid == true) {
                Player player = new Player(Integer.parseInt(id), name, password,
                        Integer.parseInt(level), Integer.parseInt(xp), Integer.parseInt(cash)
                );
                db.insertPlayer(player);
                txtValidation.setText("Player created");
                clearFields();
            }
        } else if (rdoDelete.isChecked()) {
            if (txtID.getText().toString().equals("1")) {
                txtValidation.setText("Cannot delete admin");
            } else {
                int rowCount = db.deletePlayer(txtID.getText().toString());
                if (rowCount == 1) {
                    txtValidation.setText("Player deleted");
                } else {
                    txtValidation.setText("Delete failed");
                }
                clearFields();
            }
        } else if (rdoUpdate.isChecked()) {
            if (id.isEmpty()) {
                txtValidation.setText("ID value must be specified");
                valid = false;
            }
            if (name.isEmpty()) {
                txtValidation.setText("NAME value must be specified");
                valid = false;
            }
            if (valid == true) {
                Player player = new Player(Integer.parseInt(id), name, password,
                        Integer.parseInt(level), Integer.parseInt(xp), Integer.parseInt(cash)
                );
                int rowCount = db.updatePlayer(player);
                if (rowCount == 1) {
                    txtValidation.setText("Player updated");
                } else {
                    txtValidation.setText("Update failed");
                }
                clearFields();
            }
        }
    }

    public void playGame() {
        Intent openActivity = new Intent(AdminActivity.this, MainActivity.class);
        openActivity.putExtra("playerName", "admin");
        startActivity((openActivity));
        finish();
    }

    public void returnToLogin() {
        startActivity(new Intent(AdminActivity.this, LoginActivity.class));
        finish();
    }

    public void clearFields() {
        txtID.setText("");
        txtName.setText("");
        txtPassword.setText("");
        txtLevel.setText("");
        txtXP.setText("");
        txtCash.setText("");
    }

    public void getPlayer(String playerName) {
        txtGetPlayer.setText("");
        if (!playerName.isEmpty()) {
            Player player = db.getPlayer(playerName);
            if (player == null) {
                txtValidation.setText("Player does not exist");
                clearFields();
            } else {
                txtID.setText(Integer.toString(player.getId()));
                txtName.setText(player.getName());
                txtPassword.setText(player.getPassword());
                txtLevel.setText(Integer.toString(player.getLevel()));
                txtXP.setText(Integer.toString(player.getXp()));
                txtCash.setText(Integer.toString(player.getCash()));
            }
        }
    }
}