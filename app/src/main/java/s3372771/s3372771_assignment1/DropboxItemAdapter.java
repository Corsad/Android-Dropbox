package s3372771.s3372771_assignment1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dropbox.client2.DropboxAPI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by s3372771
 */
public class DropboxItemAdapter extends BaseAdapter {
    private List<DropboxAPI.Entry> Entries = new ArrayList<DropboxAPI.Entry>();
    private Context ctxt;
    private LayoutInflater myInflater = null;
    private String CurrentViewType;
    private View view = null;

    public DropboxItemAdapter(List<DropboxAPI.Entry> Entries, Context ctxt, String CurrentViewType) {
        this.Entries = Entries;
        this.ctxt = ctxt;
        myInflater = LayoutInflater.from(ctxt);
        this.CurrentViewType = CurrentViewType;
    }

    @Override
    public int getCount() {
        return Entries.size();
    }

    @Override
    public Object getItem(int position) {
        return Entries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (CurrentViewType.equalsIgnoreCase("detail")) {
            detailViewList(position, convertView, parent);
        } else if (CurrentViewType.equalsIgnoreCase("grid")) {
            gridViewList(position, convertView, parent);
        }

        return view;
    }

    public void detailViewList(int position, View convertView, ViewGroup parent) {
        DetailViewHolder holder;

        if (convertView == null) {
            view = myInflater.inflate(R.layout.defail_listview, parent, false);
            holder = new DetailViewHolder();
            holder.detail_name_text = (TextView) view.findViewById(R.id.detail_name_text);
            holder.detail_directory_icon = (ImageView) view.findViewById(R.id.detail_directory_icon);
            holder.detail_modified_text = (TextView) view.findViewById(R.id.detail_modified_text);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (DetailViewHolder) view.getTag();
        }

        holder.detail_name_text.setText(Entries.get(position).fileName());
        holder.detail_modified_text.setText(Entries.get(position).modified);

        if (Entries.get(position).isDir) {
            holder.detail_directory_icon.setVisibility(View.VISIBLE);
        }
    }

    public void gridViewList(int position, View convertView, ViewGroup parent) {
        GridViewHolder holder;

        if (convertView == null) {
            view = myInflater.inflate(R.layout.gridview, parent, false);
            holder = new GridViewHolder();
            holder.grid_item_label = (TextView) view.findViewById(R.id.grid_item_label);
            holder.grid_item_image = (ImageView) view.findViewById(R.id.grid_item_image);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (GridViewHolder) view.getTag();
        }

        holder.grid_item_label.setText(Entries.get(position).fileName());

        if (Entries.get(position).isDir) {
            holder.grid_item_image.setImageResource(R.drawable.folder_icon);
        } else {
            holder.grid_item_image.setImageResource(R.drawable.file_icon);
        }
    }

    private class GridViewHolder {
        public ImageView grid_item_image;
        public TextView grid_item_label;
    }

    private class DetailViewHolder {
        public ImageView detail_directory_icon;
        public TextView detail_name_text, detail_modified_text;
    }

    public void updateEntriesList(List<DropboxAPI.Entry> entries) {
        Entries.clear();
        Entries.addAll(entries);
        this.notifyDataSetChanged();
    }
}
