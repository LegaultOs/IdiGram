package com.legaultOs.idigram;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;


import com.example.legault.idigram.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MainActivity extends ActionBarActivity {

    private ImageView imgMain ;
    private static final int SELECT_PHOTO = 100;
    private Bitmap src,showing,contrasteBrillo,loading,ultimo,thumbnail,ultimoThu;
    private HashMap<String,Bitmap> imgFiltered;
    private ArrayList<String> vista;
    private int brillo=1,brillant=1,contraste=1,contant=1,saturacion=1,satant=1;
    private int imgSize=400;
    private Bitmap.CompressFormat defaultFormat=Bitmap.CompressFormat.PNG;
    private Uri pathImagen;
    private Bitmap bim[];

    int posFilt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundle = getIntent().getExtras();
        loading = BitmapFactory.decodeResource(getResources(), R.drawable.loading);
        File f =new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/IDIGram/");
        if(!f.exists())f.mkdir();
        imgMain = (ImageView) findViewById(R.id.effect_main);
        pathImagen=(Uri) bundle.get("pathImagen");
        src = decodeUri(pathImagen,false);

        thumbnail=escalar(src,true);

        ultimo=src;
        imgMain.setImageBitmap(src);
        imgFiltered=new HashMap<String,Bitmap>();
        showing=src;
        vista= new ArrayList<String>();

        contrasteBrillo=null;




        imgMain.setOnTouchListener(new View.OnTouchListener()
        {

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    imgMain.setImageBitmap(src);
                }

                else if (event.getAction() == MotionEvent.ACTION_UP){
                    if(contrasteBrillo!=null) imgMain.setImageBitmap(contrasteBrillo);
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
        AlertDialog.Builder al =  new AlertDialog.Builder(this);
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
                        .setMessage("Hecho por: Oscar Carod \nE-mail:oscaracso90@gmail.com \nIDI")
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
                final View menuSettings=layoutInflater.inflate(R.layout.settings,null);
                TextView tv=(TextView)menuSettings.findViewById(R.id.editText2);
                tv.setText(Integer.toString(imgSize));
                RadioGroup rg =(RadioGroup) menuSettings.findViewById(R.id.radiogroup1);
                if(defaultFormat.equals(Bitmap.CompressFormat.PNG)) rg.check(R.id.png);
                else rg.check(R.id.jpeg);
                al
                        .setTitle("Settings")
                        .setView(menuSettings);
                al        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        TextView tv=(TextView)menuSettings.findViewById(R.id.editText2);
                        if(imgSize!=Integer.parseInt( tv.getText().toString())){
                            imgSize = Integer.parseInt( tv.getText().toString());
                            src = decodeUri(pathImagen,false);
                            thumbnail=escalar(src,true);
                            imgFiltered.clear();
                            tabSelected(R.id.tab1);

                        }
                        RadioGroup rg =(RadioGroup) menuSettings.findViewById(R.id.radiogroup1);
                        if(rg.getCheckedRadioButtonId()==R.id.png) defaultFormat= Bitmap.CompressFormat.PNG;
                        else defaultFormat= Bitmap.CompressFormat.JPEG;
                    }
                })
                        .setIcon(android.R.drawable.ic_menu_manage)
                        .show();

                return true;
        }
        return true;
    }

    private void insertarEnFiltros(String nombreFiltro,Bitmap preview,int Id)
    {
        LinearLayout ll= (LinearLayout) findViewById(R.id.espaciofiltros);
        if(ll!=null){
            View vv = View.inflate(this, R.layout.filt, null);
            ImageView iv=(ImageView)vv.findViewById(R.id.imagenFilt);
            TextView tv=(TextView)vv.findViewById(R.id.nombreFilt);
            iv.setImageBitmap(preview);
            iv.setId(Id);
            tv.setText(nombreFiltro);
            if(!vista.contains(nombreFiltro)){
                ll.addView(vv, posFilt ,new LinearLayout.LayoutParams(ll.getLayoutParams().width, ll.getLayoutParams().height));
                vista.add(nombreFiltro);}
            posFilt++;}
    }

    private void borrarLoading(View v)
    {

        LinearLayout ll = (LinearLayout) findViewById(R.id.espaciofiltros);
        if(ll!=null && v!=null){
            ll.removeView(v);}

    }
    private View meterLoading()
    {
        LinearLayout ll = (LinearLayout) findViewById(R.id.espaciofiltros);
        View vv = View.inflate(this, R.layout.filt, null);
        if(ll!=null){

            ImageView iv = (ImageView) vv.findViewById(R.id.imagenFilt);
            TextView tv = (TextView) vv.findViewById(R.id.nombreFilt);
            iv.setImageBitmap(loading);
            tv.setText("Cargando...");
            ll.addView(vv, new LinearLayout.LayoutParams(
                    ll.getLayoutParams().width, ll.getLayoutParams().height));}
        return vv;


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void llenarMuestras()
    {
        ImageFilters imgFilter = new ImageFilters();

        PreparaBitmap dividedBm= new PreparaBitmap(escalar(src,true));
        bim=new Bitmap[4];
        bim[0]=dividedBm.getBm1();
        bim[1]=dividedBm.getBm2();
        bim[2]=dividedBm.getBm3();
        bim[3]=dividedBm.getBm4();

        LinearLayout ll= (LinearLayout) findViewById(R.id.espaciofiltros);
        ll.removeAllViews();
        vista.clear();
        if(imgFiltered.containsKey("normal"))insertarEnFiltros("Normal",imgFiltered.get("normal"),0);
        else new ParaTask("normal")
                .execute(new Params(false,"normal", "Normal", thumbnail, 1.5, 0.6, 0.12, 100, 0, 0,contraste,brillo,saturacion));
        if(imgFiltered.containsKey("sepia"))insertarEnFiltros("Sepia",imgFiltered.get("sepia"),1);
        else new ParaTask("sepia")
                .execute(new Params(false,"sepia", "Sepia", thumbnail, 1.5, 0.6, 0.12, 100, 0, 1,contraste,brillo,saturacion));
        if(imgFiltered.containsKey("sharp"))insertarEnFiltros("Sharpen",imgFiltered.get("sharp"),5);
        else new ParaTask("sharp")
                .execute(new Params(false,"sharp", "Sharpen", thumbnail, 0, 0, 0, 9, 0, 5,contraste,brillo,saturacion));
        if(imgFiltered.containsKey("inv"))insertarEnFiltros("Inversa",imgFiltered.get("inv"),3);
        else new ParaTask("inv")
                .execute(new Params(false,"inv", "Inversa", thumbnail, 0, 0, 0, 0, 0, 3,contraste,brillo,saturacion));
        if(imgFiltered.containsKey("grey"))insertarEnFiltros("Gris",imgFiltered.get("grey"),4);
        else new ParaTask("grey")
                .execute(new Params(false,"grey", "Gris", thumbnail, 0, 0, 0, 0, 0, 4,contraste,brillo,saturacion));
        if(imgFiltered.containsKey("edetect"))insertarEnFiltros("Edge Detect",imgFiltered.get("edetect"),6);
        else new ParaTask("edetect")
                .execute(new Params(false,"edetect", "Edge Detect", thumbnail, 0, 0, 0, 100, 0, 6,contraste,brillo,saturacion));
        if(imgFiltered.containsKey("smooth"))insertarEnFiltros("Suavizado",imgFiltered.get("smooth"),7);
        else new ParaTask("smooth")
                .execute(new Params(false,"smooth", "Suavizado", thumbnail, 0, 0, 0, 100, 0, 7,contraste,brillo,saturacion));
        if(imgFiltered.containsKey("emboss"))insertarEnFiltros("Emboss",imgFiltered.get("emboss"),8);
        else new ParaTask("emboss")
                .execute(new Params(false,"emboss", "Emboss", thumbnail, 0, 0, 0, 100, 0, 8,contraste,brillo,saturacion));
        if(imgFiltered.containsKey("gauss"))insertarEnFiltros("Gaussian",imgFiltered.get("gauss"),2);
        else new ParaTask("gauss")
                .execute(new Params(false,"gauss", "Gaussian", thumbnail, 0, 0, 0, 16, 0, 2,contraste,brillo,saturacion));
        if(imgFiltered.containsKey("halo"))insertarEnFiltros("Halo",imgFiltered.get("halo"),9);
        else new ParaTask("halo")
                .execute(new Params(false,"halo", "Halo", thumbnail, 0, 0, 0, 9, 0, 9,contraste,brillo,saturacion));

    }

    public void tabSelected(int tab) {
        LinearLayout ll = (LinearLayout) findViewById(R.id.espacio);
        TextView t1 = (TextView) findViewById(R.id.tab1);
        TextView t2 = (TextView) findViewById(R.id.tab2);
        TextView t3 = (TextView) findViewById(R.id.tab3);
        View vv;
        switch (tab) {
            case R.id.tab1:
                // Toast.makeText(this, "Procesando vista previa...", Toast.LENGTH_SHORT).show();
                t1.setBackgroundColor(Color.parseColor("#80A7C2"));
                t2.setBackgroundColor(Color.parseColor("#354D5E"));
                t3.setBackgroundColor(Color.parseColor("#354D5E"));
                ll.removeAllViews();

                vv = View.inflate(this, R.layout.filtros, null);
                ll.addView(vv, new LinearLayout.LayoutParams(ll.getLayoutParams().width, ll.getLayoutParams().height));
                posFilt=0;
                if(contraste!=contant || brillo!=brillant || saturacion!=satant)
                {imgFiltered.clear();
                    contant=contraste;
                    brillant=brillo;
                    satant=saturacion;


                }


                llenarMuestras();


                break;
            case R.id.tab2:
                t1.setBackgroundColor(Color.parseColor("#354D5E"));
                t2.setBackgroundColor(Color.parseColor("#80A7C2"));
                t3.setBackgroundColor(Color.parseColor("#354D5E"));
                ll.removeAllViews();
                vv = View.inflate(this, R.layout.ajustes, null);
                ll.addView(vv, new LinearLayout.LayoutParams(ll.getLayoutParams().width, ll.getLayoutParams().height));

                SeekBar sb1 = (SeekBar) findViewById(R.id.seekBar1);//Contraste
                sb1.setProgress(contraste+100);
                sb1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    int progressChanged = 0;

                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                        progressChanged = progress;
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // TODO Auto-generated method stub
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                        contant=contraste;
                        contraste=progressChanged-100;
                        Bitmap bm= camibiarContrBrilloSat(showing,false,contraste,brillo,saturacion);
                        imgMain.setImageBitmap(bm);
                    }
                });

                SeekBar sb2 = (SeekBar) findViewById(R.id.seekBar2);//Brillo
                sb2.setProgress(brillo+100);
                sb2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    int progressChanged = 0;

                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                        progressChanged = progress;
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // TODO Auto-generated method stub
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                        brillant=brillo;
                        brillo=progressChanged-100;
                        Bitmap bm= camibiarContrBrilloSat(showing,false,contraste,brillo,saturacion);
                        imgMain.setImageBitmap(bm);

                    }
                });
                SeekBar sb3 = (SeekBar) findViewById(R.id.seekBar3);//Brillo
                sb3.setProgress(saturacion+49);
                sb3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    int progressChanged = 50;

                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                        progressChanged = progress;
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // TODO Auto-generated method stub
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                        satant=saturacion;
                        saturacion=progressChanged-40;
                        Bitmap bm= camibiarContrBrilloSat(showing,false,contraste,brillo,saturacion);
                        imgMain.setImageBitmap(bm);

                    }
                });

                break;
            case R.id.tab3:
                t1.setBackgroundColor(Color.parseColor("#354D5E"));
                t2.setBackgroundColor(Color.parseColor("#354D5E"));
                t3.setBackgroundColor(Color.parseColor("#80A7C2"));
                ll.removeAllViews();
                vv = View.inflate(this, R.layout.guardar, null);
                ll.addView(vv, new LinearLayout.LayoutParams(ll.getLayoutParams().width, ll.getLayoutParams().height));
                //Profe -> Mejor un popup para introducir el nombre del archivo.


                break;
        }
    }

    private Bitmap camibiarContrBrilloSat(Bitmap vit, boolean loading,int cont,int brill,int sat)
    {

        ImageFilters imgFilter = new ImageFilters();
        Bitmap bm = imgFilter.applySaturationFilter(vit, sat);
        bm = imgFilter.applyContrastEffect(bm, cont);
        bm = imgFilter.applyBrightnessEffect(bm, brill);
        if(!loading)contrasteBrillo=bm;
        return bm;

    }
    public void tabClicked(View v){


        tabSelected(v.getId());


    }

    public void buttonClicked(View v){

        //Toast.makeText(this, "Procesando...", Toast.LENGTH_SHORT).show();
        ImageFilters imgFilter = new ImageFilters();
        int id=v.getId();


        System.out.println(id);
        if(id!=R.id.btn_pick_img && id!=R.id.btn_apply_img && id!=R.id.btn_revert_img )// querra decir que hemos clickado a un filtro
        {
            contrasteBrillo=null;
            Toast.makeText(this, "Procesando filtro...", Toast.LENGTH_SHORT).show();
            PreparaBitmap dividedBm= new PreparaBitmap(src);
            bim=new Bitmap[4];
            bim[0]=dividedBm.getBm1();
            bim[1]=dividedBm.getBm2();
            bim[2]=dividedBm.getBm3();
            bim[3]=dividedBm.getBm4();

        }
        if(id == R.id.btn_pick_img){
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);


        }
        else if(id == R.id.btn_apply_img){
            posFilt=0;
            brillo=brillant=contraste=contant=saturacion=satant=1;

            if(contrasteBrillo!=null){
                ultimo=src;

                src=contrasteBrillo;
                showing=src;
                contrasteBrillo=null;
            }
            else {
                ultimo=src;

                src=showing;}
            imgFiltered.clear();
            tabSelected(R.id.tab1);

        }
        else if(id == R.id.btn_revert_img){

            if(!src.equals(showing) || contrasteBrillo!=null)
            {brillo=brillant=contraste=contant=saturacion=satant=1;
                showing=src;

                contrasteBrillo=null;

            }
            else
            {   src=ultimo;

                showing=src;
                contrasteBrillo=null;

            }

            imgMain.setImageBitmap(showing);
            imgFiltered.clear();
            vista.clear();
            tabSelected(R.id.tab1);


        }

        else if(id==0) //Normal
        {

            new ParaTask("normal").execute(new Params(true,"normal", "Normal", src, 1.5, 0.6, 0.12, 100, 0, 1,contraste,brillo,saturacion));


        }
        else if(id==1) //Sepia
        {
            new ParaTask("sepia").execute(new Params(true,"sepia", "Sepia", src, 1.5, 0.6, 0.12, 100, 0, 1,contraste,brillo,saturacion));



        }
        else if(id==2) //Gaussian blur
        {
            new ParaTask("gauss")
                    .execute(new Params(true,"gauss", "Gaussian", src, 0, 0, 0, 16, 0, 2,contraste,brillo,saturacion));



        }
        else if(id==3) //inversa
        {
            new ParaTask("inv")
                    .execute(new Params(true,"inv", "Inversa", src, 0, 0, 0, 0, 0, 3,contraste,brillo,saturacion));


        }
        else if(id==4) //Grises
        {
            new ParaTask("grey")
                    .execute(new Params(true,"grey", "Gris", src, 0, 0, 0, 0, 0, 4,contraste,brillo,saturacion));



        }
        else if(id==5) //Sharp
        {
            new ParaTask("sharp")
                    .execute(new Params(true,"sharp", "Sharpen", src, 0, 0, 0, 9, 0, 5,contraste,brillo,saturacion));



        }
        else if(id==6) //Edge detection
        {
            new ParaTask("edetect")
                    .execute(new Params(true,"edetect", "Edge Detect", src, 0, 0, 0, 100, 0, 6,contraste,brillo,saturacion));



        }
        else if(id==7) //Suavizado
        {
            new ParaTask("smooth")
                    .execute(new Params(true,"smooth", "Suavizado", src, 0, 0, 0, 100, 0, 7,contraste,brillo,saturacion));



        }
        else if(id==8) //Emboss
        {
            new ParaTask("emboss")
                    .execute(new Params(true,"emboss", "Emboss", src, 0, 0, 0, 100, 0, 8,contraste,brillo,saturacion));



        }
        else if(id==9) //Halo
        {
            new ParaTask("halo")
                    .execute(new Params(true,"halo", "Halo", src, 0, 0, 0, 9, 0, 9,contraste,brillo,saturacion));



        }
