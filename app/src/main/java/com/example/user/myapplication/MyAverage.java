package com.example.user.myapplication;

import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by user on 2016-10-25.
 */

public class MyAverage extends MyValues {
    @Override
    public void getResult(EditText e, TextView t) {
        String TmpString = e.getText().toString();
        String[] TmpStringArray = TmpString.split(" ");
        double sum=0;
        for(int i=0; i<TmpStringArray.length; i++){
            try{
                sum+=Integer.parseInt(TmpStringArray[i]);
            }catch(Exception err){
                continue;
            }
        }
        t.setText(Double.toString(sum/TmpStringArray.length));
    }
}
