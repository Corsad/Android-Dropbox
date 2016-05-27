package s3372771.s3372771_assignment1;

import android.os.AsyncTask;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;

import java.util.concurrent.ExecutionException;

/**
 * Created by Tan on 04/02/15.
 */
public class RenameDropboxItem_Network extends AsyncTask<Void, Void, Void> {

    private ViewDropboxActivity currentActivity;
    private DropboxAPI.Entry Entry;
    private String newName;

    public RenameDropboxItem_Network(ViewDropboxActivity currentActivity, DropboxAPI.Entry Entry, String newName) {
        this.Entry = Entry;
        this.newName = newName;
        this.currentActivity = currentActivity;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            MainActivity.mDBApi.move(Entry.path, Entry.parentPath() + newName);
        } catch (DropboxException e) {
            e.printStackTrace();
        }

        return null;
    }
}
