package com.legaultOs.idigram;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.legault.idigram.R;

/**
 * Created by Legault on 27/04/2015.
 */
public class Welcome extends ActionBarActivity {

    private static final int SELECT_PHOTO = 100;

    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.welcome_layout);


    }


    public void selecImatge(View v) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_main2, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        AlertDialog.Builder al = new AlertDialog.Builder(this);
        switch (item.getItemId()) {

            case R.id.about:
                al
                        .setTitle("About")
                        .setMessage("Hecho por: Oscar Carod \nE-mail:oscaracso90@gmail.com \nIDI")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_email)
                        .show();
                return true;


        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();

                    Intent i = new Intent(this, MainActivity.class);
                    i.putExtra("pathImagen", selectedImage);
                    Toast.makeText(this, "Procesando vista previa...", Toast.LENGTH_LONG).show();

                    startActivity(i);
                    //finish();
                }
        }
    }
}