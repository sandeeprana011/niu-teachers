package sirfireydevs.com.niuteachers;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.zilideus.niucommons.Constants;
import com.zilideus.niucommons.Utility;
import com.zilideus.niucommons.api.ApiServices;
import com.zilideus.niucommons.api.ApiUtil;
import com.zilideus.niucommons.api.StatusAndMessage;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class UploadStrategyFragment extends Fragment {

    public static final String TAG = "uploadstrategyfragment";
    @BindView(R.id.b_upload) Button b_upload;
    @BindView(R.id.i_document) ImageView i_document;
    @BindView(R.id.e_subject) EditText e_subject;
    @BindView(R.id.e_note) EditText e_note;
    @BindView(R.id.e_title) EditText e_title;
    private StorageReference mStorageRef;
    private String selectedFile = null;

    public UploadStrategyFragment() {
        // Required empty public constructor
    }

    @OnClick(R.id.i_document)
    void onClickDocument(final View view) {
        Dexter.withActivity(((MainActivity) getContext()))
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        PopupMenu popup = new PopupMenu(getActivity(), view);
                        //Inflating the Popup using xml file
                        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                        //registering popup with OnMenuItemClickListener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.item_camera:
                                        FilePickerBuilder.getInstance().setMaxCount(1)
                                                .setActivityTheme(R.style.AppTheme)
                                                .enableVideoPicker(true)
                                                .enableCameraSupport(true)
                                                .pickPhoto(getActivity());
                                        break;
                                    case R.id.item_file:
                                        FilePickerBuilder.getInstance().setMaxCount(1)
                                                .setActivityTheme(R.style.AppTheme)
                                                .pickFile(getActivity());
                                        break;
                                }
                                return true;
                            }
                        });

                        popup.show();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                }).check();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "OnActivityResultcalled");
        switch (requestCode) {
            case FilePickerConst.REQUEST_CODE_PHOTO:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    ArrayList<String> dat = data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA);
                    if (dat.size() > 0) {
                        this.selectedFile = dat.get(0);
                        Glide.with(getContext())
                                .load(this.selectedFile)
                                .into(i_document);

                        i_document.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }
                }
                break;
            case FilePickerConst.REQUEST_CODE_DOC:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    ArrayList<String> dat = data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS);
                    if (dat.size() > 0) {
                        this.selectedFile = dat.get(0);
                        String type = Utility.getMimeType(this.selectedFile);

                        Log.e(TAG, type);

                        Glide.with(getContext())
                                .load(R.drawable.file)
                                .into(i_document);
                    }
                }
                break;
        }


    }

    @OnClick(R.id.b_upload)
    void onClickUpload(Button button) {
        final String subject = e_subject.getText().toString();
        final String title = e_title.getText().toString();
        final String note = e_note.getText().toString();
        boolean hasError = false;


        if (selectedFile == null || selectedFile.isEmpty()) {
            hasError = true;
            Toast.makeText(getContext(), "Select atleast one file", Toast.LENGTH_SHORT).show();
        }
        if (note.isEmpty()) {
            hasError = true;
            e_note.setError("cann't be empty!");
        }
        if (title.isEmpty()) {
            hasError = true;
            e_title.setError("cann't be empty!");
        }

        if (hasError) {
            return;
        } else {
            button.setEnabled(false);
        }

        File fileP = new File(this.selectedFile);
        String strFileName = fileP.getName();

        Uri file = Uri.fromFile(fileP);
        StorageReference riversRef = this.mStorageRef.child(fileP.getName());

        String[] arrStr = fileP.getName().split("\\.");
        String fileType = Constants.UNKNOWN;
        if (arrStr.length > 1) {
            String tempTyype = arrStr[arrStr.length - 1].toUpperCase();
            switch (tempTyype) {
                case Constants.FileType.PNG:
                case Constants.FileType.JPEG:
                case Constants.FileType.JPG:
                    fileType = Constants.FileType.IMAGE;
                    break;

                case Constants.FileType.TXT:
                    fileType = Constants.FileType.TXT;
                    break;

                case Constants.FileType.MP3:
                case Constants.FileType.AMR:
                case Constants.FileType.WAV:
                    fileType = Constants.FileType.AUDIO;
                    break;

                case Constants.FileType.MP4:
                    fileType = Constants.FileType.VIDEO;
                    break;

                case Constants.FileType.PDF:
                case Constants.FileType.DOC:
                case Constants.FileType.DOCX:
                case Constants.FileType.XLS:
                case Constants.FileType.XLSX:
                case Constants.FileType.PPTX:
                case Constants.FileType.PPT:
                    fileType = Constants.FileType.DOCUMENT;
                    break;
            }
        }


        final String finalFileType = fileType;

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        @SuppressWarnings("VisibleForTests")
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Log.e(TAG, downloadUrl + "");

                        ApiServices services = ApiUtil.getService();
                        Call<StatusAndMessage> call = services.addRecord(title,
                                UserPref.getTeacher(getContext()).getTeacher_id(),
                                subject,
                                Constants.TYPE_FILE,
                                note,
                                downloadUrl.toString(),
                                finalFileType);
                        call.enqueue(new Callback<StatusAndMessage>() {
                            @Override
                            public void onResponse(Call<StatusAndMessage> call, Response<StatusAndMessage> response) {
                                if (response.isSuccessful()) {
                                    getActivity().onBackPressed();
                                }
                            }

                            @Override
                            public void onFailure(Call<StatusAndMessage> call, Throwable t) {
                                Toast.makeText(getContext(), t.getMessage() + "", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Toast.makeText(getContext(), exception.getMessage() + "", Toast.LENGTH_SHORT).show();
                    }
                });


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload_strategy, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mStorageRef = FirebaseStorage.getInstance().getReference(UserPref.getTeacher(getContext()).getTeacher_id());
    }
}
