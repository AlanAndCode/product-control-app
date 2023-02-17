package com.example.controledeprodutos.Auth;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.example.controledeprodutos.R;

public class ActivityLogin extends AppCompatActivity {
    private TextView text_CC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


    iniciaComponentes();

    configCliques();
}

    private void configCliques(){
        text_CC.setOnClickListener(view -> startActivity(new Intent(this, CriarContaActivity.class)));
    }

    private void  iniciaComponentes(){
        text_CC = findViewById(R.id.text_CC);
    }

}
