package com.legaultOs.idigram;

/**
 * Created by Legault on 20/04/2015.
 */

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.legault.idigram.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ActionBarActivity {

    private static final int SELECT_PHOTO = 100;
    private static final int SELECT_SHARE = 200;
    int posFilt;
    private ImageView imgMain;
    private Bitmap src, showing, contrasteBrillo, loading, ultimo, thumbnail, ultimoThu;
    private HashMap<String, Bitmap> imgFiltered;
    private HashMap<String, Params> Parametros;
    private ArrayList<String> vista;
    private int brillo = 1, brillant = 1, contraste = 1, contant = 1, saturacion = 10, satant = 10;
    private int imgSize = 400;
    private Bitmap.CompressFormat defaultFormat = Bitmap.CompressFormat.PNG;
    private Uri pathImagen;
    private Bitmap bim[];
    private String lastFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundle = getIntent().getExtras();
        loading = BitmapFactory.decodeResource(getResources(), R.drawable.loading);
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/IDIGram/");
        if (!f.exists()) f.mkdir();
        imgMain = (ImageView) findViewById(R.id.effect_main);
        pathImagen = (Uri) bundle.get("pathImagen");
        src = decodeUri(pathImagen, false);

        thumbnail = escalar(src, true);
        lastFilter = "normal";
        ultimo = src;
        imgMain.setImageBitmap(src);
        imgFiltered = new HashMap<String, Bitmap>();
        Parametros = new HashMap<String, Params>();
        showing = src;
        vista = new ArrayList<String>();

        contrasteBrillo = null;


        imgMain.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    imgMain.setImageBitmap(src);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (contrasteBrillo != null) imgMain.setImageBitmap(contrasteBrillo);
                    else imgMain.setImageBitmap(showing);
                }


                // TODO Auto-generated method stub
                return false;
            }
        });
        tabSelected(R.id.tab1);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        AlertDialog.Builder al = new AlertDialog.Builder(this);
        switch (item.getItemId()) {
            case R.id.help:
                new AlertDialog.Builder(this)
                        .setTitle("Help")
                        .setMessage("-Filtros: Podemos seleccionar un filtro para aplicarlo a la imagen actual. " +
                                "\n\n-Ajustes: Podemos cambiar los niveles de Saturacion,Contraste y brillo de la imagen. " +
                                "\n\n-Guardar: Guardaremos la foto en formato que hayamos indicado en Settings (por defecto .PNG) con el nombre dado una vez no haya cambios pendientes en la foto." +
                                "\n\n-Cambiar foto: Cambiaremos la foto actual por otra de nuestra galeria." +
                                "\n\n-Aplicar cambios: Aplicaremos los filtros y los ajustes a la foto inicial y podremos aplicarle más encima." +
                                "\n\n-Deshacer Cambios: Si tenemos un filtro escogido pero no aplicado, nos deshará ese filtro, si no tenemos ningún filtro ni ningún ajuste aplicado, volveremos a la versión anterior, antes de aplicar cambios." +
                                "\n\n-Tocar la foto: Nos servirá para ver la foto antes y después del filtro aplicado.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();
                return true;
            case R.id.about:
                al
                        .setTitle("About")
                        .setMessage("Hecho por: Oscar Carod \nE-mail: oscaracso90@gmail.com \nAsignatura: IDI")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_email)
                        .show();
                return true;
            case R.id.action_settings:
                LayoutInflater layoutInflater = LayoutInflater.from(this);
                final View menuSettings = layoutInflater.inflate(R.layout.settings, null);
                TextView tv = (TextView) menuSettings.findViewById(R.id.editText2);
                tv.setText(Integer.toString(imgSize));
                RadioGroup rg = (RadioGroup) menuSettings.findViewById(R.id.radiogroup1);
                if (defaultFormat.equals(Bitmap.CompressFormat.PNG)) rg.check(R.id.png);
                else rg.check(R.id.jpeg);
                al
                        .setTitle("Settings")
                        .setView(menuSettings);
                al.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        TextView tv = (TextView) menuSettings.findViewById(R.id.editText2);
                        if (imgSize != Integer.parseInt(tv.getText().toString())) {
                            imgSize = Integer.parseInt(tv.getText().toString());
                            src = decodeUri(pathImagen, false);
                            thumbnail = escalar(src, true);
                            imgFiltered.clear();
                            tabSelected(R.id.tab1);

                        }
                        RadioGroup rg = (RadioGroup) menuSettings.findViewById(R.id.radiogroup1);
                        if (rg.getCheckedRadioButtonId() == R.id.png)
                            defaultFormat = Bitmap.CompressFormat.PNG;
                        else defaultFormat = Bitmap.CompressFormat.JPEG;
                    }
                })
                        .setIcon(android.R.drawable.ic_menu_manage)
                        .show();

                return true;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder al = new AlertDialog.Builder(this);
        al
                .setTitle("Salir")
                .setMessage("Seguro que quieres salir al menú principal? \n(Los cambios no se guardarán)")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();

                    }
                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    private void insertarEnFiltros(String nombreFiltro, Bitmap preview, int Id) {
        LinearLayout ll = (LinearLayout) findViewById(R.id.espaciofiltros);
        if (ll != null) {
            View vv = View.inflate(this, R.layout.filt, null);
            ImageView iv = (ImageView) vv.findViewById(R.id.imagenFilt);
            TextView tv = (TextView) vv.findViewById(R.id.nombreFilt);
            iv.setImageBitmap(preview);
            iv.setId(Id);
            tv.setText(nombreFiltro);
            if (!vista.contains(nombreFiltro)) {
                ll.addView(vv, posFilt, new LinearLayout.LayoutParams(ll.getLayoutParams().width, ll.getLayoutParams().height));
                vista.add(nombreFiltro);
            }
            posFilt++;
        }
    }

    private void borrarLoading(View v) {

        LinearLayout ll = (LinearLayout) findViewById(R.id.espaciofiltros);
        if (ll != null && v != null) {
            ll.removeView(v);
        }

    }

    private View meterLoading() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.espaciofiltros);
        View vv = View.inflate(this, R.layout.filt, null);
        if (ll != null) {

            ImageView iv = (ImageView) vv.findViewById(R.id.imagenFilt);
            TextView tv = (TextView) vv.findViewById(R.id.nombreFilt);
            iv.setImageBitmap(loading);
            tv.setText("Cargando...");
            ll.addView(vv, new LinearLayout.LayoutParams(
                    ll.getLayoutParams().width, ll.getLayoutParams().height));
        }
        return vv;


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void llenarMuestras() {
        ImageFilters imgFilter = new ImageFilters();

        dividirImagen(escalar(src, true));


        LinearLayout ll = (LinearLayout) findViewById(R.id.espaciofiltros);
        ll.removeAllViews();
        vista.clear();


        if (imgFiltered.containsKey("Normal"))
            insertarEnFiltros("Normal", imgFiltered.get("Normal"), 0);
        else {
            Parametros.put("normal", new Params(false, "normal", "Normal", thumbnail, 1.5, 0.6, 0.12, 100, 0, 0, contraste, brillo, saturacion));
            new ParaTask("Normal").execute(Parametros.get("normal"));
        }

        if (imgFiltered.containsKey("Suavizado"))
            insertarEnFiltros("Suavizado", imgFiltered.get("Suavizado"), 7);
        else {
            Parametros.put("smooth", new Params(false, "smooth", "Suavizado", thumbnail, 0, 0, 0, 5, 0, 7, contraste, brillo, saturacion));
            new ParaTask("Suavizado")
                    .execute(Parametros.get("smooth"));
        }

        if (imgFiltered.containsKey("Sharpen"))
            insertarEnFiltros("Sharpen", imgFiltered.get("Sharpen"), 5);
        else {
            Parametros.put("sharp", new Params(false, "sharp", "Sharpen", thumbnail, 0, 0, 0, 9, 0, 5, contraste, brillo, saturacion));
            new ParaTask("Sharpen")
                    .execute(Parametros.get("sharp"));
        }

        if (imgFiltered.containsKey("Gaussian"))
            insertarEnFiltros("Gaussian", imgFiltered.get("Gaussian"), 2);
        else {
            Parametros.put("gauss", new Params(false, "gauss", "Gaussian", thumbnail, 0, 0, 0, 16, 0, 2, contraste, brillo, saturacion));
            new ParaTask("Gaussian")
                    .execute(Parametros.get("gauss"));
        }


        if (imgFiltered.containsKey("Halo")) insertarEnFiltros("Halo", imgFiltered.get("Halo"), 9);
        else {
            Parametros.put("halo", new Params(false, "halo", "Halo", thumbnail, 0, 0, 0, 9, 0, 9, contraste, brillo, saturacion));
            new ParaTask("Halo")
                    .execute(Parametros.get("halo"));
        }

        if (imgFiltered.containsKey("Sepia"))
            insertarEnFiltros("Sepia", imgFiltered.get("Sepia"), 1);
        else {
            Parametros.put("sepia", new Params(false, "sepia", "Sepia", thumbnail, 1.5, 0.6, 0.12, 100, 0, 1, contraste, brillo, saturacion));
            new ParaTask("Sepia")
                    .execute(Parametros.get("sepia"));
        }


        if (imgFiltered.containsKey("Gris")) insertarEnFiltros("Gris", imgFiltered.get("Gris"), 4);
        else {
            Parametros.put("grey", new Params(false, "grey", "Gris", thumbnail, 0, 0, 0, 0, 0, 4, contraste, brillo, saturacion));
            new ParaTask("Gris")
                    .execute(Parametros.get("grey"));
        }

        if (imgFiltered.containsKey("Efecto Rojo"))
            insertarEnFiltros("Efecto Rojo", imgFiltered.get("Efecto Rojo"), 10);
        else {
            Parametros.put("efectoRojo", new Params(false, "filtroColor", "Efecto Rojo", thumbnail, 1, 0, 0, 9, 0, 10, contraste, brillo, saturacion));
            new ParaTask("Efecto Rojo")
                    .execute(Parametros.get("efectoRojo"));
        }

        if (imgFiltered.containsKey("Efecto Azul"))
            insertarEnFiltros("Efecto Azul", imgFiltered.get("Efecto Azul"), 11);
        else {
            Parametros.put("efectoAzul", new Params(false, "filtroColor", "Efecto Azul", thumbnail, 0, 0, 1, 9, 0, 11, contraste, brillo, saturacion));
            new ParaTask("Efecto Azul")
                    .execute(Parametros.get("efectoAzul"));
        }

        if (imgFiltered.containsKey("Efecto Verde"))
            insertarEnFiltros("Efecto Verde", imgFiltered.get("Efecto Verde"), 12);
        else {
            Parametros.put("efectoVerde", new Params(false, "filtroColor", "Efecto Verde", thumbnail, 0, 1, 0, 9, 0, 12, contraste, brillo, saturacion));
            new ParaTask("Efecto Verde")
                    .execute(Parametros.get("efectoVerde"));
        }


        if (imgFiltered.containsKey("Edge Detect"))
            insertarEnFiltros("Edge Detect", imgFiltered.get("Edge Detect"), 6);
        else {
            Parametros.put("edetect", new Params(false, "edetect", "Edge Detect", thumbnail, 0, 0, 0, 100, 0, 6, contraste, brillo, saturacion));
            new ParaTask("Edge Detect")
                    .execute(Parametros.get("edetect"));
        }

        if (imgFiltered.containsKey("Inversa"))
            insertarEnFiltros("Inversa", imgFiltered.get("Inversa"), 3);
        else {
            Parametros.put("inv", new Params(false, "inv", "Inversa", thumbnail, 0, 0, 0, 0, 0, 3, contraste, brillo, saturacion));
            new ParaTask("Inversa")
                    .execute(Parametros.get("inv"));
        }


        if (imgFiltered.containsKey("Relieve"))
            insertarEnFiltros("Relieve", imgFiltered.get("Relieve"), 8);
        else {
            Parametros.put("emboss", new Params(false, "emboss", "Relieve", thumbnail, 0, 0, 0, 100, 0, 8, contraste, brillo, saturacion));
            new ParaTask("Relieve")
                    .execute(Parametros.get("emboss"));
        }

        if (imgFiltered.containsKey("Engrave"))
            insertarEnFiltros("Engrave", imgFiltered.get("Engrave"), 13);
        else {
            Parametros.put("engrave", new Params(false, "engrave", "Engrave", thumbnail, 0, 0, 0, 0, 0, 13, contraste, brillo, saturacion));
            new ParaTask("Engrave")
                    .execute(Parametros.get("engrave"));
        }


        //------------------------------------------


    }

    public void tabSelected(int tab) {
        LinearLayout ll = (LinearLayout) findViewById(R.id.espacio);
        TextView t1 = (TextView) findViewById(R.id.tab1);
        TextView t2 = (TextView) findViewById(R.id.tab2);
        TextView t3 = (TextView) findViewById(R.id.tab3);
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Service.INPUT_METHOD_SERVICE);
        View vv;
        switch (tab) {
            case R.id.tab1:
                // Toast.makeText(this, "Procesando vista previa...", Toast.LENGTH_SHORT).show();
                t1.setBackgroundColor(Color.parseColor("#354D5E"));
                t2.setBackgroundColor(Color.parseColor("#80A7C2"));
                t3.setBackgroundColor(Color.parseColor("#80A7C2"));
                if (this.getCurrentFocus() != null)
                    imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);

                ll.removeAllViews();

                vv = View.inflate(this, R.layout.filtros, null);
                ll.addView(vv, new LinearLayout.LayoutParams(ll.getLayoutParams().width, ll.getLayoutParams().height));
                posFilt = 0;
                if (contraste != contant || brillo != brillant || saturacion != satant) {
                    imgFiltered.clear();
                    contant = contraste;
                    brillant = brillo;
                    satant = saturacion;


                }


                llenarMuestras();


                break;
            case R.id.tab2:
                t1.setBackgroundColor(Color.parseColor("#80A7C2"));
                t2.setBackgroundColor(Color.parseColor("#354D5E"));
                t3.setBackgroundColor(Color.parseColor("#80A7C2"));
                if (this.getCurrentFocus() != null)
                    imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
                ll.removeAllViews();
                vv = View.inflate(this, R.layout.ajustes, null);
                ll.addView(vv, new LinearLayout.LayoutParams(ll.getLayoutParams().width, ll.getLayoutParams().height));

                SeekBar sb1 = (SeekBar) findViewById(R.id.seekBar1);//Contraste
                sb1.setProgress(contraste + 100);
                sb1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    int progressChanged = 0;

                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progressChanged = progress;
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // TODO Auto-generated method stub
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                        contant = contraste;
                        contraste = progressChanged - 100;
                        /*Bitmap bm= camibiarContrBrilloSat(showing,false,contraste,brillo,saturacion);
                        imgMain.setImageBitmap(bm);*/
                        dividirImagen(src);
                        Params temp = Parametros.get(lastFilter);
                        temp.setBm(src);
                        temp.setContraste(contraste);
                        temp.setProcesa(true);
                        new ParaTask("").execute(temp);
                    }
                });

                SeekBar sb2 = (SeekBar) findViewById(R.id.seekBar2);//Brillo
                sb2.setProgress(brillo + 100);
                sb2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    int progressChanged = 0;

                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progressChanged = progress;
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // TODO Auto-generated method stub
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                        brillant = brillo;
                        brillo = progressChanged - 100;
                        /*Bitmap bm= camibiarContrBrilloSat(showing,false,contraste,brillo,saturacion);
                        imgMain.setImageBitmap(bm);*/
                        dividirImagen(src);
                        Params temp = Parametros.get(lastFilter);
                        temp.setBm(src);
                        temp.setProcesa(true);
                        temp.setBrillo(brillo);
                        new ParaTask("").execute(temp);

                    }
                });
                SeekBar sb3 = (SeekBar) findViewById(R.id.seekBar3);//Brillo
                sb3.setProgress(saturacion + 50);
                sb3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    int progressChanged = 50;

                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progressChanged = progress;
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // TODO Auto-generated method stub
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                        satant = saturacion;
                        saturacion = progressChanged - 40;
                       /* Bitmap bm= camibiarContrBrilloSat(showing,false,contraste,brillo,saturacion);
                        imgMain.setImageBitmap(bm);*/
                        dividirImagen(src);
                        Params temp = Parametros.get(lastFilter);
                        temp.setBm(src);
                        temp.setProcesa(true);
                        temp.setSaturacion(saturacion);
                        new ParaTask("").execute(temp);

                    }
                });

                break;
            case R.id.tab3:
                t1.setBackgroundColor(Color.parseColor("#80A7C2"));
                t2.setBackgroundColor(Color.parseColor("#80A7C2"));
                t3.setBackgroundColor(Color.parseColor("#354D5E"));
                ll.removeAllViews();
                vv = View.inflate(this, R.layout.guardar, null);
                ll.addView(vv, new LinearLayout.LayoutParams(ll.getLayoutParams().width, ll.getLayoutParams().height));

                EditText tv = (EditText) findViewById(R.id.editText);
                tv.requestFocusFromTouch();

                imm.showSoftInput(tv, 0);

                break;
        }
    }

    private Bitmap camibiarContrBrilloSat(Bitmap vit, boolean loading, int cont, int brill, int sat) {

        ImageFilters imgFilter = new ImageFilters();
        Bitmap bm = imgFilter.applySaturationFilter(vit, sat);
        bm = imgFilter.applyContrastEffect(bm, cont);
        bm = imgFilter.applyBrightnessEffect(bm, brill);
        if (!loading) contrasteBrillo = bm;
        return bm;

    }

    public void tabClicked(View v) {


        tabSelected(v.getId());


    }

    public void buttonClicked(View v) {

        //Toast.makeText(this, "Procesando...", Toast.LENGTH_SHORT).show();
        ImageFilters imgFilter = new ImageFilters();
        int id = v.getId();


        System.out.println(id);
        if (id != R.id.btn_pick_img && id != R.id.btn_apply_img && id != R.id.btn_revert_img)// querra decir que hemos clickado a un filtro
        {
            contrasteBrillo = null;
            //Toast.makeText(this, "Procesando filtro...", Toast.LENGTH_SHORT).show();
            dividirImagen(src);


        }
        if (id == R.id.btn_pick_img) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);


        } else if (id == R.id.btn_apply_img) {
            posFilt = 0;
            lastFilter = "normal";
            brillo = brillant = contraste = contant = 1;
            saturacion = satant = 10;

            if (contrasteBrillo != null) {
                ultimo = src;

                src = contrasteBrillo;
                showing = src;
                contrasteBrillo = null;
            } else {
                ultimo = src;

                src = showing;
            }
            Toast.makeText(getApplicationContext(), "Cambios aplicados", Toast.LENGTH_SHORT).show();
            imgFiltered.clear();
            if (findViewById(R.id.guardarImagen) == null) tabSelected(R.id.tab1);

        } else if (id == R.id.btn_revert_img) {
            lastFilter = "normal";
            if (!src.equals(showing) || contrasteBrillo != null) {
                brillo = brillant = contraste = contant = 1;
                saturacion = satant = 10;
                showing = src;

                contrasteBrillo = null;

            } else {
                src = ultimo;

                showing = src;
                contrasteBrillo = null;

            }

            imgMain.setImageBitmap(showing);
            imgFiltered.clear();
            vista.clear();
            tabSelected(R.id.tab1);


        } else if (id == 0) //Normal
        {


            new ParaTask("Normal").execute(new Params(true, "normal", "Normal", src, 0, 0, 0, 100, 0, 0, contraste, brillo, saturacion));
            lastFilter = "normal";


        } else if (id == 1) //Sepia
        {
            new ParaTask("Sepia").execute(new Params(true, "sepia", "Sepia", src, 1.5, 0.6, 0.12, 100, 0, 1, contraste, brillo, saturacion));
            lastFilter = "sepia";


        } else if (id == 2) //Gaussian blur
        {
            new ParaTask("Gaussian")
                    .execute(new Params(true, "gauss", "Gaussian", src, 0, 0, 0, 16, 0, 2, contraste, brillo, saturacion));
            lastFilter = "gauss";


        } else if (id == 3) //inversa
        {
            new ParaTask("Inversa")
                    .execute(new Params(true, "inv", "Inversa", src, 0, 0, 0, 0, 0, 3, contraste, brillo, saturacion));

            lastFilter = "inv";
        } else if (id == 4) //Grises
        {
            new ParaTask("Gris")
                    .execute(new Params(true, "grey", "Gris", src, 0, 0, 0, 0, 0, 4, contraste, brillo, saturacion));

            lastFilter = "grey";

        } else if (id == 5) //Sharp
        {
            new ParaTask("Sharpen")
                    .execute(new Params(true, "sharp", "Sharpen", src, 0, 0, 0, 9, 0, 5, contraste, brillo, saturacion));
            lastFilter = "sharp";


        } else if (id == 6) //Edge detection
        {
            new ParaTask("Edge Detect")
                    .execute(new Params(true, "edetect", "Edge Detect", src, 0, 0, 0, 100, 0, 6, contraste, brillo, saturacion));

            lastFilter = "edetect";

        } else if (id == 7) //Suavizado
        {
            new ParaTask("Suavizado")
                    .execute(new Params(true, "smooth", "Suavizado", src, 0, 0, 0, 100, 0, 7, contraste, brillo, saturacion));

            lastFilter = "smooth";

        } else if (id == 8) //Emboss
        {
            new ParaTask("Relieve")
                    .execute(new Params(true, "emboss", "Relieve", src, 0, 0, 0, 100, 0, 8, contraste, brillo, saturacion));

            lastFilter = "emboss";

        } else if (id == 9) //Halo
        {
            new ParaTask("Halo")
                    .execute(new Params(true, "halo", "Halo", src, 0, 0, 0, 9, 0, 9, contraste, brillo, saturacion));

            lastFilter = "halo";

        }

        //------
        else if (id == 10) //Efecto rojo
        {
            new ParaTask("Efecto Rojo")
                    .execute(new Params(true, "filtroColor", "Efecto Rojo", src, 1, 0, 0, 9, 0, 10, contraste, brillo, saturacion));

            lastFilter = "efectoRojo";

        } else if (id == 11) //Efecto azul
        {
            new ParaTask("Efecto Azul")
                    .execute(new Params(true, "filtroColor", "Efecto Azul", src, 0, 0, 1, 9, 0, 11, contraste, brillo, saturacion));

            lastFilter = "efectoAzul";

        } else if (id == 12) //efecto verde
        {
            new ParaTask("Efecto Verde")
                    .execute(new Params(true, "filtroColor", "Efecto Verde", src, 0, 1, 0, 9, 0, 12, contraste, brillo, saturacion));

            lastFilter = "efectoVerde";

        } else if (id == 13) //Bajorelieve
        {
            new ParaTask("Engrave")
                    .execute(new Params(true, "engrave", "Engrave", src, 0, 0, 0, 0, 0, 13, contraste, brillo, saturacion));

            lastFilter = "engrave";

        }

    }

    public Bitmap escalar(Bitmap img, boolean thumb) {


        try {


            // The new size we want to scale to
            int REQUIRED_SIZE;
            if (thumb == true) {
                REQUIRED_SIZE = imgSize / 4;
            } else REQUIRED_SIZE = imgSize;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = img.getWidth(), height_tmp = img.getHeight();

            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        && height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;

            }

            // Decode with inSampleSize
            return Bitmap.createScaledBitmap(img, width_tmp, height_tmp, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    pathImagen = data.getData();

                    Bitmap bmp = decodeUri(pathImagen, false);
                    Bitmap tbmp = escalar(bmp, true);
                    if (bmp != null) {
                        thumbnail = tbmp;

                        src = bmp;
                        ultimo = src;
                        imgMain.setImageBitmap(src);
                        imgFiltered.clear();
                        showing = src;
                        posFilt = 0;
                        Toast.makeText(this, "Procesando vista previa...", Toast.LENGTH_SHORT).show();
                        contant = contraste = 1;
                        brillant = brillo = 1;
                        satant = saturacion = 10;
                        lastFilter = "normal";
                        tabSelected(R.id.tab1);
                    }
                }
                break;
            case SELECT_SHARE:
                //Toast.makeText(this, "Compartido correctamente!", Toast.LENGTH_SHORT).show();


                break;
        }
    }

    public void dividirImagen(Bitmap img) {

        PreparaBitmap dividedBm = new PreparaBitmap(img);
        bim = new Bitmap[4];
        bim[0] = dividedBm.getBm1();
        bim[1] = dividedBm.getBm2();
        bim[2] = dividedBm.getBm3();
        bim[3] = dividedBm.getBm4();
    }

    public void guardarImagen(View v) {
        String extension;
        AlertDialog.Builder al = new AlertDialog.Builder(this);
        EditText et = (EditText) findViewById(R.id.editText);
        if (!et.getText().toString().replace(" ", "").equals("")) {
            if (!src.equals(showing) || contrasteBrillo != null) {
                Toast.makeText(this, "Hay cambios sin aplicar", Toast.LENGTH_SHORT).show();

            } else {
                try {

                    if (defaultFormat.equals(Bitmap.CompressFormat.PNG)) extension = ".png";
                    else extension = ".jpeg";
                    File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/IDIGram/" + et.getText().toString() + extension);
                    FileOutputStream fos = new FileOutputStream(f);
                    src.compress(defaultFormat, 90, fos);

                    Toast.makeText(this, "Guardado correctamente en " + f.getAbsolutePath(), Toast.LENGTH_SHORT).show();

                    al
                            .setTitle("Compartir")
                            .setMessage("Quieres compartir la foto guardada?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    share();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_menu_share)
                            .show();


                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(this, "Ha ocurrido un problema guardando", Toast.LENGTH_SHORT).show();
                }

            }
        } else {
            Toast.makeText(this, "Tienes que ponerle un nombre al archivo", Toast.LENGTH_SHORT).show();
        }
    }

    private void share() {
        Bitmap icon = src;
        String extension;
        Intent share = new Intent(Intent.ACTION_SEND);
        if (defaultFormat.equals(Bitmap.CompressFormat.PNG)) extension = "png";
        else extension = "jpeg";
        share.setType("image/" + extension);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        icon.compress(defaultFormat, 90, bytes);
        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "idi_gram_share." + extension);
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/idi_gram_share." + extension));

        startActivityForResult(Intent.createChooser(share, "Share Image"), SELECT_SHARE);

    }


    private Bitmap decodeUri(Uri selectedImage, boolean thumb) {

        try {

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

            // The new size we want to scale to
            int REQUIRED_SIZE;
            if (thumb == true) {
                REQUIRED_SIZE = imgSize / 4;
            } else REQUIRED_SIZE = imgSize;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        && height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;

            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private class Pair {
        String tipo;
        String label;
        Bitmap bm, bm1, bm2, bm3, bm4;
        int id;
        boolean procesa;

        public Pair(String label, String tipo, Bitmap bm, int id, boolean proc) {
            super();
            this.label = label;
            this.bm = bm;
            this.tipo = tipo;
            this.id = id;
            this.procesa = proc;

        }

        public Bitmap getBm1() {
            return bm1;
        }

        public Bitmap getBm2() {
            return bm2;
        }

        public Bitmap getBm3() {
            return bm3;
        }

        public Bitmap getBm4() {
            return bm4;
        }

        public String getLabel() {
            return label;
        }

        public String getTipo() {
            return tipo;
        }

        public Bitmap getBm() {
            return bm;
        }

        public int getId() {
            return id;
        }

        public boolean isProcesa() {
            return procesa;
        }


    }


    private class ParaTask extends AsyncTask<Params, Void, Pair> {
        ProgressDialog barProgressDialog = null;
        private View load;
        private String nombreEtiqueta;


        public ParaTask(String label) {
            nombreEtiqueta = label;

        }

        protected Pair doInBackground(Params... arg0) {

            ImageFilters imgFilter = new ImageFilters();
            Bitmap bm = arg0[0].getBm();
            Log.d("Ancho imagen antes", Integer.toString(bm.getWidth()));
            String tipo = arg0[0].getTipo();
            String label = arg0[0].getLabel();
            double R = arg0[0].getR();
            double G = arg0[0].getG();
            double B = arg0[0].getB();
            float percent = arg0[0].getPercent();
            double valor = arg0[0].getValor();
            int id = arg0[0].getId();
            Bitmap partes[] = new Bitmap[4];

            partes[0] = Bitmap.createBitmap(bim[0].getWidth(), bim[0].getHeight(), Bitmap.Config.ARGB_8888);
            partes[1] = Bitmap.createBitmap(bim[1].getWidth(), bim[1].getHeight(), Bitmap.Config.ARGB_8888);
            partes[2] = Bitmap.createBitmap(bim[2].getWidth(), bim[2].getHeight(), Bitmap.Config.ARGB_8888);
            partes[3] = Bitmap.createBitmap(bim[3].getWidth(), bim[3].getHeight(), Bitmap.Config.ARGB_8888);
            //Bitmap result = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);

            if (imgFiltered.containsKey(tipo) && arg0[0].isProcesa() == false)
                return new Pair(label, tipo, null, id, arg0[0].isProcesa());


            long startTime = System.currentTimeMillis();

            Runnable task1 = new Hilo(arg0[0], bim[0], partes[0], 1, barProgressDialog);
            Runnable task2 = new Hilo(arg0[0], bim[1], partes[1], 2, barProgressDialog);
            Runnable task3 = new Hilo(arg0[0], bim[2], partes[2], 3, barProgressDialog);
            Runnable task4 = new Hilo(arg0[0], bim[3], partes[3], 4, barProgressDialog);
            Thread parte1 = new Thread(task1, "Parte-" + 1);
            Thread parte2 = new Thread(task2, "Parte-" + 2);
            Thread parte3 = new Thread(task3, "Parte-" + 3);
            Thread parte4 = new Thread(task4, "Parte-" + 4);
            //4,2,3,1
            try {
                parte4.start();

                parte2.start();

                parte3.start();

                parte1.start();
                parte4.join();
                parte2.join();
                parte3.join();
                parte1.join();


            } catch (Exception e) {
                e.printStackTrace();
            }
            PreparaBitmap pBm = new PreparaBitmap(bm, partes[0], partes[1], partes[2], partes[3]);
            Bitmap result = pBm.joinBm();
            Log.d("Ancho imagen despues", Integer.toString(result.getWidth()));
            // Bitmap result =partes[0];
            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;


            imgFiltered.put(label, result);

            System.out.println("Filtro: " + label + " Tiempo:" + elapsedTime);


            return new Pair(label, tipo, result, id, arg0[0].isProcesa());
        }

        protected void onPostExecute(Pair result) {
            // Pass the result data back to the main activity
            if (result != null && result.isProcesa() == false) {
                borrarLoading(load);
                insertarEnFiltros(result.getLabel(),
                        imgFiltered.get(result.getLabel()), result.getId());
            } else if (result != null && result.isProcesa() == true) {
                imgMain.setImageBitmap(result.getBm());
                if (!result.getTipo().equals("")) showing = result.getBm();
                else {
                    contrasteBrillo = result.getBm();
                    Toast.makeText(getApplicationContext(), "Procesado", Toast.LENGTH_SHORT).show();
                }
            }

        }

        protected void onPreExecute() {
            if (!imgFiltered.containsKey(nombreEtiqueta) && !nombreEtiqueta.equals(""))
                load = meterLoading();
            else {
                barProgressDialog = new ProgressDialog(MainActivity.this);

                barProgressDialog.setTitle("Procesando imagen...");

                barProgressDialog.setMessage("Procesando ...");

                barProgressDialog.setProgressStyle(barProgressDialog.STYLE_HORIZONTAL);

                barProgressDialog.setProgress(0);

                barProgressDialog.setMax(100);

                barProgressDialog.show();

            }
            //Toast.makeText(getApplicationContext(), "Procesando...", Toast.LENGTH_SHORT).show();

        }

    }
}
