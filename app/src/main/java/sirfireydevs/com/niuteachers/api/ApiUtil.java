package sirfireydevs.com.niuteachers.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sirfireydevs.com.niuteachers.BuildConfig;

/**
 * Created by sandeeprana on 10/09/17.
 * License is only applicable to individuals and non-profits
 * and that any for-profit company must
 * purchase a different license, and create
 * a second commercial license of your
 * choosing for companies
 */


public class ApiUtil {

    public static ApiServices getService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ApiServices.class);
    }

}
