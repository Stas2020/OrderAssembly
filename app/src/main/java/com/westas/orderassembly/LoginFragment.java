package com.westas.orderassembly;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class LoginFragment extends Fragment {

    interface TOnClickOk
    {
        void OnClickOk(String password);
    }

    private TextView textView1;
    private String password = "";
    private TOnClickOk onClickOk_;

    public void SetOnClickListern (TOnClickOk onClickOk)
    {
        onClickOk_ = onClickOk;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.keyboard_for_login, container, false);


    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        textView1 = getView().findViewById(R.id.editText2);

       Button button1 = getView().findViewById(R.id.button1);
       button1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               password += "1";
               textView1.setText(password);
           }
       });

        Button button2 = getView().findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                password += "2";
                textView1.setText(textView1.getText().toString() + "2");
            }
        });
        Button button3 = getView().findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                password += "3";
                textView1.setText(textView1.getText().toString() + "3");
            }
        });
        Button button4 = getView().findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                password += "4";
                textView1.setText(textView1.getText().toString() + "4");
            }
        });
        Button button5 = getView().findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                password += "5";
                textView1.setText(textView1.getText().toString() + "5");
            }
        });
        Button button6 = getView().findViewById(R.id.button6);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                password += "6";
                textView1.setText(textView1.getText().toString() + "6");
            }
        });
        Button button7 = getView().findViewById(R.id.button7);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                password += "7";
                textView1.setText(textView1.getText().toString() + "7");
            }
        });
        Button button8 = getView().findViewById(R.id.button8);
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                password += "8";
                textView1.setText(textView1.getText().toString() + "8");
            }
        });
        Button button9 = getView().findViewById(R.id.button9);
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                password += "9";
                textView1.setText(textView1.getText().toString() + "9");
            }
        });
        Button button0 = getView().findViewById(R.id.button0);
        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                password += "0";
                textView1.setText(textView1.getText().toString() + "0");
            }
        });
        Button button_cancel = getView().findViewById(R.id.button_cancel);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                password = "";
                textView1.setText(password);
            }
        });
        Button button_ok = getView().findViewById(R.id.button_ok);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(onClickOk_ != null)
                {
                    onClickOk_.OnClickOk(password);
                }
                password = "";
                textView1.setText(password);
            }
        });
    }
}
