package com.example.controledeprodutos.activity;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.controledeprodutos.Auth.ActivityLogin;
import com.example.controledeprodutos.activity.FormProdutoActivity;
import com.example.controledeprodutos.adapter.AdapterProduto;
import com.example.controledeprodutos.ProductDAO;
import com.example.controledeprodutos.helper.FirebaseHelper;
import com.example.controledeprodutos.model.Produto;
import com.example.controledeprodutos.R;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterProduto.OnClick {

    private SwipeableRecyclerView RecyclerProdutos;
    private AdapterProduto adapterProduto;
private TextView text_info;
    private ImageButton image_add;
    private ImageButton image_info;
private List<Produto> produtoList = new ArrayList<>();
    private ProductDAO productDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productDAO = new ProductDAO(this);

produtoList = productDAO.getListProdutos();

    image_add = findViewById(R.id.image_add);
        image_info= findViewById(R.id.image_info);
   text_info = findViewById(R.id.text_info);

        RecyclerProdutos = findViewById(R.id.RecyclerProdutos);

        configRecyclerView();

        ouvinteCliques();
    }

    @Override
    protected void onStart() {
        super.onStart();

        configRecyclerView();

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

        produtoList.clear();
        produtoList = productDAO.getListProdutos();

        verificaQtdLista();

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


                productDAO.deleteProduct(produto);
                produtoList.remove(produto);
adapterProduto.notifyItemRemoved(position);

                verificaQtdLista();


            }
        });

    }

private void verificaQtdLista(){
    if(produtoList.size() == 0){
        text_info.setVisibility(View.VISIBLE);
    }else{
        text_info.setVisibility(View.GONE);
    }
}

    @Override
    public void onClickListener(Produto produto) {
        Intent intent = new Intent(this, FormProdutoActivity.class);
        intent.putExtra("produto", produto);
        startActivity(intent);
    }




}