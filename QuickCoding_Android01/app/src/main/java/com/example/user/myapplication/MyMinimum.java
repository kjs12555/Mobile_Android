package com.example.user.myapplication;

import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by user on 2016-10-25.
 */

public class MyMinimum extends MyValues {
    @Override
    public void getResult(EditText e, TextView t) {
        String TmpString = e.getText().toString();
        String[] TmpStringArray = TmpString.split(" ");
        int min=Integer.MAX_VALUE,tmpNum;
        for(int i=0; i<TmpStringArray.length; i++){
            try{
                tmpNum=Integer.parseInt(TmpStringArray[i]);
                if(tmpNum<min){
                    min=tmpNum;
                }
            }catch(Exception err){
                continue;
            }
        }
        t.setText(Integer.toString(min));
    }
}
