package com.handong.moa.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.handong.moa.R;
import com.handong.moa.data.MyData;

public class BankActivity extends AppCompatActivity {
    //* for getting bank account info
    private EditText bankName;
    private EditText bankAccount;
    private EditText checkingBankAccount;
    private EditText bankPeopleName;

    private ImageButton backButton;
    private ImageButton finishButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);
        bankName = findViewById(R.id.bank_bankname_edittext);
        bankAccount = findViewById(R.id.bank_banknumber_edittext);
        checkingBankAccount = findViewById(R.id.bank_banknumberconfirm_edittext);
        bankPeopleName = findViewById(R.id.bank_bank_people_name_edittext);

        backButton = findViewById(R.id.bank_backbutton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        finishButton = findViewById(R.id.bank_finish_button);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
                loadPhoneData();
                finish();
            }
        });

    }

    // get the phone number info and save phone number info into users local
    void saveData() {
        String tempBankName = bankName.getText().toString();
        String tempBankAccount = bankAccount.getText().toString();
        String tempCheckBankAccount = checkingBankAccount.getText().toString();
        String tempBankPeopleName = bankPeopleName.getText().toString();


        if(tempBankAccount.equals(tempCheckBankAccount)) {
            MyData.accountNumber = tempBankAccount;
            MyData.accountName = tempBankPeopleName;
            MyData.bankName = tempBankName;
            String account = tempBankName + " " + tempBankAccount + " " + tempBankPeopleName;
            MyData.account = account;

            SharedPreferences preferences= getSharedPreferences("info",MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("accountNumber", MyData.accountNumber);
            editor.putString("accountName", MyData.accountName);
            editor.putString("bankName", MyData.bankName);
            editor.putString("account", MyData.account);
            editor.commit();
            finish();
        }else{
            Toast.makeText(this, "The account number is different", Toast.LENGTH_SHORT).show();
        }
    }

    //??? phone??? ???????????? ?????? ??????????????? ????????????
    void loadPhoneData(){
        SharedPreferences preferences = getSharedPreferences("info", MODE_PRIVATE);
        MyData.accountNumber = preferences.getString("accountNumber", null);
        MyData.accountName = preferences.getString("accountName", null);
        MyData.bankName = preferences.getString("bankName", null);
        MyData.account = preferences.getString("account", null);

    }
}