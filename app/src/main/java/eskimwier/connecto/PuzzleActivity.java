package eskimwier.connecto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;

public class PuzzleActivity extends AppCompatActivity implements View.OnClickListener {

    TableLayout gameTable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        gameTable = (TableLayout) findViewById(R.id.game_table);

        for (int i = 0; i < 5; i++) {

            TableRow row = new TableRow(getApplicationContext());
            for (int j = 0; j < 5; j++) {
                Junction junc = new Junction(Junction.getRandomJuncType());
                SquareView square = new SquareView(getApplicationContext(), junc);
                square.setOnClickListener(this);
                row.addView(square, 200, 200);
            }
            gameTable.addView(row);
        }
    }

    @Override
    public void onClick(View v) {

        try {

            ((SquareView) v).rotateImageClockwise();

        } catch (ClassCastException cce) {



        }

    }
}
