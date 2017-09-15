package sirfireydevs.com.niuteachers.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import sirfireydevs.com.niuteachers.api.models.ResponseTeachers;

/**
 * Created by sandeeprana on 10/09/17.
 * License is only applicable to individuals and non-profits
 * and that any for-profit company must
 * purchase a different license, and create
 * a second commercial license of your
 * choosing for companies
 */

public interface ApiServices {

    @GET("teachers")
    Call<ResponseTeachers> getTEachersList();

    @GET("teachers/profile")
    Call<ResponseTeacherProfile> getTeacherProfile(
            @Query("email") String email,
            @Query("password") String password
    );

    @GET("teachers/records")
    Call<ResponseRecords> getRecordsList(
            @Query("teacher_id") String teacherId);

    @POST("record/add")
    Call<StatusAndMessage> addRecord(
            @Query("title") String title,
            @Query("teacher_id") String teacher_id,
            @Query("subject") String subject,
            @Query("type") String type,
            @Query("note") String note,
            @Query("url") String url,
            @Query("file_type") String file_type
    );

}
