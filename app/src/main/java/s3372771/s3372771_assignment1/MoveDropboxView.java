package s3372771.s3372771_assignment1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.dropbox.client2.DropboxAPI;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Tan on 04/04/15.
 */


// Could have make a giant If using ViewDropboxActivity but then it will be too hard to review

public class MoveDropboxView extends ActionBarActivity {


    private Intent currentIntent;
    private String currentPath;

    private ImageButton acceptBtn;
    private ImageButton deniedBtn;
    private TextView header;

    private ListView listView;
    private DropboxItemAdapter ad;

    private List<DropboxAPI.Entry> Entries = null;

    static final int CREATE_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dropbox_listview);

        // Get type of file chooser. File or Folder
        currentIntent = getIntent();
        currentPath = currentIntent.getStringExtra("currentPath");

        // Get Accept Button
        acceptBtn = (ImageButton) findViewById(R.id.listview_add_accept_file_button);
        acceptBtn.setImageResource(R.drawable.accept_icon);

        // Get Denied Button
        deniedBtn = (ImageButton) findViewById(R.id.listview_option_deny_button);
        deniedBtn.setImageResource(R.drawable.denied_icon);

        // Get header
        header = (TextView) findViewById(R.id.list_header_text);
        header.setText("Selecting " + currentPath);

        ReadDropboxFileForMoveView task = new ReadDropboxFileForMoveView(this, currentPath);

        try {
            Void str_result = task.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        listView = (ListView) findViewById(R.id.item_listview);
        ad = new DropboxItemAdapter(Entries, this, "detail");
        listView.setAdapter(ad);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Entries.get(position).isDir) {
                    openFolder(position);
                }
            }
        });

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeSelectedPath();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        deniedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }

    public void openFolder(int position) {
        Intent intent = new Intent(this, MoveDropboxView.class);
        intent.putExtra("currentPath", Entries.get(position).path);
        startActivityForResult(intent, CREATE_REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        }
    }

    public void writeSelectedPath() {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("selectFileConfig.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(currentPath);
            outputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setEntries(List<DropboxAPI.Entry> entries) {
        Entries = entries;
    }


}
