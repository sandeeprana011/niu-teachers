package sirfireydevs.com.niuteachers;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sirfireydevs.com.niuteachers.api.ApiServices;
import sirfireydevs.com.niuteachers.api.ApiUtil;
import sirfireydevs.com.niuteachers.api.ResponseRecords;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public static final String TAG = "HomeFragment";

    @BindView(R.id.rv_recordslist) RecyclerView rv_recordslist;
    private RVRecordsAdapter adapter;

    public HomeFragment() {
        // Required empty public constructor
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

        adapter = new RVRecordsAdapter(getContext());
        rv_recordslist.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_recordslist.setAdapter(adapter);

        ApiServices services = ApiUtil.getService();
        String teachId = UserPref.getTeacher(getContext()).getTeacher_id();
        Call<ResponseRecords> call = services.getRecordsList(teachId);
        call.enqueue(new Callback<ResponseRecords>() {
            @Override
            public void onResponse(Call<ResponseRecords> call, Response<ResponseRecords> response) {
                if (response.isSuccessful() && response.body().getStatus() == 200) {
                    adapter.addNewRecords(response.body().getRecords());
                }
            }

            @Override
            public void onFailure(Call<ResponseRecords> call, Throwable t) {

            }
        });
    }
}
