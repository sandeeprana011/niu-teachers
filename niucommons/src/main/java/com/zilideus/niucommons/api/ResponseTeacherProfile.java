package com.zilideus.niucommons.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.zilideus.niucommons.api.models.Teachers;

/**
 * Created by sandeeprana on 13/09/17.
 * License is only applicable to individuals and non-profits
 * and that any for-profit company must
 * purchase a different license, and create
 * a second commercial license of your
 * choosing for companies
 */

public class ResponseTeacherProfile {
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("message")
    @Expose
    private String message;

    public Teachers getTeacher() {
        return teacher;
    }

    public void setTeacher(Teachers teacher) {
        this.teacher = teacher;
    }

    @SerializedName("teacher")
    @Expose
    private Teachers teacher;

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

}
