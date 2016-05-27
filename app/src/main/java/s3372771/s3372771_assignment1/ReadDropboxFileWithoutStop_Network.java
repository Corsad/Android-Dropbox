package s3372771.s3372771_assignment1;

import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;

/**
 * Created by s3372771
 */
public class ReadDropboxFileWithoutStop_Network extends AsyncTask<Void, Void, Void> {

    private ViewDropboxActivity currentActivity;
    private String currentPath;
    DropboxAPI.Entry existingEntry;

    public ReadDropboxFileWithoutStop_Network(ViewDropboxActivity currentActivity, String currentPath) {
        this.currentActivity = currentActivity;
        this.currentPath = currentPath;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        currentActivity.notifyEntriesChanged(existingEntry.contents);
    }

    @Override
    protected Void doInBackground(Void... params) {
        existingEntry = null;
        try {
            existingEntry = MainActivity.mDBApi.metadata(currentPath, 100, null, true, null);
        } catch (DropboxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
