package com.legaultOs.idigram;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.legault.idigram.R;

/**
 * Created by Legault on 27/04/2015.
 */
public class Welcome extends Activity {

    private static final int SELECT_PHOTO = 100;

    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.welcome_layout);



    }


    public void selecImatge (View v)
    {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();

                    Intent i = new Intent(this, MainActivity.class);
                    i.putExtra("pathImagen", selectedImage);
                    Toast.makeText(this, "Procesando vista previa...", Toast.LENGTH_LONG).show();

                    startActivity(i);
                }
        }
    }
}