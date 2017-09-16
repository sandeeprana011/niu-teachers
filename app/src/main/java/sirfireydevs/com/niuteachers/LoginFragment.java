package sirfireydevs.com.niuteachers;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sirfireydevs.com.niuteachers.api.ApiServices;
import sirfireydevs.com.niuteachers.api.ApiUtil;
import sirfireydevs.com.niuteachers.api.ResponseTeacherProfile;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";
    @BindView(R.id.e_username) EditText e_username;
    @BindView(R.id.e_password) EditText e_password;
    @BindView(R.id.b_login) Button b_login;

    public LoginFragment() {
        // Required empty public constructor
    }

    @OnClick(R.id.b_login)
    void onClickLogin(final Button button) {
        button.setEnabled(false);
        ApiServices services = ApiUtil.getService();
        Call<ResponseTeacherProfile> call = services.getTeacherProfile(e_username.getText().toString(), e_password.getText().toString());
        call.enqueue(new Callback<ResponseTeacherProfile>() {
            @Override
            public void onResponse(Call<ResponseTeacherProfile> call, Response<ResponseTeacherProfile> response) {
                button.setEnabled(true);
                if (response.isSuccessful() && response.body().getStatus() == 200) {
                    UserPref.saveTeacher(getContext(), new Gson().toJson(response.body().getTeacher()));
                    getFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment(), HomeFragment.TAG).commitAllowingStateLoss();
                }
            }

            @Override
            public void onFailure(Call<ResponseTeacherProfile> call, Throwable t) {
                button.setEnabled(true);
                Log.e(TAG, t.getMessage());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }
}
