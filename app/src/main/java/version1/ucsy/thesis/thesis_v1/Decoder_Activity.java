package version1.ucsy.thesis.thesis_v1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.net.URISyntaxException;

import static android.content.ContentValues.TAG;

/**
 * Created by Naylay on 12/10/2017.
 */

public class Decoder_Activity extends Activity {
    public static final int FILE_SELECT_CODE=0;
    Bitmap bMap=null;
    EditText outputmessage;
    ImageView loadedimage;
    String filepath=" ";
    int inputimgwidth=0 , inputimgheight=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.decode_imgae);
        outputmessage =(EditText) findViewById(R.id.outputmessage);
        loadedimage =(ImageView)findViewById(R.id.inputimagedec);
    }


    public void browseDecImage(View view)
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

                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loadImage() {


        bMap = BitmapFactory.decodeFile(filepath);

        // bMap.getWidth()
        Toast.makeText(getApplicationContext(),"This is file size width"+getWindowManager().getDefaultDisplay().getWidth()+" height "
                +getWindowManager().getDefaultDisplay().getHeight()+"image width "+bMap.getWidth()+" height "+bMap.getHeight(),Toast.LENGTH_LONG).show();
        //  inputimg.setBackgroundColor(Color.RED);
        int allowwidth=getWindowManager().getDefaultDisplay().getWidth();
        int allowheith=getWindowManager().getDefaultDisplay().getHeight()/3;
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



        loadedimage.setImageBitmap(Bitmap.createScaledBitmap(bMap, inputimgwidth, inputimgheight, false));



        //  inputimg.setImageURI(Uri.fromFile(new File(filepath)));
    }


    public void decodeFunction(View view)
    {
        ImageToText  imagetotextdecoder=new ImageToText();
         Bitmap embb=bMap.copy(Bitmap.Config.ARGB_8888, true);
        String output_stirng=imagetotextdecoder.decodeMessage(embb,0,0);
        outputmessage.setText(output_stirng);
    }

}


