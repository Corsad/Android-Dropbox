package s3372771.s3372771_assignment1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by s3372771
 */

public class MainActivity extends ActionBarActivity {

    private static final String APP_KEY = "4pllkaqxbs1bi87";
    private static final String APP_SECRET = "n8wx8on1whiop7v";

    final static private String ACCOUNT_PREFS_NAME = "prefs";
    final static private String ACCESS_KEY_NAME = "ACCESS_KEY";
    final static private String ACCESS_SECRET_NAME = "ACCESS_SECRET";

    public static DropboxAPI<AndroidAuthSession> mDBApi;

    private Button linkButton;
    private Button folderButton;
    private Boolean hasLoggedIn = false;

    private Intent intent = null;
    private Boolean clickUnlink = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);

        folderButton = (Button) findViewById(R.id.folderBtn);
        linkButton = (Button) findViewById(R.id.loginBtn);

        // If user press unlink
        intent = getIntent();
        if (intent.getBooleanExtra("unlink", clickUnlink)) {
            logOut();
        }

        //

        linkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasLoggedIn == false) {
                    mDBApi.getSession().startOAuth2Authentication(MainActivity.this);
                } else {
                    logOut();
                }
            }
        });

        folderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openViewDropbox(v);
            }
        });
    }

    protected void onResume() {
        super.onResume();

        if (mDBApi.getSession().authenticationSuccessful()) {
            try {
                // Required to complete auth, sets the access token on the session
                mDBApi.getSession().finishAuthentication();

                String accessToken = mDBApi.getSession().getOAuth2AccessToken();
                onLoggedIn();

                //new TestUpload(mDBApi).execute();
                //new ReadDropboxFileNetwork(mDBApi).execute();
            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            }
        }
    }

    public void onLoggedIn() {
        linkButton.setText(R.string.unLinkBtnTxt);
        hasLoggedIn = true;
        folderButton.setVisibility(View.VISIBLE);
    }

    public void logOut() {
        linkButton.setText(R.string.linkingBtnTxt);
        mDBApi.getSession().unlink();
        hasLoggedIn = false;
        folderButton.setVisibility(View.GONE);
    }

    public void openViewDropbox(View view) {
        String currentPath = "/";
        String newView = readViewTypefile();
        Intent intent = new Intent(this, ViewDropboxActivity.class);
        intent.putExtra("currentPath", currentPath);
        intent.putExtra("viewType", newView);
        startActivity(intent);
    }

    public String readViewTypefile() {
        String viewType = "";

        try {
            InputStream inputStream = openFileInput("viewConfig.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedString = new BufferedReader(inputStreamReader);

                String readString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((readString = bufferedString.readLine()) != null) {
                    stringBuilder.append(readString);
                }

                inputStream.close();
                viewType = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            return "detail";
        } catch (IOException e) {
            e.printStackTrace();
        }

        return viewType;

    }
}
