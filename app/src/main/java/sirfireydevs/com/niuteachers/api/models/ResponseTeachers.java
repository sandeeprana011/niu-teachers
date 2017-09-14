package sirfireydevs.com.niuteachers.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by sandeeprana on 10/09/17.
 * License is only applicable to individuals and non-profits
 * and that any for-profit company must
 * purchase a different license, and create
 * a second commercial license of your
 * choosing for companies
 */

public class ResponseTeachers {
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("teachers")
    @Expose

    private ArrayList<Teachers> teachersArrayList;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public ArrayList<Teachers> getTeachersArrayList() {
        return teachersArrayList;
    }

    public void setTeachersArrayList(ArrayList<Teachers> teachersArrayList) {
        this.teachersArrayList = teachersArrayList;
    }
}
