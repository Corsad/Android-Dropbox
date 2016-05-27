package s3372771.s3372771_assignment1;

import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxUnlinkedException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Tan on 04/03/15.
 */
public class UploadDropboxItem_Network extends AsyncTask<Void, Void, Void> {
    private ViewDropboxActivity currentActivity;
    private String filePath;

    public UploadDropboxItem_Network(ViewDropboxActivity currentActivity, String filePath) {
        this.filePath = filePath;
        this.currentActivity = currentActivity;
    }

    @Override
    protected Void doInBackground(Void... params) {
        File file = new File(filePath);
        recursiveCheck(file, currentActivity.getCurrentPath(), file.getName());

        return null;
    }

    public void recursiveCheck(File file, String folderPath, String fileName) {
        if (file.isDirectory()) {
            for (File i : file.listFiles()) {
                recursiveCheck(i, folderPath + File.separator + fileName, i.getName());
            }
        } else {
            try {
                MainActivity.mDBApi.createFolder(folderPath);
            } catch (DropboxException e) {
                e.printStackTrace();
            }
            uploadFile(file, folderPath + File.separator + fileName);
        }

    }

    public void uploadFile(File file, String path) {
        FileInputStream inputStream = null;
        DropboxAPI.Entry newEntry = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            newEntry = MainActivity.mDBApi.putFile(path, inputStream, file.length(), null, null);
        } catch (DropboxException e) {
            e.printStackTrace();
        }
    }
}
