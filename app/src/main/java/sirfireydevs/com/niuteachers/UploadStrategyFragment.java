package sirfireydevs.com.niuteachers;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;


/**
 * A simple {@link Fragment} subclass.
 */
public class UploadStrategyFragment extends Fragment {

    public static final String TAG = "uploadstrategyfragment";
    @BindView(R.id.b_upload) Button b_upload;
    @BindView(R.id.i_document) ImageView i_document;
    @BindView(R.id.t_subject) TextView t_subject;
    @BindView(R.id.t_note) TextView t_note;
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
    }
}
