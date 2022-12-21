package com.example.fbvideosdownloader.ui.activities;

import android.Manifest;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fbvideosdownloader.R;
import com.example.fbvideosdownloader.util.Util;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button btnDownload;
    EditText edtURL;
    private MainActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        accessPermission();
        btnDownload = findViewById(R.id.btnDownload);
        edtURL = findViewById(R.id.edtFbUrl);
        activity = this;
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFbData();
            }
        });

    }

    private void getFbData() {
        URL url = null;
        try {
            url = new URL(edtURL.getText().toString());
            String host = url.getHost();
            if (host.contains(getString(R.string.txt_host_txt_one)) || host.contains(getString(R.string.txt_host_txt_two))) {
                new CallGetFbData().execute(edtURL.getText().toString());
            } else {
                Toast.makeText(this, getString(R.string.txt_invalid), Toast.LENGTH_LONG).show();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


    }

    private void accessPermission() {

        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (!report.areAllPermissionsGranted()) {
                            accessPermission();
                        } else {


                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();


    }

    class CallGetFbData extends AsyncTask<String, Void, Document> {
        Document fBDoc;

        @Override
        protected Document doInBackground(String... strings) {
            try {
                fBDoc = Jsoup.connect(strings[0]).get();
            } catch (Exception e) {

            }
            return fBDoc;
        }

        @Override
        protected void onPostExecute(Document document) {
            String videoUrl = document.select("meta[property=\"og:video\"]").last().attr(getString(R.string.txt_content));
            if (!videoUrl.equals(""))
                Util.download(videoUrl, Util.RootDirectoryFacebook, activity, getString(R.string.txt_facebook) + System.currentTimeMillis() + getString(R.string.txt_extension));

        }
    }
}