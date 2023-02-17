package com.example.controledeprodutos;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.controledeprodutos.model.Produto;

import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    private final SQLiteDatabase write;
    private final SQLiteDatabase read;

    public ProductDAO(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        this.write = dbHelper.getWritableDatabase();
        this.read = dbHelper.getReadableDatabase();
    }

    @SuppressLint("Range")
    public List<Produto> getListProdutos() {

        List<Produto> produtoList = new ArrayList<>();

        String sql = " SELECT * FROM " + DbHelper.TB_PRODUTO + ";";
        Cursor cursor = read.rawQuery(sql, null);

        while (cursor.moveToNext()) {

            cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int stock = cursor.getInt(cursor.getColumnIndex("stock"));
            double value = cursor.getDouble(cursor.getColumnIndex("value"));

            Produto produto = new Produto();

            produto.setNome(name);
            produto.setEstoque(stock);
            produto.setValor(value);

            produtoList.add(produto);

        }

        return produtoList;

    }

    public void saveProduct(Produto produto) {

        ContentValues cv = new ContentValues();
        cv.put("name", produto.getNome());
        cv.put("stock", produto.getEstoque());
        cv.put("value", produto.getValor());

        try {
            write.insert(DbHelper.TB_PRODUTO, null, cv);
            write.close();
        } catch (Exception e) {
            Log.i("ERROR", "erro ao salvar Produto: " + e.getMessage());
        }

    }

    public void atualizarProduto(Produto produto){

        ContentValues cv = new ContentValues();
cv.put("name", produto.getNome());
        cv.put("stock", produto.getEstoque());
        cv.put("value", produto.getValor());

        String where = "id=?";
        String[] args = {String.valueOf(produto.getId())};

                try {
write.update(DbHelper.TB_PRODUTO, cv, where, args);
//write.close();
                }catch (Exception e){
                    Log.i("Error", "Erro ao atualizar produto " + e.getMessage());


                }

    }

    public void deleteProduct(Produto produto){
        try{
            String[] args = {String.valueOf(produto.getId())};
            String where ="id=?";
            write.delete(DbHelper.TB_PRODUTO, where, args);
            write.close();
        }catch (Exception e){
            Log.i("ERROR", "Erro ao delete produto: " + e.getMessage());
        }
    }
}