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
    InputData<Integer, String> tmp = new InputData<Integer, String>();
    TextView input_int;
    TextView input_String;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input = (Button) findViewById(R.id.button);
        input_com = (Button) findViewById(R.id.button2);
        tmp_data= (EditText) findViewById(R.id.editText);
        input_int = (TextView) findViewById(R.id.textView3);
        input_String = (TextView) findViewById(R.id.textView5);
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
        List<T> arr_integer = new LinkedList<T>();
        List<V> arr_string = new LinkedList<V>();
        void input_T(T input){
            arr_integer.add(input);
        }
        void input_V(V input){
            arr_string.add(input);
        }
        void show(){
            for(V a : arr_string){
                input_String.setText(input_String.getText().toString()+" "+a);
            }
            for(T a : arr_integer){
                input_int.setText(input_int.getText().toString()+" "+a.toString());
            }
        }
    }
}
