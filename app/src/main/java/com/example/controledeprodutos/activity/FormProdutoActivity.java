package com.example.controledeprodutos.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.controledeprodutos.R;
import com.example.controledeprodutos.helper.FirebaseHelper;
import com.example.controledeprodutos.model.Produto;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.squareup.picasso.Picasso;


import java.io.IOException;
import java.util.List;

public class FormProdutoActivity extends AppCompatActivity {
    private static final int REQUEST_GALLERIA = 100;
private EditText edit_stock;
    private EditText edit_value;
    private EditText edit_prod;

private ImageView image_product;
private String caminhoImagem;
private Bitmap imagem;


    private Produto produto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_produto);


        edit_stock = findViewById(R.id.edit_stock);
        edit_prod = findViewById(R.id.edit_prod);
        edit_value = findViewById(R.id.edit_value);
        image_product = findViewById(R.id.image_product);



        Bundle bundle = getIntent().getExtras();
if(bundle != null){
    produto = (Produto) bundle.getSerializable("produto");

    editProduto();
}




    }


    public void verifYPermissionGallery(View view) {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                abrirgaleria();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(FormProdutoActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        };

        showDialogPermissao(permissionListener, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});

    }

    private void abrirgaleria(){
Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
startActivityForResult(intent, REQUEST_GALLERIA);
    }


    private void showDialogPermissao(PermissionListener permissionListener, String[] permissions){
        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedTitle("Permissão negada")
                .setDeniedMessage("Try again and get Permission for System")
                .setDeniedCloseButtonText("nao")
                .setGotoSettingButtonText("sim")
                .setPermissions(permissions)
                .check();
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

                            if(caminhoImagem == null){
                                Toast.makeText(this, "Select any Image", Toast.LENGTH_SHORT).show();

                            }else{
                                SaveImage();
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

    private void SaveImage(){
        StorageReference reference = FirebaseHelper.getStorageReference()
                .child("imagens")
                .child("produtos")
                .child(FirebaseHelper.getIdFirebase())
                .child(produto.getId() + ".jpeg");

        UploadTask uploadTask = reference.putFile(Uri.parse(caminhoImagem));
        uploadTask.addOnSuccessListener(taskSnapshot -> reference.getDownloadUrl().addOnCompleteListener(task -> {

            produto.setUrlImage(task.getResult().toString());
            produto.SaveProduct();

            finish();

    })).addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());

    }

    private void editProduto(){
        edit_prod.setText(produto.getNome());
        edit_stock.setText(String.valueOf(produto.getEstoque()));
        edit_value.setText(String.valueOf(produto.getValor()));
        Picasso.get().load(produto.getUrlImage()).into(image_product);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_GALLERIA){
Uri localImageSelected = data.getData();
caminhoImagem = localImageSelected.toString();

if(Build.VERSION.SDK_INT < 28){
    try {
        imagem = MediaStore.Images.Media.getBitmap(getBaseContext().getContentResolver(), localImageSelected);
    } catch (IOException e) {
        throw new RuntimeException(e);
    }

}else{
    ImageDecoder.Source source = ImageDecoder.createSource(getBaseContext().getContentResolver(), localImageSelected);
    try {
        imagem = ImageDecoder.decodeBitmap(source);
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}

image_product.setImageBitmap(imagem);

            }
        }

    }





}