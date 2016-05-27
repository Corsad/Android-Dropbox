package s3372771.s3372771_assignment1;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tan on 04/03/15.
 */
public class DownloadDropboxItem_Network extends AsyncTask<Void, Void, Void> {

    private DropboxAPI.Entry entry;
    //    private List<DropboxAPI.Entry> templist = new ArrayList<DropboxAPI.Entry>();
    private String downloadPath;

    public DownloadDropboxItem_Network(DropboxAPI.Entry entry, String downloadPath) {
        this.entry = entry;
        this.downloadPath = downloadPath;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.i("DbExampleLog", "Done Download");
    }


    @Override
    protected Void doInBackground(Void... params) {
        recursiveCheck(entry, downloadPath);
        return null;
    }

    public void recursiveCheck(DropboxAPI.Entry entry, String downloadPath) {
        if (entry.isDir) {
            List<DropboxAPI.Entry> templist = new ArrayList<DropboxAPI.Entry>();
            templist = getFilesInsideDir(entry.path);
            for (DropboxAPI.Entry i : templist) {
                recursiveCheck(i, downloadPath + File.separator + entry.fileName());
            }
        } else {
            if (!entry.isDir) {
                downloadFile(entry, downloadPath);
            }
        }

    }

    public void downloadFile(DropboxAPI.Entry entry, String downloadPath) {
        File file = new File(downloadPath);
        if (!file.exists()) {
            file.mkdirs();
        }

        File outputFile = new File(file, entry.fileName());

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(outputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            DropboxAPI.DropboxFileInfo info = MainActivity.mDBApi.getFile(entry.path, null, outputStream, null);
        } catch (DropboxException e) {
            e.printStackTrace();
        }
    }

    public List<DropboxAPI.Entry> getFilesInsideDir(String currentPath) {
        DropboxAPI.Entry existingEntry = null;
        try {
            existingEntry = MainActivity.mDBApi.metadata(currentPath, 100, null, true, null);
        } catch (DropboxException e) {
            e.printStackTrace();
        }
        return existingEntry.contents;
    }
}
