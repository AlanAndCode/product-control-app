package com.example.controledeprodutos.Auth;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.controledeprodutos.R;
import com.example.controledeprodutos.activity.MainActivity;
import com.example.controledeprodutos.helper.FirebaseHelper;

public class ActivityLogin extends AppCompatActivity {
    private TextView text_CC;

    private EditText edit_email;
    private EditText edit_senha;
    private ProgressBar progressBar;

    private TextView text_CP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


    iniciaComponentes();

    configCliques();
}

public void logar(View view){
        String email = edit_email.getText().toString().trim();
        String senha = edit_senha.getText().toString();

        if(!email.isEmpty()){
if(!senha.isEmpty()){

    progressBar.setVisibility(View.VISIBLE);

    validaLogin(email, senha);

}else {
edit_senha.requestFocus();
        edit_senha.setError("Write your password");
}
        }else {
            edit_email.requestFocus();
            edit_email.setError("Write your email");
        }

}

private void validaLogin(String email, String senha){
    FirebaseHelper.getAuth().signInWithEmailAndPassword(

email, senha

    ).addOnCompleteListener(task -> {
       if(task.isSuccessful()){
finish();
startActivity(new Intent(this, MainActivity.class));
       }else{
           String error = task.getException().getMessage();
           Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
           progressBar.setVisibility(View.GONE);
       }
    });
}
    private void configCliques(){
        text_CC.setOnClickListener(view -> startActivity(new Intent(this, CriarContaActivity.class)));
        text_CP.setOnClickListener(view -> startActivity(new Intent(this, RecuperarContaActivity.class)));
    }

    private void  iniciaComponentes(){
        text_CC = findViewById(R.id.text_CC);
        edit_email = findViewById(R.id.edit_email);
        edit_senha = findViewById(R.id.edit_senha);
        text_CP = findViewById(R.id.text_CP);
        progressBar = findViewById(R.id.progressBar);
    }

}
