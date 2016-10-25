package kr.ac.kookmin.cs.quickcoding_android02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button start,big,small,bingo;
    TextView result,resultBingo;
    int s,l,cnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start = (Button) findViewById(R.id.start);
        big = (Button) findViewById(R.id.big);
        small = (Button) findViewById(R.id.small);
        bingo = (Button) findViewById(R.id.bingo);
        result = (TextView) findViewById(R.id.result);
        resultBingo = (TextView) findViewById(R.id.result_bingo);

        start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                s=1;
                l=500;
                cnt=1;
                result.setText(Integer.toString((s+l)/2));
            }
        });

        big.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(s>l){
                    resultBingo.setText("Error!");
                    result.setText("Error!");
                }
                s=(s+l)/2+1;
                cnt++;
                result.setText(Integer.toString((s+l)/2));
            }
        });

        small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(s>l){
                    resultBingo.setText("Error!");
                    result.setText("Error!");
                }
                l=(s+l)/2-1;
                cnt++;
                result.setText(Integer.toString((s+l)/2));
            }
        });

        bingo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultBingo.setText(Integer.toString(cnt));
            }
        });
    }
}
