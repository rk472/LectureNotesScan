package com.scanner.demo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class SingleMainActivity extends AppCompatActivity{
    private RecyclerView recyclerView;
    private static final int REQUEST_CODE = 99;
    FloatingActionButton cameraButton;
    private ProgressDialog pd;
    private ImageAdapter imageAdapter;
     List<Bitmap> l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_single);
        init();
    }

    private void init() {
        cameraButton = findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(new ScanButtonClickListener(ScanConstants.OPEN_CAMERA));
        recyclerView=(RecyclerView)findViewById(R.id.image_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2,GridLayout.VERTICAL,false));
        l=new ArrayList<>();
        imageAdapter=new ImageAdapter(this,l);
        recyclerView.setAdapter(imageAdapter);
        inst=this;
    }

    public void finishExport() {
        Document document = new Document(PageSize.A4, 10, 10, 10, 10);
        String directoryPath = android.os.Environment.getExternalStorageDirectory().toString();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(directoryPath + "/example.pdf"));
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
        document.open();
        try {
            for(int i=0;i<l.size();i++){
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                l.get(i).compress(Bitmap.CompressFormat.PNG, 20, stream);
                byte[] byteArray = stream.toByteArray();
                Image image = Image.getInstance(byteArray);
                Float width,height;
                if(l.get(i).getWidth()>l.get(i).getHeight()) {
                     width = document.getPageSize().getWidth();
                     height = width * l.get(i).getHeight() / l.get(i).getWidth();
                }else{
                    width=document.getPageSize().getWidth()-20f;
                    height=document.getPageSize().getHeight()-20f;
                }
                    Rectangle rectangle = new Rectangle(width, height);
                    image.setAlignment(Element.ALIGN_CENTER);
                    image.scaleAbsolute(rectangle);
                    document.add(image);
            }
            new UploadFileAsync().execute("");
        }catch (BadElementException | IOException e) {
            e.printStackTrace();
        }catch (DocumentException e) {
            e.printStackTrace();
        }finally {
            document.close();
            //pd.dismiss();
        }
    }
    private class UploadFileAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String sourceFileUri = Environment.getExternalStorageDirectory().getAbsolutePath()+"/example.pdf";
                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;
                File sourceFile = new File(sourceFileUri);
                if (sourceFile.isFile()) {
                    try {
                        String upLoadServerUri = "http://192.168.43.113/uploadPdf.php?";
                        // open a URL connection to the Servlet
                        FileInputStream fileInputStream = new FileInputStream(
                                sourceFile);
                        URL url = new URL(upLoadServerUri);
                        // Open a HTTP connection to the URL
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true); // Allow Inputs
                        conn.setDoOutput(true); // Allow Outputs
                        conn.setUseCaches(false); // Don't use a Cached Copy
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Connection", "Keep-Alive");
                        conn.setRequestProperty("ENCTYPE",
                                "multipart/form-data");
                        conn.setRequestProperty("Content-Type",
                                "multipart/form-data;boundary=" + boundary);
                        conn.setRequestProperty("bill", sourceFileUri);
                        dos = new DataOutputStream(conn.getOutputStream());
                        dos.writeBytes(twoHyphens + boundary + lineEnd);
                        dos.writeBytes("Content-Disposition: form-data; name=\"bill\";filename=\""
                                + sourceFileUri + "\"" + lineEnd);
                        dos.writeBytes(lineEnd);
                        // create a buffer of maximum size
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        buffer = new byte[bufferSize];
                        // read file and write it into form...
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                        while (bytesRead > 0) {
                            dos.write(buffer, 0, bufferSize);
                            bytesAvailable = fileInputStream.available();
                            bufferSize = Math
                                    .min(bytesAvailable, maxBufferSize);
                            bytesRead = fileInputStream.read(buffer, 0,
                                    bufferSize);
                        }
                        // send multipart form data necesssary after file
                        // data...
                        dos.writeBytes(lineEnd);
                        dos.writeBytes(twoHyphens + boundary + twoHyphens
                                + lineEnd);
                        // Responses from the server (code and message)
                        int serverResponseCode = conn.getResponseCode();
                        String serverResponseMessage = conn
                                .getResponseMessage();
                        if (serverResponseCode == 200) {
                            StringBuilder sb = new StringBuilder();
                            try{
                                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                String line ;
                                while((line=rd.readLine()) != null){
                                    sb.append(line);
                                }
                                rd.close();
                            }catch (IOException ee){
                                return ee.getMessage();
                            }
                            return sb.toString();
                        }
                        fileInputStream.close();
                        dos.flush();
                        dos.close();
                        return serverResponseMessage;
                    } catch (Exception e) {
                        return  e.getMessage();
                    }
                }
            } catch (Exception ex) {
                return  ex.getMessage();
            }
            return "false";
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            Toast.makeText(inst, s, Toast.LENGTH_SHORT).show();
            if(s.equals("file uploaded successfully")){
                l=new ArrayList<>();
                imageAdapter.notifyDataSetChanged();
            }
        }
    }
    private class ScanButtonClickListener implements View.OnClickListener {

        private int preference;

        public ScanButtonClickListener(int preference) {
            this.preference = preference;
        }

        public ScanButtonClickListener() {
        }

        @Override
        public void onClick(View v) {
            startScan(preference);
        }
    }

    protected void startScan(int preference) {
        Intent intent = new Intent(this, ScanActivity.class);
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
        startActivityForResult(intent, REQUEST_CODE);
    }
    public static SingleMainActivity inst;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                getContentResolver().delete(uri, null, null);
                l.add(bitmap);
                imageAdapter.notifyDataSetChanged();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap convertByteArrayToBitmap(byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        pd = new ProgressDialog(this);
        pd.setTitle("Please wait");
        pd.setMessage("Exporting images to PDF.");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            if(l.size()!=0) {
                finishExport();
                return true;
            }else {
                Toast.makeText(inst, "No files to upload", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
