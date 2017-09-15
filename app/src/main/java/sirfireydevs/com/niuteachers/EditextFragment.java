package sirfireydevs.com.niuteachers;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sirfireydevs.com.niuteachers.api.ApiServices;
import sirfireydevs.com.niuteachers.api.ApiUtil;
import sirfireydevs.com.niuteachers.api.StatusAndMessage;
import sirfireydevs.com.niuteachers.api.models.Teachers;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditextFragment extends Fragment {

    public static final String TAG = "EdittextFragmet";
    @BindView(R.id.e_title) EditText e_title;
    @BindView(R.id.e_content) EditText e_content;
    @BindView(R.id.e_subject) EditText e_subject;
    @BindView(R.id.b_save) Button b_save;

    public EditextFragment() {
        // Required empty public constructor
    }

    @OnClick(R.id.b_save)
    void onClickSaveButton(final Button button) {
        String str = "";
        if (button != null) {
            button.setEnabled(false);
        }
        if (e_title.getText().length() <= 0) {
            e_title.setError("Can't be empty!");
            button.setEnabled(true);
            return;
        }

        if (e_content.getText().length() <= 0) {
            e_content.setError("Can't be empty!");
            button.setEnabled(true);
            return;
        }
        Teachers teachers = UserPref.getTeacher(getContext());
        ApiServices services = ApiUtil.getService();
        Call<StatusAndMessage> call = services.addRecord(
                e_title.getText().toString(),
                teachers.getTeacher_id(),
                e_subject.getText().toString(),
                Constants.TYPE_TEXT,
                e_content.getText().toString(),
                null,
                null);
        call.enqueue(new Callback<StatusAndMessage>() {
            @Override
            public void onResponse(Call<StatusAndMessage> call, Response<StatusAndMessage> response) {
                if (response.isSuccessful() && response.body().getStatus() == 200) {
                    if (getActivity() != null) {
                        getActivity().onBackPressed();
                    }
                }
            }

            @Override
            public void onFailure(Call<StatusAndMessage> call, Throwable t) {
                if (button != null) {
                    button.setEnabled(true);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_editext, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }
}
