package com.zilideus.niucommons;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zilideus.niucommons.api.models.Record;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sandeeprana on 10/09/17.
 * License is only applicable to individuals and non-profits
 * and that any for-profit company must
 * purchase a different license, and create
 * a second commercial license of your
 * choosing for companies
 */

public class RVRecordsAdapter extends RecyclerView.Adapter<RVRecordsAdapter.Holder> {

    ArrayList<Record> arrayList;
    SimpleDateFormat dateFormat;
    private OnRowClickListener onRowClickListener;
    private Context context;


    public RVRecordsAdapter(Context context) {
        this.context = context;
        arrayList = new ArrayList<>();
        dateFormat = new SimpleDateFormat("dd MMM yyyy");
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.records_item, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Record record = this.arrayList.get(position);

        if (record != null) {
            Date date = new Date(record.getTimestamp());
            holder.t_titlefile.setText(record.getTitle());
            holder.t_description.setText(record.getNote());
            holder.t_timestamp.setText(dateFormat.format(date));
            switch (record.getType()) {
                case Constants.TYPE_TEXT:
                    Glide.with(context)
                            .load(R.drawable.file_type_txt)
                            .into(holder.i_filetype);
                    break;
                case Constants.TYPE_FILE:
                    switch (record.getFile_type() == null ? "unknown" : record.getFile_type()) {
                        case Constants.FileType.IMAGE:
                            Glide.with(context)
                                    .load(R.drawable.icon_image)
                                    .into(holder.i_filetype);
                            break;
                        case Constants.FileType.VIDEO:
                            Glide.with(context)
                                    .load(R.drawable.icon_video)
                                    .into(holder.i_filetype);
                            break;
                        case Constants.FileType.AUDIO:
                            Glide.with(context)
                                    .load(R.drawable.icon_audio)
                                    .into(holder.i_filetype);
                            break;
                        case Constants.FileType.DOCUMENT:
                            Glide.with(context)
                                    .load(R.drawable.doc)
                                    .into(holder.i_filetype);
                            break;
                        case Constants.FileType.URL:
                            Glide.with(context)
                                    .load(R.drawable.url_link)
                                    .into(holder.i_filetype);
                            break;
                        default:
                            Glide.with(context)
                                    .load(R.drawable.unknown)
                                    .into(holder.i_filetype);
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void addNewRecords(ArrayList<Record> records) {
        this.arrayList.clear();
        this.arrayList.addAll(records);
        this.notifyDataSetChanged();
    }

    public void setOnRowClickListener(OnRowClickListener onRowClickListener) {
        this.onRowClickListener = onRowClickListener;
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R2.id.t_description) TextView t_description;
        @BindView(R2.id.t_titlefile) TextView t_titlefile;
        @BindView(R2.id.t_timestamp) TextView t_timestamp;
        @BindView(R2.id.i_filetype) ImageView i_filetype;


        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onRowClickListener != null) {
                onRowClickListener.onRowClicked(view, arrayList.get(getAdapterPosition()), getAdapterPosition());
            }
        }
    }
}
