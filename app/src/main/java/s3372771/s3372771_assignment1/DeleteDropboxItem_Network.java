package s3372771.s3372771_assignment1;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;

/**
 * Created by Tan on 04/02/15.
 */
public class DeleteDropboxItem_Network extends AsyncTask<Void, Void, Void> {

    private DropboxAPI.Entry Entry;

    public DeleteDropboxItem_Network(DropboxAPI.Entry Entry) {
        this.Entry = Entry;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            MainActivity.mDBApi.delete(Entry.path);
        } catch (DropboxException e) {
            e.printStackTrace();
        }

        return null;
    }
}
