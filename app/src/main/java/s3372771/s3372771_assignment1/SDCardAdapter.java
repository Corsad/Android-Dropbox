package s3372771.s3372771_assignment1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

/**
 * Created by Tan on 04/03/15.
 */
public class SDCardAdapter extends BaseAdapter {

    private Context ctxt;
    private LayoutInflater myInflater = null;
    private List<File> files;

    public SDCardAdapter(List<File> files, Context ctxt) {
        this.files = files;
        this.ctxt = ctxt;
        myInflater = LayoutInflater.from(ctxt);
    }

    @Override
    public int getCount() {
        return files.size();
    }

    @Override
    public Object getItem(int position) {
        return files.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        DetailViewHolder holder;

        if (convertView == null) {
            view = myInflater.inflate(R.layout.defail_listview, parent, false);
            holder = new DetailViewHolder();
            holder.detail_name_text = (TextView) view.findViewById(R.id.detail_name_text);
            holder.detail_directory_icon = (ImageView) view.findViewById(R.id.detail_directory_icon);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (DetailViewHolder) view.getTag();
        }

        holder.detail_name_text.setText(files.get(position).getName());
        if (files.get(position).isDirectory()) {
            holder.detail_directory_icon.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private class DetailViewHolder {
        public ImageView detail_directory_icon;
        public TextView detail_name_text, detail_modified_text;
    }
}
