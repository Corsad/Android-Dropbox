package s3372771.s3372771_assignment1;

import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;

/**
 * Created by Tan on 04/02/15.
 */
public class DeltaDropbox_Network extends AsyncTask<Void, Void, Void> {

    private DropboxAPI.DeltaPage deltaInfo;
    private ViewDropboxActivity currentActivity;
    private String cursor = "";

    public DeltaDropbox_Network(ViewDropboxActivity currentActivity) {
        this.currentActivity = currentActivity;
    }

    @Override
    protected Void doInBackground(Void... params) {
        do {
            try {
                deltaInfo = MainActivity.mDBApi.delta(cursor);
            } catch (DropboxException e) {
                e.printStackTrace();
            }
            cursor = deltaInfo.cursor;

        } while (deltaInfo.hasMore);

        return null;
    }
}
