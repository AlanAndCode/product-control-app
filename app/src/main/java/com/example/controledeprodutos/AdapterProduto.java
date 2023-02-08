package com.example.controledeprodutos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterProduto extends RecyclerView.Adapter<AdapterProduto.MyViewHolder> {

private List<Produto> produtoList;
private OnClick onClick;
    public AdapterProduto(List<Produto> produtoList, OnClick onClick) {
        this.produtoList = produtoList;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produto, parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
Produto produto = produtoList.get(position);

holder.textProduct.setText(produto.getNome());
        holder.textStorage.setText("Stock: " + produto.getEstoque());
                holder.textPrice.setText("value: " + produto.getValor());

                holder.itemView.setOnClickListener(view -> onClick.onClickListener(produto));
    }

    @Override
    public int getItemCount() {
        return produtoList.size();
    }

    public interface OnClick{
        void onClickListener(Produto produto);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textProduct, textPrice, textStorage;
        ImageView imageInicial;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textProduct = itemView.findViewById(R.id.textProduct);
            textStorage = itemView.findViewById(R.id.textStorage);
            textPrice = itemView.findViewById(R.id.textPrice);
            imageInicial = itemView.findViewById(R.id.imageInicial);
        }
    }

}
