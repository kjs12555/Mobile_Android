package kr.ac.kookmin.cs.quickcoding_android;

import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button input;
    Button input_com;
    EditText tmp_data;
    InputData<Integer, String> tmp = new InputData<>();
    TextView Text_T;
    TextView Text_V;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input = (Button) findViewById(R.id.button);
        input_com = (Button) findViewById(R.id.button2);
        tmp_data= (EditText) findViewById(R.id.editText);
        Text_T = (TextView) findViewById(R.id.textView3);
        Text_V = (TextView) findViewById(R.id.textView5);
        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = tmp_data.getText().toString();
                try{
                    int data = Integer.parseInt(input);
                    tmp.input_T(data);
                }catch(Exception e){
                    String data = input;
                    tmp.input_V(data);
                }
            }
        });
        input_com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tmp.show();
            }
        });
    }
    class InputData<T,V>{
        List<T> arr_T = new LinkedList<T>();
        List<V> arr_V = new LinkedList<V>();
        void input_T(T input){
            arr_T.add(input);
            tmp_data.setText("");
        }
        void input_V(V input){
            arr_V.add(input);
            tmp_data.setText("");
        }
        void show(){
            for(V a : arr_V){
                Text_V.setText(Text_V.getText().toString()+" "+a.toString());
            }
            for(T a : arr_T){
                Text_T.setText(Text_T.getText().toString()+" "+a.toString());
            }
        }
    }
}
