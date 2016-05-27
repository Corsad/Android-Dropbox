package s3372771.s3372771_assignment1;

import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;

/**
 * Created by s3372771
 */
public class ReadDropboxFile_Network extends AsyncTask<Void, Void, Void> {

    private ViewDropboxActivity currentActivity;
    private String currentPath;
    DropboxAPI.Entry existingEntry;

    public ReadDropboxFile_Network(ViewDropboxActivity currentActivity, String currentPath) {
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

//        currentActivity.notifyNewEntries();

//        currentActivity.notifyNewEntries(existingEntry.contents);
//        Test Entry after getting content
//
//        for(DropboxAPI.Entry i : existingEntry.contents){
//            Log.i("DbExampleLog", "Network existingEntry.fileName: " + i.fileName());
//        }

        return null;
    }
}
