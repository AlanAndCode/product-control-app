package com.example.controledeprodutos.activity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.controledeprodutos.Auth.ActivityLogin;
import com.example.controledeprodutos.adapter.AdapterProduto;
import com.example.controledeprodutos.helper.FirebaseHelper;
import com.example.controledeprodutos.model.Produto;
import com.example.controledeprodutos.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterProduto.OnClick {

    private SwipeableRecyclerView RecyclerProdutos;
    private AdapterProduto adapterProduto;
private TextView text_info;
    private ImageButton image_add;
    private ImageButton image_info;

    private ProgressBar progressBar;
private List<Produto> produtoList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    image_add = findViewById(R.id.image_add);
        image_info= findViewById(R.id.image_info);
   text_info = findViewById(R.id.text_info);
   progressBar = findViewById(R.id.progressBar);

        RecyclerProdutos = findViewById(R.id.RecyclerProdutos);

        configRecyclerView();

        ouvinteCliques();
    }
    @Override
    protected void onStart() {
        super.onStart();
        recuperarProdutos();


    }
    private void recuperarProdutos(){
        DatabaseReference produtosRef = FirebaseHelper.getDatabaseReference()
        .child("produtos")
                .child(FirebaseHelper.getIdFirebase());
        produtosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                produtoList.clear();
                for (DataSnapshot snap : snapshot.getChildren()){
                    Produto produto = snap.getValue(Produto.class);


                    produtoList.add(produto);

                }

                verificaQtdLista();

                Collections.reverse(produtoList);
                adapterProduto.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void ouvinteCliques(){
        image_add.setOnClickListener(view -> {
            startActivity(new Intent(this, FormProdutoActivity.class));
        });

        image_info.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(this, image_info);
            popupMenu.getMenuInflater().inflate(R.menu.menu_toolbar, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.menu_sobre ){
                Toast.makeText(this, "Sobre", Toast.LENGTH_SHORT).show();
            } else if (menuItem.getItemId() == R.id.menu_sair) {
                FirebaseHelper.getAuth().signOut();
                startActivity(new Intent(this, ActivityLogin.class));
            }
                return true;
            });
popupMenu.show();
        });
    }

    private void configRecyclerView(){

        RecyclerProdutos.setLayoutManager(new LinearLayoutManager(this));
        RecyclerProdutos.setHasFixedSize(true);
        adapterProduto = new AdapterProduto(produtoList, this);
        RecyclerProdutos.setAdapter(adapterProduto);

        RecyclerProdutos.setListener(new SwipeLeftRightCallback.Listener() {
            @Override
            public void onSwipedLeft(int position) {
            }

            @Override
            public void onSwipedRight(int position) {

               Produto produto = produtoList.get(position);



                produtoList.remove(produto);
                produto.deleteProduct();
adapterProduto.notifyItemRemoved(position);

                verificaQtdLista();


            }
        });

    }

private void verificaQtdLista(){
    if(produtoList.size() == 0){
        text_info.setText("Nenhum produto cadastrado.");
        text_info.setVisibility(View.VISIBLE);

    }else{
        text_info.setVisibility(View.GONE);
    }
    progressBar.setVisibility(View.GONE);
}

    @Override
    public void onClickListener(Produto produto) {
        Intent intent = new Intent(this, FormProdutoActivity.class);
        intent.putExtra("produto", produto);
        startActivity(intent);
    }




}