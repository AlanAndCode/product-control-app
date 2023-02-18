package com.example.controledeprodutos.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.controledeprodutos.ProductDAO;
import com.example.controledeprodutos.R;
import com.example.controledeprodutos.model.Produto;

public class FormProdutoActivity extends AppCompatActivity {
private EditText edit_stock;
    private EditText edit_value;
    private EditText edit_prod;


    private ProductDAO productDAO;

    private Produto produto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_produto);

        productDAO = new ProductDAO(this);

        edit_stock = findViewById(R.id.edit_stock);
        edit_prod = findViewById(R.id.edit_prod);
        edit_value = findViewById(R.id.edit_value);


        Bundle bundle = getIntent().getExtras();
if(bundle != null){
    produto = (Produto) bundle.getSerializable("produto");
}



editProduto();
    }
private void editProduto(){
        edit_prod.setText(produto.getNome());
        edit_stock.setText(String.valueOf(produto.getEstoque()));
        edit_value.setText(String.valueOf(produto.getValor()));
}
    public void salvarProduto(View view) {

        String nome = edit_prod.getText().toString();
        String quantidade = edit_stock.getText().toString();
        String valor = edit_value.getText().toString();

        if (!nome.isEmpty()) {
            if (!quantidade.isEmpty()) {
                int qtd = Integer.parseInt(quantidade);
                if (qtd >= 1) {

                    if (!valor.isEmpty()) {
                        double valorProduct = Double.parseDouble(valor);

                        if (valorProduct > 0) {

                            if(produto == null) produto = new Produto();
                            produto.setNome(nome);
                            produto.setEstoque(qtd);
                            produto.setValor(valorProduct);

                            if(produto.getId() != 0){
                                productDAO.atualizarProduto(produto);
                            }else{
                                productDAO.saveProduct(produto);
                            }

                            finish();

                        } else {
                            edit_value.requestFocus();
                            edit_value.setError("informe valor maior que 0");
                        }
                    } else {
                        edit_value.requestFocus();
                        edit_value.setError("informe valor");
                    }

                } else {
                    edit_stock.requestFocus();
                    edit_stock.setError("valor deve ser maior que 0");
                }

            } else{
                edit_stock.requestFocus();
                edit_stock.setError("preencha quantidade");
            }
        } else{
            edit_prod.requestFocus();
            edit_prod.setError("Informe o nome do produto");
        }
    }


}