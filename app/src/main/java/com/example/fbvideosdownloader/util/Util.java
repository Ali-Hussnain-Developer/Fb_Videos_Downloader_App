package com.example.fbvideosdownloader.util;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.example.fbvideosdownloader.R;

public class Util {
    public static String RootDirectoryFacebook = "/Download/FacebookVideo/";

    public static void download(String downloadPath, String destinationPath, Context context, String fileName) {
        Toast.makeText(context, R.string.txt_download_start, Toast.LENGTH_LONG).show();
        Uri uri = Uri.parse(downloadPath);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle(fileName);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, destinationPath + fileName);
        ((DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request);
    }
}
