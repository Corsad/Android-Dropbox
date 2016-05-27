package s3372771.s3372771_assignment1;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tan on 04/03/15.
 */
public class FileAndFolderChooser extends ActionBarActivity {

    private FileAndFolderChooser currentView = this;

    private Intent currentIntent;
    private String fileChooserType;
    private String currentPath;

    private ImageButton acceptBtn;
    private ImageButton deniedBtn;
    private TextView header;

    private ListView listView;
    private SDCardAdapter ad;

    private List<File> files = new ArrayList<File>();
    private File[] temp = null;

    static final int CREATE_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dropbox_listview);

        // Get type of file chooser. File or Folder
        currentIntent = getIntent();
        fileChooserType = currentIntent.getStringExtra("fileChooserType");
        currentPath = currentIntent.getStringExtra("sdCardPath");

        // Get Accept Button
        acceptBtn = (ImageButton) findViewById(R.id.listview_add_accept_file_button);
        acceptBtn.setImageResource(R.drawable.accept_icon);

        // Get Denied Button
        deniedBtn = (ImageButton) findViewById(R.id.listview_option_deny_button);
        deniedBtn.setImageResource(R.drawable.denied_icon);

        // Get header
        header = (TextView) findViewById(R.id.list_header_text);

        // Get file from sdcard
//        File sdcard = Environment.getExternalStorageDirectory();
//        File dirs = new File(sdcard.getAbsolutePath());

        File sdcard = Environment.getExternalStorageDirectory();
        File dirs = new File(currentPath);

        // Get all file from dir
        getfile(dirs);

        // Set view and adapter
        listView = (ListView) findViewById(R.id.item_listview);
        ad = new SDCardAdapter(files, this);
        listView.setAdapter(ad);

        // Set Listener depend on fileChooserType
        if (fileChooserType.equalsIgnoreCase("file"))
            setFileListener();
        else if (fileChooserType.equalsIgnoreCase("folder"))
            setFolderListener();

    }

    public void setFolderListener() {
        header.setText("Selecting " + currentPath);
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openFolder(position);
            }
        });
    }

    public void setFileListener() {
        header.setText(R.string.header_folder_selection);
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                header.setText("You are selecting " + files.get(position).getName());
                currentPath = files.get(position).getPath();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                openFolder(position);
                return true;
            }
        });
    }

    public void openFolder(int position) {
        Intent intent = new Intent(this, FileAndFolderChooser.class);
        intent.putExtra("sdCardPath", files.get(position).getPath());
        intent.putExtra("fileChooserType", fileChooserType);
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

    public List<File> getfile(File dir) {
        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {
                if (fileChooserType.equalsIgnoreCase("file"))
                    files.add(listFile[i]);
                else if (fileChooserType.equalsIgnoreCase("folder")) {
                    if (listFile[i].isDirectory())
                        files.add(listFile[i]);
                }
            }
        }
        return files;
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
}
