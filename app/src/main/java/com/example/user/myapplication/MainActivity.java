package com.example.user.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button min,aver;
    TextView result;
    EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = (TextView) findViewById(R.id.result);
        min = (Button) findViewById(R.id.min);
        aver = (Button) findViewById(R.id.average);
        input = (EditText) findViewById(R.id.input);

        min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyMinimum().getResult(input, result);
            }
        });

        aver.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new MyAverage().getResult(input, result);
            }
        });
    }
}
