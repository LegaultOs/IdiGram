package com.legaultOs.idigram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;


import com.example.legault.idigram.R;

import java.util.HashMap;


public class MainActivity extends ActionBarActivity {

    private ImageView imgMain ;
    private static final int SELECT_PHOTO = 100;
    private Bitmap src,showing,contrasteBrillo;
    private HashMap<String,Bitmap> imgFiltered;
    private int brillo=1,contraste=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundle = getIntent().getExtras();
        File f =new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/IDIGram/");
        if(!f.exists())f.mkdir();
        imgMain = (ImageView) findViewById(R.id.effect_main);
        src = decodeUri((Uri) bundle.get("pathImagen"));
        imgMain.setImageBitmap(src);
        imgFiltered=new HashMap<String,Bitmap>();
        showing=src;
        contrasteBrillo=null;
        tabSelected(R.id.tab1);


    }

    private void insertarEnFiltros(String nombreFiltro,Bitmap preview,int Id)
    {
        LinearLayout ll= (LinearLayout) findViewById(R.id.espaciofiltros);
        View vv = View.inflate(this, R.layout.filt, null);
        ImageView iv=(ImageView)vv.findViewById(R.id.imagenFilt);
        TextView tv=(TextView)vv.findViewById(R.id.nombreFilt);
        iv.setImageBitmap(preview);
        iv.setId(Id);
        tv.setText(nombreFiltro);
        ll.addView(vv, new LinearLayout.LayoutParams(ll.getLayoutParams().width, ll.getLayoutParams().height));
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


        LinearLayout ll= (LinearLayout) findViewById(R.id.espaciofiltros);
        ll.removeAllViews();

        insertarEnFiltros("Normal",src,0);
        new ParaTask()
                .execute(new Params("tint", "Tinta", src, 0, 0, 0, 100, 0,1));
        new ParaTask()
                .execute(new Params("gauss", "Gaussian", src, 0, 0, 0, 0, 0,2));
        new ParaTask()
                .execute(new Params("inv", "Inversa", src, 0, 0, 0, 0, 0,3));
        new ParaTask()
                .execute(new Params("grey", "Gris", src, 0, 0, 0, 0, 0,4));
    	   /*imgFiltered.put("blackFilter", imgFilter.applyBlackFilter(src));
    	   imgFiltered.put("boost1", imgFilter.applyBoostEffect(src, 1, 40));
    	   imgFiltered.put("boost2", imgFilter.applyBoostEffect(src, 2, 30));
    	   imgFiltered.put("boost3", imgFilter.applyBoostEffect(src, 3, 67));



    	   imgFiltered.put("brightness", imgFilter.applyBrightnessEffect(src, 80));
    	   imgFiltered.put("colorRed", imgFilter.applyColorFilterEffect(src, 255, 0, 0));
    	   imgFiltered.put("colorGreen", imgFilter.applyColorFilterEffect(src, 0, 255, 0));
    	   imgFiltered.put("colorBlue", imgFilter.applyColorFilterEffect(src, 0, 0, 255));


    	   imgFiltered.put("decColorDepth64", imgFilter.applyDecreaseColorDepthEffect(src, 64));
    	   imgFiltered.put("decColorDepth32", imgFilter.applyDecreaseColorDepthEffect(src, 32));
    	   imgFiltered.put("contrast", imgFilter.applyContrastEffect(src, 70));
    	   imgFiltered.put("emboss", imgFilter.applyEmbossEffect(src));
    	   imgFiltered.put("engrave", imgFilter.applyEngraveEffect(src));



    	   imgFiltered.put("flea", imgFilter.applyFleaEffect(src));
    	   imgFiltered.put("gaussianBlur", imgFilter.applyGaussianBlurEffect(src));
    	   imgFiltered.put("gamma", imgFilter.applyGammaEffect(src, 1.8, 1.8, 1.8));

    	   imgFiltered.put("greyscale", imgFilter.applyGreyscaleEffect(src));
    	   imgFiltered.put("hue", imgFilter.applyHueFilter(src, 2));
    	   imgFiltered.put("invert", imgFilter.applyInvertEffect(src));



    	   imgFiltered.put("meanremoval", imgFilter.applyMeanRemovalEffect(src));
    	   imgFiltered.put("roundCorner", imgFilter.applyRoundCornerEffect(src, 45));
    	   imgFiltered.put("saturation", imgFilter.applySaturationFilter(src, 1));


    	   imgFiltered.put("sepia", imgFilter.applySepiaToningEffect(src, 10, 1.5, 0.6, 0.12));
    	   imgFiltered.put("sepiaGreen", imgFilter.applySepiaToningEffect(src, 10, 0.88, 2.45, 1.43));
    	   imgFiltered.put("sepiaBlue", imgFilter.applySepiaToningEffect(src, 10, 1.2, 0.87, 2.1));


    	   imgFiltered.put("smooth", imgFilter.applySmoothEffect(src, 100));
    	   imgFiltered.put("shadingCyan", imgFilter.applyShadingFilter(src, Color.CYAN));
    	   imgFiltered.put("shadingYellow", imgFilter.applyShadingFilter(src, Color.YELLOW));
    	   imgFiltered.put("shadingGreen", imgFilter.applyShadingFilter(src, Color.GREEN));*/


       /* if(!imgFiltered.containsKey("gaussianBlur"))imgFiltered.put("gaussianBlur", imgFilter.applyGaussianBlurEffect(src));
        insertarEnFiltros("Gaussian B",imgFiltered.get("gaussianBlur"),6);

        if(!imgFiltered.containsKey("sepia"))imgFiltered.put("sepia", imgFilter.applySepiaToningEffect(src, 10, 1.5, 0.6, 0.12));
        insertarEnFiltros("Sepia",imgFiltered.get("sepia"),5);

        if(!imgFiltered.containsKey("blackFilter"))imgFiltered.put("blackFilter", imgFilter.applyBlackFilter(src));
        insertarEnFiltros("Granulado",imgFiltered.get("blackFilter"),4);

        if(!imgFiltered.containsKey("greyscale"))imgFiltered.put("greyscale", imgFilter.applyGreyscaleEffect(src));
        insertarEnFiltros("Grises",imgFiltered.get("greyscale"),1);

        if(!imgFiltered.containsKey("tint"))imgFiltered.put("tint", imgFilter.applyTintEffect(src, 100));
        insertarEnFiltros("Tinta",imgFiltered.get("tint"),2);

        if(!imgFiltered.containsKey("waterMark")) imgFiltered.put("waterMark", imgFilter.applyWaterMarkEffect(src, "IDI-Gram", 200, 200, Color.GREEN, 80, 24, false));
        insertarEnFiltros("WaterMark",imgFiltered.get("waterMark"),3);*/
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
                t1.setBackgroundColor(Color.RED);
                t2.setBackgroundColor(Color.BLUE);
                t3.setBackgroundColor(Color.BLUE);
                ll.removeAllViews();
                vv = View.inflate(this, R.layout.filtros, null);
                ll.addView(vv, new LinearLayout.LayoutParams(ll.getLayoutParams().width, ll.getLayoutParams().height));
                llenarMuestras();

                break;
            case R.id.tab2:
                t1.setBackgroundColor(Color.BLUE);
                t2.setBackgroundColor(Color.RED);
                t3.setBackgroundColor(Color.BLUE);
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
                        contraste=progressChanged-100;
                        camibiarContrBrillo();
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
                        brillo=progressChanged-100;
                        camibiarContrBrillo();

                    }
                });

                break;
            case R.id.tab3:
                t1.setBackgroundColor(Color.BLUE);
                t2.setBackgroundColor(Color.BLUE);
                t3.setBackgroundColor(Color.RED);
                ll.removeAllViews();
                vv = View.inflate(this, R.layout.guardar, null);
                ll.addView(vv, new LinearLayout.LayoutParams(ll.getLayoutParams().width, ll.getLayoutParams().height));



                break;
        }
    }

    private void camibiarContrBrillo()
    {

        ImageFilters imgFilter = new ImageFilters();
        Bitmap bm = imgFilter.applyBrightnessEffect(showing, brillo);
        bm = imgFilter.applyContrastEffect(bm, contraste);
        imgMain.setImageBitmap(bm);
        contrasteBrillo=bm;

    }
    public void tabClicked(View v){


        tabSelected(v.getId());


    }

    public void buttonClicked(View v){

        //Toast.makeText(this, "Procesando...", Toast.LENGTH_SHORT).show();
        ImageFilters imgFilter = new ImageFilters();
        int id=v.getId();
        contraste=1;
        brillo=1;

        System.out.println(id);
        if(id!=R.id.btn_pick_img && id!=R.id.btn_apply_img )// querra decir que hemos clickado a un filtro
        {
            contrasteBrillo=null;

        }
        if(id == R.id.btn_pick_img){
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);

        }
        else if(id == R.id.btn_apply_img){
            if(contrasteBrillo!=null){
                src=contrasteBrillo;
                showing=src;
                contrasteBrillo=null;}
            else src=showing;
            imgFiltered.clear();
            if(findViewById(R.id.espaciofiltros)!=null)llenarMuestras();
            else if(findViewById(R.id.espacioajustes)!=null) {
                SeekBar sb1 = (SeekBar) findViewById(R.id.seekBar1);
                sb1.setProgress(100);
                SeekBar sb2 = (SeekBar) findViewById(R.id.seekBar2);
                sb2.setProgress(100);
            }
        }


        else if(id==0) //Normal
        {
         imgMain.setImageBitmap(src);
         showing=src;

        }
        else if(id==1) //tinta azul
        {
            Bitmap img = imgFiltered.get("tint");
            if (img != null) {
                imgMain.setImageBitmap(img);
                showing=img;
            }


        }
        else if(id==2) //Gaussian blur
        {
            Bitmap img = imgFiltered.get("gauss");
            if (img != null) {
                imgMain.setImageBitmap(img);
                showing=img;
            }

        }
        else if(id==3) //inversa
        {
            Bitmap img = imgFiltered.get("inv");
            if (img != null) {
                imgMain.setImageBitmap(img);
                showing=img;
            }
        }
        else if(id==4) //Grises
        {
            Bitmap img = imgFiltered.get("grey");
            if (img != null) {
                imgMain.setImageBitmap(img);
                showing=img;
            }

        }
        else if(id==5) //Sepia
        {
            Bitmap img = imgFiltered.get("sepia");
            if (img != null) {
                imgMain.setImageBitmap(img);
                showing=img;
            }

        }
        else if(id==6) //Gaussian Blur
        {
            Bitmap img = imgFiltered.get("gaussianBlur");
            if (img != null) {
                imgMain.setImageBitmap(img);
                showing=img;
            }

        }
        else if(id==7) //escala de grises
        {
            Bitmap img = imgFiltered.get("greyscale");
            if (img != null) {
                imgMain.setImageBitmap(img);
                showing=img;
            }

        }
        else if(id==8) //escala de grises
        {
            Bitmap img = imgFiltered.get("greyscale");
            if (img != null) {
                imgMain.setImageBitmap(img);
                showing=img;
            }

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



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    Bitmap bmp = decodeUri(selectedImage);
                    if(bmp !=null){
                        src = bmp;
                        imgMain.setImageBitmap(src);
                        imgFiltered.clear();
                        showing=src;
                        Toast.makeText(this, "Procesando vista previa...", Toast.LENGTH_SHORT).show();
                        contraste=1;
                        brillo=1;
                        if(findViewById(R.id.espaciofiltros)!=null)llenarMuestras();
                    }
                }
        }
    }

    public void guardarImagen(View v)
    {
       EditText et= (EditText)findViewById(R.id.editText);

       if(!src.equals(showing) || contrasteBrillo!=null)
       {
           Toast.makeText(this, "Hay cambios sin aplicar", Toast.LENGTH_SHORT).show();

       }
       else
       {
           try {

               File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/IDIGram/" + et.getText().toString()+".png");
               FileOutputStream fos = new FileOutputStream(f);
               src.compress(Bitmap.CompressFormat.PNG,90,fos);
               Toast.makeText(this, "Guardado correctamente en " +f.getAbsolutePath() , Toast.LENGTH_SHORT).show();
           }
           catch(Exception ex){
               ex.printStackTrace();
               Toast.makeText(this, "Ha ocurrido un problema guardando", Toast.LENGTH_SHORT).show();
           }

       }

    }

    private Bitmap decodeUri(Uri selectedImage)  {

        try {

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE = 400;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE) {
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
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private class Pair {
        String tipo;
        String label;
        Bitmap bm;
        int id;

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

        public Pair(String label, String tipo, Bitmap bm,int id) {
            super();
            this.label = label;
            this.bm = bm;
            this.tipo = tipo;
            this.id=id;
        }

    }

    private class ParaTask extends AsyncTask<Params, Void, Pair> {
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
            Bitmap result = null;
            if (imgFiltered.containsKey(tipo))
                return new Pair(label, tipo, null,id);
            switch (tipo) {

                case "tint":
                    result = imgFilter.applyTintEffect(bm, valor);
                    break;
                case "gauss":
                    result = imgFilter.applyGaussianBlurEffect(bm);
                    break;
                case "inv":
                    result = imgFilter.applyInvertEffect(bm);
                    break;
                case "grey":
                    result = imgFilter.applyGreyscaleEffect(bm);
                    break;

                default:
                    return null;

            }
            imgFiltered.put(tipo, result);

            return new Pair(label, tipo, result,id);
        }

        protected void onPostExecute(Pair result) {
            // Pass the result data back to the main activity
            if (result != null)
                insertarEnFiltros(result.getLabel(),
                        imgFiltered.get(result.getTipo()),result.getId());

        }

    }
}
