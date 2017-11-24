package ca.on.conestogac.swassignment2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ca.on.conestogac.swassignment1.R;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {
    private View screen;
    private TextView txtPlayerCards, txtDealerCards, txtMoney, txtDealerTotal, txtPlayerTotal;
    private TextView txtBust, txtPlayerName;
    private int playerTotal;
    private int dealerTotal;
    private List<Integer> playerCards = new ArrayList<Integer>();
    private List<Integer> dealerCards = new ArrayList<Integer>();
    private Button btnBet, btnHit, btnStay, btnFold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set all on-screen widgets
        screen = findViewById(android.R.id.content);
        txtPlayerName = (TextView) findViewById(R.id.playerLabel);
        txtPlayerTotal = (TextView) findViewById(R.id.playerTotal);
        txtDealerTotal = (TextView) findViewById(R.id.dealerTotal);
        txtPlayerCards = (TextView) findViewById(R.id.playerCards);
        txtDealerCards = (TextView) findViewById(R.id.dealerCards);
        txtMoney = (TextView) findViewById(R.id.money);
        txtBust = (TextView) findViewById(R.id.textBust);
        btnBet = (Button) findViewById(R.id.buttonBet);
        btnHit = (Button) findViewById(R.id.buttonHit);
        btnStay = (Button) findViewById(R.id.buttonStay);
        btnFold = (Button) findViewById(R.id.buttonFold);

        // Set listeners
        btnBet.setOnClickListener(this);
        btnHit.setOnClickListener(this);
        btnStay.setOnClickListener(this);
        btnFold.setOnClickListener(this);
        screen.setOnClickListener(this);

        // Get and display player name and money pool
        String playerName = getIntent().getStringExtra("playerName");
        txtPlayerName.setText(playerName);

        int difficulty = getIntent().getIntExtra("difficulty", 200);
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        txtMoney.setText(currency.format(difficulty));

        initialize();

        TaskListDB db = new TaskListDB(this);
        // Reading all shops
        Log.d("Reading: ", "Reading all tasks ...");
        ArrayList<Task> tasks = db.getTasks("1");
        for (Task task : tasks) {
            String log = "Id: " + task.getId() + " ,Name: " + task.getName() + " ,Notes: " + task.getNotes();
        // Writing shops to log
            Log.d("Task: : ", log);
        }
    }

    public void initialize() {
        txtBust.setVisibility(View.INVISIBLE);
        btnBet.setVisibility(View.INVISIBLE);
        btnHit.setVisibility(View.VISIBLE);
        btnStay.setVisibility(View.VISIBLE);
        btnFold.setVisibility(View.VISIBLE);
        playerTotal = 0;
        dealerTotal = 0;
        playerCards.clear();
        dealerCards.clear();
        addCard("player");
        addCard("player");
        addCard("dealer");
        addCard("dealer");
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.buttonHit:
                addCard("player");
                break;
            case R.id.buttonStay:
                //TODO
                break;
            case R.id.buttonFold:
                initialize();
                break;
            case R.id.buttonBet:
                initialize();
                break;
        }
    }

    public void addCard(String actor) {
        Random rand = new Random();
        int randomCard = rand.nextInt(13) + 1;
        if (actor == "player") {
            playerTotal += Math.min(randomCard, 10);
            playerCards.add(randomCard);
            txtPlayerCards.setText(playerCards.toString());
            txtPlayerTotal.setText("Total: " + Integer.toString(playerTotal));
        } else if (actor == "dealer") {
            dealerTotal += Math.min(randomCard, 10);
            dealerCards.add(randomCard);
            txtDealerCards.setText(dealerCards.toString());
            txtDealerTotal.setText("Total: " + Integer.toString(dealerTotal));
        }
        checkWinLoss();
    }

    public void checkWinLoss() {
        if (playerTotal > 21) {
            txtBust.setVisibility(View.VISIBLE);
            btnBet.setVisibility(View.VISIBLE);
            btnHit.setVisibility(View.INVISIBLE);
            btnStay.setVisibility(View.INVISIBLE);
            btnFold.setVisibility(View.INVISIBLE);
        }
    }
}