//        else if(v.getId() == R.id.effect_highlight)
//            saveBitmap(imgFilter.applyHighlightEffect(src), "effect_highlight");
       /* else if(v.getId() == R.id.effect_black)
            saveBitmap(imgFilter.applyBlackFilter(src),"effect_black");
        else if(v.getId() == R.id.effect_boost_1)
            saveBitmap(imgFilter.applyBoostEffect(src, 1, 40),"effect_boost_1");
        else if(v.getId() == R.id.effect_boost_2)
            saveBitmap(imgFilter.applyBoostEffect(src, 2, 30),"effect_boost_2");
        else if(v.getId() == R.id.effect_boost_3)
            saveBitmap(imgFilter.applyBoostEffect(src, 3, 67),"effect_boost_3");
        else if(v.getId() == R.id.effect_brightness)
            saveBitmap(imgFilter.applyBrightnessEffect(src, 80),"effect_brightness");
        else if(v.getId() == R.id.effect_color_red)
            saveBitmap(imgFilter.applyColorFilterEffect(src, 255, 0, 0),"effect_color_red");
        else if(v.getId() == R.id.effect_color_green)
            saveBitmap(imgFilter.applyColorFilterEffect(src, 0, 255, 0),"effect_color_green");
        else if(v.getId() == R.id.effect_color_blue)
            saveBitmap(imgFilter.applyColorFilterEffect(src, 0, 0, 255),"effect_color_blue");
        else if(v.getId() == R.id.effect_color_depth_64)
            saveBitmap(imgFilter.applyDecreaseColorDepthEffect(src, 64),"effect_color_depth_64");
        else if(v.getId() == R.id.effect_color_depth_32)
            saveBitmap(imgFilter.applyDecreaseColorDepthEffect(src, 32),"effect_color_depth_32");
        else if(v.getId() == R.id.effect_contrast)
            saveBitmap(imgFilter.applyContrastEffect(src, 70),"effect_contrast");
        else if(v.getId() == R.id.effect_emboss)
            saveBitmap(imgFilter.applyEmbossEffect(src),"effect_emboss");
        else if(v.getId() == R.id.effect_engrave)
            saveBitmap(imgFilter.applyEngraveEffect(src),"effect_engrave");
        else if(v.getId() == R.id.effect_flea)
            saveBitmap(imgFilter.applyFleaEffect(src),"effect_flea");
        else  if(v.getId() == R.id.effect_gaussian_blue)
            saveBitmap(imgFilter.applyGaussianBlurEffect(src),"effect_gaussian_blue");
        else if(v.getId() == R.id.effect_gamma)
            saveBitmap(imgFilter.applyGammaEffect(src, 1.8, 1.8, 1.8),"effect_gamma");
        else if(v.getId() == R.id.effect_grayscale)
            saveBitmap(imgFilter.applyGreyscaleEffect(src),"effect_grayscale");
        else  if(v.getId() == R.id.effect_hue)
            saveBitmap(imgFilter.applyHueFilter(src, 2),"effect_hue");
        else if(v.getId() == R.id.effect_invert)
            saveBitmap(imgFilter.applyInvertEffect(src),"effect_invert");
        else if(v.getId() == R.id.effect_mean_remove)
            saveBitmap(imgFilter.applyMeanRemovalEffect(src),"effect_mean_remove");
//        else if(v.getId() == R.id.effect_reflaction)
//            saveBitmap(imgFilter.applyReflection(src),"effect_reflaction");
        else if(v.getId() == R.id.effect_round_corner)
            saveBitmap(imgFilter.applyRoundCornerEffect(src, 45),"effect_round_corner");
        else if(v.getId() == R.id.effect_saturation)
            saveBitmap(imgFilter.applySaturationFilter(src, 1),"effect_saturation");
        else if(v.getId() == R.id.effect_sepia)
            saveBitmap(imgFilter.applySepiaToningEffect(src, 10, 1.5, 0.6, 0.12),"effect_sepia");
        else if(v.getId() == R.id.effect_sepia_green)
            saveBitmap(imgFilter.applySepiaToningEffect(src, 10, 0.88, 2.45, 1.43),"effect_sepia_green");
        else if(v.getId() == R.id.effect_sepia_blue)
            saveBitmap(imgFilter.applySepiaToningEffect(src, 10, 1.2, 0.87, 2.1),"effect_sepia_blue");
        else if(v.getId() == R.id.effect_smooth)
            saveBitmap(imgFilter.applySmoothEffect(src, 100),"effect_smooth");
        else if(v.getId() == R.id.effect_sheding_cyan)
            saveBitmap(imgFilter.applyShadingFilter(src, Color.CYAN),"effect_sheding_cyan");
        else if(v.getId() == R.id.effect_sheding_yellow)
            saveBitmap(imgFilter.applyShadingFilter(src, Color.YELLOW),"effect_sheding_yellow");
        else if(v.getId() == R.id.effect_sheding_green)
            saveBitmap(imgFilter.applyShadingFilter(src, Color.GREEN),"effect_sheding_green");
        else if(v.getId() == R.id.effect_tint)
            saveBitmap(imgFilter.applyTintEffect(src, 100),"effect_tint");
        else if(v.getId() == R.id.effect_watermark)
            saveBitmap(imgFilter.applyWaterMarkEffect(src, "kpbird.com", 200, 200, Color.GREEN, 80, 24, false),"effect_watermark");*/

    }
    public Bitmap escalar(Bitmap img,boolean thumb)
    {


        try {



            // The new size we want to scale to
            int REQUIRED_SIZE;
            if(thumb==true){
                REQUIRED_SIZE = imgSize/4;}
            else REQUIRED_SIZE = imgSize;

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
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    pathImagen = data.getData();

                    Bitmap bmp = decodeUri(pathImagen,false);
                    Bitmap tbmp = escalar(bmp,true);
                    if(bmp !=null){
                        thumbnail=tbmp;

                        src = bmp;
                        ultimo=src;
                        imgMain.setImageBitmap(src);
                        imgFiltered.clear();
                        showing=src;
                        posFilt=0;
                        Toast.makeText(this, "Procesando vista previa...", Toast.LENGTH_SHORT).show();
                        contant=contraste=1;
                        brillant=brillo=1;
                        satant=saturacion=1;
                        tabSelected(R.id.tab1);
                    }
                }
        }
    }

    public void dividirImagen(Bitmap img)
    {

        PreparaBitmap dividedBm= new PreparaBitmap(img);
        bim=new Bitmap[4];
        bim[0]=dividedBm.getBm1();
        bim[1]=dividedBm.getBm2();
        bim[2]=dividedBm.getBm3();
        bim[3]=dividedBm.getBm4();
    }

    public void guardarImagen(View v)
    {
        String extension;
        EditText et= (EditText)findViewById(R.id.editText);
        if(!et.getText().toString().replace(" ","").equals("")) {
            if (!src.equals(showing) || contrasteBrillo != null) {
                Toast.makeText(this, "Hay cambios sin aplicar", Toast.LENGTH_SHORT).show();

            } else {
                try {

                    if(defaultFormat.equals(Bitmap.CompressFormat.PNG)) extension=".png";
                    else extension=".jpeg";
                    File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/IDIGram/" + et.getText().toString() + extension);
                    FileOutputStream fos = new FileOutputStream(f);
                    src.compress(defaultFormat, 90, fos);

                    Toast.makeText(this, "Guardado correctamente en " + f.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(this, "Ha ocurrido un problema guardando", Toast.LENGTH_SHORT).show();
                }

            }
        }
        else
        {
            Toast.makeText(this, "Tienes que ponerle un nombre al archivo", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap decodeUri(Uri selectedImage, boolean thumb)  {

        try {

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

            // The new size we want to scale to
            int REQUIRED_SIZE;
            if(thumb==true){
                REQUIRED_SIZE = imgSize/4;}
            else REQUIRED_SIZE = imgSize;

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
                scale *=2;

            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
























    private class Pair {
        String tipo;
        String label;
        Bitmap bm,bm1,bm2,bm3,bm4;
        int id;
        boolean procesa;

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
        public boolean isProcesa(){return procesa;}

        public Pair(String label, String tipo, Bitmap bm,int id,boolean proc) {
            super();
            this.label = label;
            this.bm = bm;
            this.tipo = tipo;
            this.id=id;
            this.procesa=proc;

        }



    }



    private class ParaTask extends AsyncTask<Params, Void, Pair> {
        private View load;
        private String nombreEtiqueta;
        public ParaTask(String label)
        {
            nombreEtiqueta=label;

        }
        protected Pair doInBackground(Params... arg0) {

            ImageFilters imgFilter = new ImageFilters();
            Bitmap bm = arg0[0].getBm();
            String tipo = arg0[0].getTipo();
            String label = arg0[0].getLabel();
            double R = arg0[0].getR();
            double G = arg0[0].getG();
            double B = arg0[0].getB();
            float percent = arg0[0].getPercent();
            double valor = arg0[0].getValor();
            int id = arg0[0].getId();
            Bitmap result = Bitmap.createBitmap(bm.getWidth(),bm.getHeight(),Bitmap.Config.ARGB_8888) ;

            if (imgFiltered.containsKey(tipo)&&arg0[0].isProcesa()==false)
                return new Pair(label, tipo, null,id,arg0[0].isProcesa());

            Thread[] parteMatriz = new Thread[4];
            long startTime = System.currentTimeMillis();
            SecuenciaHilos orden=new SecuenciaHilos();
            Runnable task1 = new Hilo(arg0[0],bim[0],result,1,orden);
            Runnable task2 = new Hilo(arg0[0],bim[1],result,2,orden);
            Runnable task3 = new Hilo(arg0[0],bim[2],result,3,orden);
            Runnable task4 = new Hilo(arg0[0],bim[3],result,4,orden);
            Thread parte1 = new Thread(task1, "Parte-"+1);
            Thread parte2 = new Thread(task2, "Parte-"+2);
            Thread parte3 = new Thread(task3, "Parte-"+3);
            Thread parte4 = new Thread(task4, "Parte-"+4);
            //4,2,3,1
            try{
            parte4.start();
            parte4.join();
            parte2.start();
            parte2.join();
            parte3.start();
            parte3.join();
            parte1.start();
            parte1.join();



            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;

            imgFiltered.put(tipo, result);

            System.out.println("Filtro: "+label+" Tiempo:"+elapsedTime);


            return new Pair(label, tipo, result,id,arg0[0].isProcesa());
        }

        protected void onPostExecute(Pair result) {
            // Pass the result data back to the main activity
            if (result != null && result.isProcesa()==false){
                borrarLoading(load);
                insertarEnFiltros(result.getLabel(),
                        imgFiltered.get(result.getTipo()),result.getId());}
            else if(result !=null &&  result.isProcesa()==true)
            {
                imgMain.setImageBitmap(result.getBm());
                showing=result.getBm();
                Toast.makeText(getApplicationContext(), "Procesado", Toast.LENGTH_SHORT).show();
            }

        }
        protected  void onPreExecute()
        {
            if (!imgFiltered.containsKey(nombreEtiqueta)) load=meterLoading();

        }

    }
}
