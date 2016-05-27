package s3372771.s3372771_assignment1;

import android.os.AsyncTask;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;

/**
 * Created by Tan on 04/04/15.
 */
public class ReadDropboxFileForMoveView extends AsyncTask<Void, Void, Void> {

    private MoveDropboxView currentActivity;
    private String currentPath;
    DropboxAPI.Entry existingEntry;

    public ReadDropboxFileForMoveView(MoveDropboxView currentActivity, String currentPath) {
        this.currentActivity = currentActivity;
        this.currentPath = currentPath;
    }

    @Override
    protected Void doInBackground(Void... params) {
        existingEntry = null;
        try {
            existingEntry = MainActivity.mDBApi.metadata(currentPath, 100, null, true, null);
        } catch (DropboxException e) {
            e.printStackTrace();
        }

        currentActivity.setEntries(existingEntry.contents);

        return null;
    }
}
