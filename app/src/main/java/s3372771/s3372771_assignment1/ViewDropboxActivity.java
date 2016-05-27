package s3372771.s3372771_assignment1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.dropbox.client2.DropboxAPI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by s3372771
 */
public class ViewDropboxActivity extends ActionBarActivity {

    private ViewDropboxActivity currentActivity = this;

    private List<DropboxAPI.Entry> Entries = null;

    private String currentView;
    private ListView listView;
    private GridView gridView;
    private DropboxItemAdapter ad;

    private ImageButton addBtn;
    private ImageButton optionBtn;

    private Intent intent = null;
    private String currentPath = null;

    AlertDialog alertDialog;

    static final int CREATE_REQUEST_CODE = 0;
    private String currentProcess = null;
    private DropboxAPI.Entry currentProcessEntry;
    private String returnFileOrFolderPath = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = getIntent();
        currentPath = intent.getStringExtra("currentPath");
        currentView = intent.getStringExtra("viewType");

        if (currentView.equalsIgnoreCase("detail")) {
            setContentView(R.layout.dropbox_listview);
        } else if (currentView.equalsIgnoreCase("grid")) {
            setContentView(R.layout.dropbox_gridview);
        }

        // Delta, get current state of folder
//        try {
//            deltaInfo = MainActivity.mDBApi.delta("");
//        } catch (DropboxException e) {
//            e.printStackTrace();
//        }


        // run AsyncTask and wait still it's done running
        // Shouldn't wait still it's done runing but if I didn't do it
        // Entries won't be changed in time and still be Null


        ReadDropboxFile_Network task = new ReadDropboxFile_Network(this, currentPath);

