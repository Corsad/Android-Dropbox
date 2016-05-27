package s3372771.s3372771_assignment1;

import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;

import java.io.File;

/**
 * Created by Tan on 04/04/15.
 */
public class MoveDropboxItem_Network extends AsyncTask<Void, Void, Void> {

    private DropboxAPI.Entry Entry;
    private String destinationPath;

    public MoveDropboxItem_Network(DropboxAPI.Entry Entry, String destinationPath) {
        this.Entry = Entry;
        this.destinationPath = destinationPath;
    }

    @Override
    protected Void doInBackground(Void... params) {
        if(!Entry.parentPath().equalsIgnoreCase(destinationPath)) {
            try {
                MainActivity.mDBApi.move(Entry.path, destinationPath + File.separator + Entry.fileName());
            } catch (DropboxException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}