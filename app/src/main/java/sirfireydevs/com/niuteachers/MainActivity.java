package sirfireydevs.com.niuteachers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean lof = UserPref.isLoggedIn(this);
        if (lof) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
        }else {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new LoginFragment()).commit();
        }
    }
}
