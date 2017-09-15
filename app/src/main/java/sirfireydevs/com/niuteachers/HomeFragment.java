package sirfireydevs.com.niuteachers;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.github.clans.fab.FloatingActionButton;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sirfireydevs.com.niuteachers.api.ApiServices;
import sirfireydevs.com.niuteachers.api.ApiUtil;
import sirfireydevs.com.niuteachers.api.ResponseRecords;
import sirfireydevs.com.niuteachers.api.models.Record;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements OnRowClickListener {

    public static final String TAG = "HomeFragment";

    @BindView(R.id.rv_recordslist) RecyclerView rv_recordslist;
    @BindView(R.id.fab_add_text) FloatingActionButton fab_add_text;
    @BindView(R.id.fab_upload_file) FloatingActionButton fab_upload_file;
    @BindView(R.id.progress_bar) ProgressBar progress_bar;
    private RVRecordsAdapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @OnClick(R.id.fab_add_text)
    void onClickAddText() {
        Fragment fragment = new EditextFragment();
        getFragmentManager().beginTransaction().replace(R.id.container, fragment, EditextFragment.TAG).addToBackStack(EditextFragment.TAG).commitAllowingStateLoss();
    }

    @OnClick(R.id.fab_upload_file)
    void onClickUploadFile() {
        Fragment fragment = new UploadStrategyFragment();
        getFragmentManager().beginTransaction().replace(R.id.container, fragment, UploadStrategyFragment.TAG).addToBackStack(UploadStrategyFragment.TAG).commitAllowingStateLoss();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        if (adapter == null) {
            adapter = new RVRecordsAdapter(getContext());
        }
        adapter.setOnRowClickListener(this);
        rv_recordslist.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_recordslist.setAdapter(adapter);


        ApiServices services = ApiUtil.getService();
        String teachId = UserPref.getTeacher(getContext()).getTeacher_id();

        progress_bar.setVisibility(View.VISIBLE);

        Call<ResponseRecords> call = services.getRecordsList(teachId);
        call.enqueue(new Callback<ResponseRecords>() {
            @Override
            public void onResponse(Call<ResponseRecords> call, Response<ResponseRecords> response) {
                progress_bar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body().getStatus() == 200) {
                    adapter.addNewRecords(response.body().getRecords());
                }
            }

            @Override
            public void onFailure(Call<ResponseRecords> call, Throwable t) {
                progress_bar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onRowClicked(View view, Record record, int adapterPosition) {
        switch (record.getType()) {
            case Constants.TYPE_TEXT:
                Fragment fragment = new TextFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.RECORD, new Gson().toJson(record));
                fragment.setArguments(bundle);
                ((MainActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, TextFragment.TAG).addToBackStack(TextFragment.TAG).commitAllowingStateLoss();
                break;
            case Constants.TYPE_FILE:
                switch (record.getFile_type() == null ? "unknown" : record.getFile_type()) {
                    case Constants.FileType.IMAGE:
                    case Constants.FileType.VIDEO:
                    case Constants.FileType.AUDIO:
                    case Constants.FileType.DOCUMENT:
                    case Constants.FileType.URL:
                    default:
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(record.getUrl()));
                        startActivity(browserIntent);
                        break;
                }
                break;
            default:
                break;
        }
    }
}
