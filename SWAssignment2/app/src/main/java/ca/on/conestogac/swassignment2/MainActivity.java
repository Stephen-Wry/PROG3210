package ca.on.conestogac.swassignment2;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private View screen;
    private TextView txtPlayerCards, txtDealerCards, txtMoney, txtDealerTotal, txtPlayerTotal;
    private List<ImageView> playerImages = new ArrayList<>();
    private List<ImageView> dealerImages = new ArrayList<>();
    private TextView txtBust, txtPlayerName;
    private int playerTotal, dealerTotal;
    private int playerCount, dealerCount;
    private List<Integer> playerCards = new ArrayList<Integer>();
    private List<Integer> dealerCards = new ArrayList<Integer>();
    private Button btnBet, btnHit, btnStay, btnFold;
    private Player player;
    NumberFormat currency = NumberFormat.getCurrencyInstance();

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

        playerImages.add((ImageView) findViewById(R.id.imgPlayer1));
        playerImages.add((ImageView) findViewById(R.id.imgPlayer2));
        playerImages.add((ImageView) findViewById(R.id.imgPlayer3));
        playerImages.add((ImageView) findViewById(R.id.imgPlayer4));
        playerImages.add((ImageView) findViewById(R.id.imgPlayer5));

        dealerImages.add((ImageView) findViewById(R.id.imgDealer1));
        dealerImages.add((ImageView) findViewById(R.id.imgDealer2));
        dealerImages.add((ImageView) findViewById(R.id.imgDealer3));
        dealerImages.add((ImageView) findViewById(R.id.imgDealer4));
        dealerImages.add((ImageView) findViewById(R.id.imgDealer5));

        // Set listeners
        btnBet.setOnClickListener(this);
        btnHit.setOnClickListener(this);
        btnStay.setOnClickListener(this);
        btnFold.setOnClickListener(this);
        screen.setOnClickListener(this);

        // Get and display player name and money pool
        player = db.getPlayer(getIntent().getStringExtra("playerName"));
        txtPlayerName.setText(player.getName());
        txtMoney.setText(currency.format(player.getCash()));

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
        for (ImageView image: playerImages) {
            image.setImageResource(R.drawable.blank);
        }
        for (ImageView image: dealerImages) {
            image.setImageResource(R.drawable.blank);
        }
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
                while (dealerTotal <= playerTotal && dealerTotal < 21 && dealerCount < 5) {
                    addCard("dealer");
                }
                checkWinLoss(true);
                break;
            case R.id.buttonFold:
                txtBust.setText("FOLD");
                txtBust.setVisibility(View.VISIBLE);
                btnBet.setVisibility(View.VISIBLE);
                btnHit.setVisibility(View.INVISIBLE);
                btnStay.setVisibility(View.INVISIBLE);
                btnFold.setVisibility(View.INVISIBLE);
                break;
            case R.id.buttonBet:
                initialize();
                player.setCash(player.getCash() - 10);
                txtMoney.setText(currency.format(player.getCash()));
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
            if (!playerCards.contains(randomCard) && !dealerCards.contains(randomCard)) {
                exitFlag = true;
                int id = getResources().getIdentifier("card" + randomCard,
                        "drawable", getPackageName());
                if (actor == "player") {
                    playerCount += 1;
                    playerCards.add(randomCard);
                    playerTotal += Math.min(db.getCard(randomCard).getValue(), 10);
                    playerImages.get(playerCount-1).setImageResource(id);
                    txtPlayerTotal.setText("Total: " + Integer.toString(playerTotal));
                } else if (actor == "dealer") {
                    dealerCount += 1;
                    dealerCards.add(randomCard);
                    dealerTotal += Math.min(db.getCard(randomCard).getValue(), 10);
                    dealerImages.get(dealerCount - 1).setImageResource(id);
                    txtDealerTotal.setText("Total: " + Integer.toString(dealerTotal));
                }
            }
        }
        checkWinLoss(false);
    }

    public void checkWinLoss(boolean gameOver) {
        boolean endGame = false;
        if ((playerCount == 5 && playerTotal <= 21) || playerTotal == 21 || dealerTotal > 21) {
            txtBust.setText("WIN");
            player.setCash(player.getCash() + 20);
            txtMoney.setText(currency.format(player.getCash()));
            endGame = true;
        } else if (playerTotal > 21) {
            txtBust.setText("BUST");
            player.setCash(player.getCash() - 20);
            txtMoney.setText(currency.format(player.getCash()));
            endGame = true;
        } else if (gameOver && playerTotal < dealerTotal) {
            txtBust.setText("LOSE");
            player.setCash(player.getCash() - 20);
            txtMoney.setText(currency.format(player.getCash()));
            endGame = true;
        } else if (gameOver && playerTotal >= dealerTotal) {
            txtBust.setText("WIN");
            player.setCash(player.getCash() + 20);
            txtMoney.setText(currency.format(player.getCash()));
            endGame = true;
        }
        if (endGame == true) {
            txtBust.setVisibility(View.VISIBLE);
            btnBet.setVisibility(View.VISIBLE);
            btnHit.setVisibility(View.INVISIBLE);
            btnStay.setVisibility(View.INVISIBLE);
            btnFold.setVisibility(View.INVISIBLE);
        }
    }
}