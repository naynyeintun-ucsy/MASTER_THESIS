package version1.ucsy.thesis.thesis_v1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URISyntaxException;
import java.util.Random;


import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {
    public static final int FILE_SELECT_CODE=0;
    public String filepath="";
    ImageView inputimg,outputimg;
    int inputimgwidth;
    int inputimgheight;
    EditText message;
    Bitmap bMap;
   Bitmap sourceImage = null, embeddedImage = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputimg=(ImageView) findViewById(R.id.inputimg);
        outputimg=(ImageView)findViewById(R.id.outputimg);

        message=(EditText)findViewById(R.id.inputmessage);
    }

    public void browsing(View v)
    {
        Toast.makeText(getApplicationContext(),"on click message",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d(TAG, "File Uri: " + uri.toString());
                    // Get the path
                    String path = null;
                    try {
                       filepath=  FileUtils.getPath(this, uri);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "File Path: " + filepath);
                    if(filepath.length()!=0)loadImage();


                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
   public void embbedingProcess(View view)
   {

       String mess = message.getText().toString();
       embeddedImage = bMap.copy(Bitmap.Config.ARGB_8888, true);
       TextToImage textoimagembedder=new TextToImage();
       int messageLength = mess.length();

       int imageWidth = embeddedImage.getWidth(), imageHeight = embeddedImage.getHeight(), imageSize = imageWidth
               * imageHeight;
       if (messageLength * 8 + 32 > imageSize) {
           Toast.makeText(getApplicationContext(),
                   "Message is too long for the chosen image"+
                           "Message too long!", Toast.LENGTH_SHORT).show();
           return;
       }
        embeddedImage=textoimagembedder.embedMessage(embeddedImage,mess);
        outputimg.setImageBitmap(Bitmap.createScaledBitmap(embeddedImage, inputimgwidth, inputimgheight, false));

   }

   /* private void embedMessage(Bitmap img, String mess) {
        int messageLength = mess.length();

        int imageWidth = img.getWidth(), imageHeight = img.getHeight(), imageSize = imageWidth
                * imageHeight;

        embedInteger(img, messageLength, 0, 0);

        byte b[] = mess.getBytes();
        for (int i = 0; i < b.length; i++)
            embedByte(img, b[i], i * 8 + 32, 0);
    }*/




    ////get bit value



    /// embedbyte
/*
    private void embedByte(Bitmap img, byte b, int start, int storageBit) {

    int maxX = img.getWidth(), maxY = img.getHeight(), startX = start
            / maxY, startY = start - startX * maxY, count = 0;
		for (int i = startX; i < maxX && count < 8; i++) {
        for (int j = startY; j < maxY && count < 8; j++) {

            int rgb = img.getPixel(i, j), bit = getBitValue(b, count);

            rgb = setBitValue(rgb, storageBit, bit);


            img.setPixel(i, j, rgb);
            count++;
        }
    }
}
*/

///embedbyte

//get bit value
  /*  private int getBitValue(int n, int location) {
        int v = n & (int) Math.round(Math.pow(2, location));

        return v == 0 ? 0 : 1;
    }
*/
    ///get bit value


    ///set bit value

 /*   private int setBitValue(int n, int location, int bit) {
        int toggle = (int) Math.pow(2, location), bv = getBitValue(n, location);
        System.out.println("The value of Toggle int " + toggle);
        System.out.println("The value of getBitvalue in setbitvaue bv  "+ bv);

        if (bv == bit)
            return n;
        if (bv == 0 && bit == 1)
            n |= toggle;
        else if (bv == 1 && bit == 0)
            n ^= toggle;
        return n;
    }*/

    //set bit value
  /*  public static Bitmap getMutableBitmap(Resources resources, int resId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        return BitmapFactory.decodeResource(resources, resId, options);
    }*/

   public void saveFunction(View view)
   {

       toastCaller();
       SaveImage(embeddedImage);



   }

    private void toastCaller() {
        Toast.makeText(getApplicationContext(),"Saving",Toast.LENGTH_LONG).show();
    }

    public void resetFunction(View view)
    {

        Intent intentdecoder=new Intent(this,Decoder_Activity.class);
        startActivity(intentdecoder);
        this.finishAffinity();
      /*  ImageToText  imagetotextdecoder=new ImageToText();
        String output_stirng=imagetotextdecoder.decodeMessage(embeddedImage,0,0);
        message.setText("This is the output message   "+output_stirng);*/
    }






    private void SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();

        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
      //  Random generator = new Random();
       // int n = 10000;
      //  n = generator.nextInt(n);
        String fname = "Image-"+ 1000 +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            Toast.makeText(getApplicationContext(),"file saved in  " +root + " /saved_image/   ", Toast.LENGTH_SHORT ).show();
          //finalBitmap.compress(Bitmap.CompressFormat.PNG)
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadImage() {


            bMap = BitmapFactory.decodeFile(filepath);

           // bMap.getWidth()
        Toast.makeText(getApplicationContext(),"This is file size width"+getWindowManager().getDefaultDisplay().getWidth()+" height "
                +getWindowManager().getDefaultDisplay().getHeight()+"image width "+bMap.getWidth()+" height "+bMap.getHeight(),Toast.LENGTH_LONG).show();
            //  inputimg.setBackgroundColor(Color.RED);
             int allowwidth=getWindowManager().getDefaultDisplay().getWidth();
                int allowheith=getWindowManager().getDefaultDisplay().getHeight()/4;
               inputimgwidth=bMap.getWidth();
                inputimgheight=bMap.getHeight();
        int no_of_time=0;
                 if(inputimgheight>allowheith)
                 {
                     no_of_time=((1*inputimgheight)/allowheith);

                     inputimgheight=inputimgheight/no_of_time;
                     inputimgwidth=inputimgwidth/(no_of_time);
                     Log.i("outinfo", no_of_time +" "+inputimgwidth+" "+inputimgheight);
                   //  System.out.print(no_of_time +" "+inputimgwidth+" "+inputimgheight);

                 }



            inputimg.setImageBitmap(Bitmap.createScaledBitmap(bMap, inputimgwidth, inputimgheight, false));
            //  inputimg.setImageURI(Uri.fromFile(new File(filepath)));
        }

}

class FileUtils {

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

}