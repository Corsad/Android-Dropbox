package s3372771.s3372771_assignment1;

import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxUnlinkedException;

import java.io.ByteArrayInputStream;

/**
 * Created by s3372771
 */
public class TestUpload extends AsyncTask<Void, Void, Void> {
    private DropboxAPI<AndroidAuthSession> mDBApi;

    public TestUpload(DropboxAPI<AndroidAuthSession> mDBApi) {
        this.mDBApi = mDBApi;
    }

    @Override
    protected Void doInBackground(Void... params) {
        String fileContents = "Hello World!";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContents.getBytes());
        DropboxAPI.Entry newEntry = null;
        try {
            newEntry = mDBApi.putFile("/sad/testing_123456.txt", inputStream, fileContents.length(), null, null);
        } catch (DropboxUnlinkedException e) {
            Log.e("DbExampleLog", "User has unlinked.");
        } catch (DropboxException e) {
            Log.e("DbExampleLog", "Something went wrong while uploading.");
        }

        return null;
    }
}
