package com.clintonmedbery.rajawalibasicproject;

/**
 * Created by romanismagilov on 30.04.17.
 */

import android.content.ClipData;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by yarolegovich on 07.03.2017.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private List<Sight> data;

    public PhotoAdapter(List<Sight> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_sight, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.image.setImageDrawable(
                holder.itemView
                        .getContext()
                        .getResources()
                        .getDrawable(data.get(position).image)
        );
        holder.text.setText(data.get(position).name);
        holder.descr.setText(data.get(position).descr);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView text;
        private TextView descr;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            text = (TextView) itemView.findViewById(R.id.tv_sightname);
            descr = (TextView) itemView.findViewById(R.id.tv_row_descr);
        }
    }
}
