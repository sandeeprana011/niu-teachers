package sirfireydevs.com.niuteachers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import sirfireydevs.com.niuteachers.api.models.Record;

/**
 * Created by sandeeprana on 10/09/17.
 * License is only applicable to individuals and non-profits
 * and that any for-profit company must
 * purchase a different license, and create
 * a second commercial license of your
 * choosing for companies
 */

class RVRecordsAdapter extends RecyclerView.Adapter<RVRecordsAdapter.Holder> {

    ArrayList<Record> arrayList;
    SimpleDateFormat dateFormat;
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
//            Glide.with(context)
//                    .load()
            holder.t_titlefile.setText(record.getTitle());
            holder.t_description.setText(record.getNote());
            holder.t_timestamp.setText(dateFormat.format(date));
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void addNewRecords(ArrayList<Record> records) {
        this.arrayList.addAll(records);
        this.notifyDataSetChanged();

    }

    public class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.t_description) TextView t_description;
        @BindView(R.id.t_titlefile) TextView t_titlefile;
        @BindView(R.id.t_timestamp) TextView t_timestamp;
        @BindView(R.id.i_filetype) ImageView i_filetype;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