        try {
            Void str_result = task.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        // End run AsyncTask

        // Set and aside Item to listView


        if (currentView.equalsIgnoreCase("detail")) {
            listViewInitialize();
        } else if (currentView.equalsIgnoreCase("grid")) {
            gridViewInitialize();
        }

        // ADD Button Listener
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFileOrFolder("file");
                currentProcess = "upload";
            }
        });

        // Option Button Listener
        optionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionPopUp(v);
            }
        });
    }

    public void openFolder(int position) {
        Intent intent = new Intent(this, ViewDropboxActivity.class);
        intent.putExtra("currentPath", Entries.get(position).path);
        intent.putExtra("viewType", currentView);
        startActivity(intent);
    }


    public void listViewInitialize() {
        listView = (ListView) findViewById(R.id.item_listview);
        ad = new DropboxItemAdapter(Entries, this, currentView);
        listView.setAdapter(ad);

        // Controller for each item when click

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Entries.get(position).isDir) {
                    openFolder(position);
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showFileOptionPopUp(Entries.get(position), position);
                return true;
            }
        });

        addBtn = (ImageButton) findViewById(R.id.listview_add_accept_file_button);
        optionBtn = (ImageButton) findViewById(R.id.listview_option_deny_button);

    }

    public void gridViewInitialize() {
        gridView = (GridView) findViewById(R.id.item_gridview);
        ad = new DropboxItemAdapter(Entries, this, currentView);
        gridView.setAdapter(ad);

        // Controller for each item when click

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Entries.get(position).isDir) {
                    openFolder(position);
                }
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showFileOptionPopUp(Entries.get(position), position);
                return true;
            }
        });

        addBtn = (ImageButton) findViewById(R.id.grid_add_file_button);
        optionBtn = (ImageButton) findViewById(R.id.grid_option_button);
    }

    ///////////////////////  POP UP   ///////////////////////


    public void showOptionPopUp(View view) {

        String[] ObjectItemData = new String[2];

        ObjectItemData[0] = "View";
        ObjectItemData[1] = "Unlink";

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ObjectItemData);

        ListView listViewItems = new ListView(this);
        listViewItems.setAdapter(adapter);
        listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = view.getContext();
                ((ViewDropboxActivity) context).alertDialog.cancel();
                switch (position) {
                    case 0:
                        showViewSelectionPopUp(view);
                        break;
                    case 1:
                        unlink();
                        break;
                }
            }
        });

        alertDialog = new AlertDialog.Builder(this)
                .setView(listViewItems)
                .setTitle("Menus")
                .show();

    }

    public void showViewSelectionPopUp(View view) {

        String[] ObjectItemData = new String[2];

        ObjectItemData[0] = "Grid View";
        ObjectItemData[1] = "Detail View";

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ObjectItemData);


        ListView listViewItems = new ListView(this);
        listViewItems.setAdapter(adapter);
        listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = view.getContext();
                switch (position) {

                    case 0:
                        finish();
                        writeCurrentView("grid");
                        openViewDropbox(view, "grid");
                        break;

                    case 1:
                        finish();
                        writeCurrentView("detail");
                        openViewDropbox(view, "detail");
                        break;
                }

                ((ViewDropboxActivity) context).alertDialog.cancel();
            }
        });
        alertDialog = new AlertDialog.Builder(this)
                .setView(listViewItems)
                .setTitle("View Selection")
                .show();

    }

    public void showFileOptionPopUp(final DropboxAPI.Entry entry, int position) {

        String[] ObjectItemData = new String[4];

        ObjectItemData[0] = "Download";
        ObjectItemData[1] = "Rename";
        ObjectItemData[2] = "Move";
        ObjectItemData[3] = "Delete";

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ObjectItemData);

        ListView listViewItems = new ListView(this);
        listViewItems.setAdapter(adapter);
        listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = view.getContext();
                switch (position) {

                    // Download File, Folder
                    case 0:
                        ((ViewDropboxActivity) context).alertDialog.cancel();
                        selectFileOrFolder("folder");

                        currentProcess = "download";
                        currentProcessEntry = entry;
                        break;

                    // Rename File, Folder
                    case 1:
                        ((ViewDropboxActivity) context).alertDialog.cancel();

                        showRenamePopUp(entry);

                        break;

                    // Move Command
                    case 2:
                        ((ViewDropboxActivity) context).alertDialog.cancel();
                        selectDestinationFolder();

                        currentProcess = "move";
                        currentProcessEntry = entry;

                        break;

                    // Delete file - folder
                    case 3:
                        deleteFromDropbox(entry);
                        ((ViewDropboxActivity) context).alertDialog.cancel();

                        break;
                }
            }
        });

        alertDialog = new AlertDialog.Builder(this)
                .setView(listViewItems)
                .setTitle("File Option")
                .show();

    }

    public void showRenamePopUp(final DropboxAPI.Entry entry) {

        final EditText input = new EditText(this);
        input.setText(entry.fileName());

        alertDialog = new AlertDialog.Builder(this)
                .setView(input)
                .setTitle("Rename")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newName = input.getText().toString();
                        if (!entry.fileName().equals(newName)) {
                            if (!newName.isEmpty()) {
                                renameFromDropbox(entry, newName);
                            }
                        }
                    }
                })
                .show();
    }

    // Getter, Setter

    public void setEntries(List<DropboxAPI.Entry> entries) {
        Entries = entries;
    }

    public void setCurrentPath(String currentPath) {
        this.currentPath = currentPath;
    }

    public DropboxItemAdapter getAd() {
        return ad;
    }

    public List<DropboxAPI.Entry> getEntries() {
        return Entries;
    }

    public String getCurrentPath() {
        return currentPath;
    }

    ////////////////// RENAME, DELETE  ///////////////////////

    public void renameFromDropbox(DropboxAPI.Entry entry, String newName) {

        RenameDropboxItem_Network renameTask = new RenameDropboxItem_Network(currentActivity, entry, newName);
        renameTask.execute();

        refreshView();
    }

    public void deleteFromDropbox(DropboxAPI.Entry entry) {
        DeleteDropboxItem_Network deleteTask = new DeleteDropboxItem_Network(entry);

        deleteTask.execute();

        Entries.remove(entry);
        ad.notifyDataSetChanged();
    }

    public void notifyEntriesChanged(List<DropboxAPI.Entry> entries) {
        Entries.clear();
        Entries.addAll(entries);
        ad = new DropboxItemAdapter(Entries, this, currentView);
        listView.setAdapter(ad);
    }

    public void openViewDropbox(View view, String newView) {
        Intent intent = new Intent(this, ViewDropboxActivity.class);
        intent.putExtra("currentPath", currentPath);
        intent.putExtra("viewType", newView);
        startActivity(intent);
    }

    public void unlink() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("unlink", true);
        startActivity(intent);
    }

    public void writeCurrentView(String viewType) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("viewConfig.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(viewType);
            outputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void selectFileOrFolder(String type) {
        Intent intent = new Intent(this, FileAndFolderChooser.class);
        File sdcard = Environment.getExternalStorageDirectory();
        intent.putExtra("sdCardPath", sdcard.getAbsolutePath() + "/storage");
        intent.putExtra("fileChooserType", type);
        startActivityForResult(intent, CREATE_REQUEST_CODE);
    }

    public void selectDestinationFolder(){
        Intent intent = new Intent(this, MoveDropboxView.class);
        intent.putExtra("currentPath", "/");
        startActivityForResult(intent, CREATE_REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                returnFileOrFolderPath = readSelectedPath();

                // I have to do this because when it change to startActivityForResult
                // It won't stop for main activity and try to run the rest of the code without waiting for result
                // So I can only use this to download, move file and upload
                if (currentProcess.equalsIgnoreCase("download")) {
                    downloadFile();
                } else if(currentProcess.equalsIgnoreCase("upload")) {
                    uploadFile();
                } else if(currentProcess.equalsIgnoreCase("move")) {
                    moveFile();
                }

            }
        }
    }


    public void downloadFile() {
        DownloadDropboxItem_Network downloadTask = new DownloadDropboxItem_Network(currentProcessEntry, returnFileOrFolderPath);
        downloadTask.execute();
        returnFileOrFolderPath = null;
    }

    public void uploadFile() {
        UploadDropboxItem_Network uploadTask = new UploadDropboxItem_Network(currentActivity, returnFileOrFolderPath);
        uploadTask.execute();

        refreshView();

    }

    public void moveFile(){
        MoveDropboxItem_Network moveTask = new MoveDropboxItem_Network(currentProcessEntry, returnFileOrFolderPath);
        moveTask.execute();

        refreshView();
    }

    public void refreshView(){
        ReadDropboxFileWithoutStop_Network readFileWithoutStopTask = new ReadDropboxFileWithoutStop_Network(currentActivity, currentPath);
        readFileWithoutStopTask.execute();
    }


    public String readSelectedPath() {
        String viewType = "";

        try {
            InputStream inputStream = openFileInput("selectFileConfig.txt");

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
