package com.example.controledeprodutos.model;

import android.view.LayoutInflater;
import android.view.View;

import com.example.controledeprodutos.helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;

public class Produto implements Serializable {
private String id;


    private String nome;
    private int estoque;
    private double valor;
    private String urlImage;


    public Produto() {
DatabaseReference reference = FirebaseHelper.getDatabaseReference();
this.setId(reference.push().getKey());
    }

    public void SaveProduct(){
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("produtos")
                .child(FirebaseHelper.getIdFirebase())
                .child(this.id);
        reference.setValue(this);
    }

    public void deleteProduct(){
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("produtos")
                .child(FirebaseHelper.getIdFirebase())
                .child(this.id);
        reference.removeValue();

        StorageReference storageReference = FirebaseHelper.getStorageReference()
                .child("imagens")
                .child("produtos")
                .child(FirebaseHelper.getIdFirebase())
                .child(this.id + ".jpeg");
        storageReference.delete();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getNome() {

        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
