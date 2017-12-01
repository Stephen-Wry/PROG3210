package ca.on.conestogac.swassignment2;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private View screen;
    private TextView txtPlayerCards, txtDealerCards, txtMoney, txtDealerTotal, txtPlayerTotal;
    private ArrayList<ImageView> playerImages = new ArrayList<>();
    private ArrayList<ImageView> dealerImages = new ArrayList<>();
    private TextView txtBust, txtPlayerName;
    private int playerTotal, dealerTotal;
    private int playerCount, dealerCount;
    private List<Integer> playerCards = new ArrayList<Integer>();
    private List<Integer> dealerCards = new ArrayList<Integer>();

    private List<Drawable> drawables = new ArrayList<>();
    private Button btnBet, btnHit, btnStay, btnFold;

    AssignmentDB db = new AssignmentDB(this);

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

        playerImages.add((ImageView) findViewById(R.id.imgPlayer3));
        playerImages.add((ImageView) findViewById(R.id.imgPlayer2));
        playerImages.add((ImageView) findViewById(R.id.imgPlayer3));
        playerImages.add((ImageView) findViewById(R.id.imgPlayer4));
        playerImages.add((ImageView) findViewById(R.id.imgPlayer5));


        //drawables.add(R.drawable.card41);

        /*
        dealerImages.add((ImageView) findViewById(R.id.imgDealer1));
        dealerImages.add((ImageView) findViewById(R.id.imgDealer2));
        dealerImages.add((ImageView) findViewById(R.id.imgDealer3));
        dealerImages.add((ImageView) findViewById(R.id.imgDealer4));
        dealerImages.add((ImageView) findViewById(R.id.imgDealer5));
        */

        // Set listeners
        btnBet.setOnClickListener(this);
        btnHit.setOnClickListener(this);
        btnStay.setOnClickListener(this);
        btnFold.setOnClickListener(this);
        screen.setOnClickListener(this);

        // Get and display player name and money pool
        String playerName = getIntent().getStringExtra("playerName");
        txtPlayerName.setText(playerName);

        int difficulty = getIntent().getIntExtra("playerCash", 200);
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        txtMoney.setText(currency.format(difficulty));

        initialize();
    }

    public void initialize() {
        txtBust.setVisibility(View.INVISIBLE);
        btnBet.setVisibility(View.INVISIBLE);
        btnHit.setVisibility(View.VISIBLE);
        btnStay.setVisibility(View.VISIBLE);
        btnFold.setVisibility(View.VISIBLE);
        playerCount = 0;
        dealerCount = 0;
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
        boolean exitFlag = false;
        Random rand = new Random();
        int randomCard;

        while (exitFlag == false)
        {
            randomCard = rand.nextInt(52) + 1;
            if (actor == "player") {
                if (!playerCards.contains(randomCard)) {
                    playerCount += 1;
                    playerCards.add(randomCard);
                    playerTotal += Math.min(db.getCard(randomCard).getValue(), 10);
                    //txtPlayerCards.setText(playerCards.toString());
                    playerImages.get(playerCount).setImageResource(R.drawable.card41);
                    txtPlayerTotal.setText("Total: " + Integer.toString(playerTotal));
                    exitFlag = true;
                }
            } else if (actor == "dealer") {
                if (!dealerCards.contains(randomCard)) {
                    dealerCount += 1;
                    dealerCards.add(randomCard);
                    dealerTotal += Math.min(db.getCard(randomCard).getValue(), 10);
                    txtDealerCards.setText(dealerCards.toString());
                    //dealerImages.get(dealerCount).setImageResource(R.drawable.card41);
                    txtDealerTotal.setText("Total: " + Integer.toString(dealerTotal));
                    exitFlag = true;
                }
            }
        }
        checkWinLoss();
    }

    public void checkWinLoss() {
        if (playerTotal > 21) {
            txtBust.setText("BUST");
            txtBust.setVisibility(View.VISIBLE);
            btnBet.setVisibility(View.VISIBLE);
            btnHit.setVisibility(View.INVISIBLE);
            btnStay.setVisibility(View.INVISIBLE);
            btnFold.setVisibility(View.INVISIBLE);
        } else if (playerCount == 5) {
            // WIN
            txtBust.setText("WIN");
        }
    }
}