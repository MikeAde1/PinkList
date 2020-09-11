package com.example.mike.pinklist.ui;
import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.mike.pinklist.BuildConfig;
import com.example.mike.pinklist.R;
import com.example.mike.pinklist.store.SessionManager;
import com.example.mike.pinklist.utils.PermissionUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.example.mike.pinklist.ui.BottomSheetDialogFragmentKt.CAMERA;
import static com.example.mike.pinklist.ui.BottomSheetDialogFragmentKt.GALLERY;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Profile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class Profile extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener, BottomFragment.ItemClickListener{

    private boolean set;
    private boolean isSet;
    private CircleImageView cv;
    private SwitchCompat sw,s_vibrate,s_share,s_status;
    private ProgressDialog pd;
    private DatabaseReference db;
    private SharedPreferences sp;
    private SharedPreferences sf;
    private SessionManager sessionManager;
    private FirebaseAuth fth;

    private OnFragmentInteractionListener mListener;
    private StorageReference mStorageRef;

    private TextView tv,t_email;
    private BottomFragment bottomFragment = BottomFragment.Companion.newInstance();

    public static final String FILENAME = "My shared string";
    public static final String FILENAMES = "My_shared_string";
    private static final int STORAGE = 123;

    private String photo,name,email,fullName;
    private String currentPhotoPath;
    private String [] perms = {Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    static Fragment newInstance() {
        return new Profile();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        sessionManager = new SessionManager(getContext());
        mStorageRef = FirebaseStorage.getInstance().getReference();
        FirebaseDatabase fb = FirebaseDatabase.getInstance();
        pd = new ProgressDialog(getContext());
        fth = FirebaseAuth.getInstance();
        db = fb.getReference("users");
        //sp= getContext().getSharedPreferences(FILENAME,MODE_PRIVATE);
        //sf= getContext().getSharedPreferences(FILENAMES,MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
         tv = view.findViewById(R.id.profile_namess);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(getResources()
                .getDrawable(R.color.Colorpigpink));

        Typeface tp = Typeface.createFromAsset(getContext().getAssets(),"fonts/NotoSans-Bold.ttf");
        tv.setTypeface(tp);
        sw = view.findViewById(R.id.set_notifications);
        s_vibrate = view.findViewById(R.id.set_vibration);
        s_share = view.findViewById(R.id.set_sharing);
        s_status = view.findViewById(R.id.show_status);
        //SharedPreferences sharedPrefs = getContext().getSharedPreferences(FILENAME, MODE_PRIVATE);
        s_vibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                          @Override
                                          public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                  set = isChecked;
                                                  setUpVibration();
                                              }
                                      });
        s_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //value is true
                isSet = isChecked;
                setUpStatus();
            }
        });
        cv = view.findViewById(R.id.profile_image);
        //tt = (TextView) view.findViewById(R.id.profile_names);
        t_email = view.findViewById(R.id.profile_email);
        //image_ref = db.child(fth.getCurrentUser().getUid());
        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissions();
            }
        });

        setUpViews();
        return view;
    }

    private void checkPermissions() {
        String [] newPerms = new String[perms.length];
        int permsCount = 0;
        for (String permission: perms) {
            if (ActivityCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(permission)) {
                    //not permanently denied
                    newPerms[permsCount] = permission;
                    permsCount++;
                } else if (PermissionUtils.neverAskAgainSelected(requireContext())) {
                    //permanently denied
                    showPermissionDialog();
                    return;
                } else {
                    //never asked
                    newPerms[permsCount] = permission;
                    permsCount++;
                }
            }
        }

        if (Arrays.toString(newPerms).contains("null")) {
            bottomFragment.show(getChildFragmentManager(), "edit_profile");
        } else {
            Log.d("TAGS", newPerms.toString());
            requestPermissions(newPerms, STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE) {
            //int count = 0;
            //boolean granted = false;
            for (int result: grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    PermissionUtils.setShouldShowStatus(requireContext());
                    return;
                }
            }

            bottomFragment.show(getChildFragmentManager(), "edit_profile");
        }
    }

    private void showPermissionDialog() {
        new AlertDialog.Builder(getContext(), R.style.AlertDialogCustom)
                .setTitle("Permission Denied")
                .setMessage("You have forcefully denied some of the required permissions for this action. " +
                        "Please open settings, go to permissions and allow them.")
                .setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", Objects.requireNonNull(getActivity()).getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                }).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        s_vibrate.setChecked(sessionManager.getVibrationStatus());
        s_status.setChecked(sessionManager.getStatus());
    }

    private void setUpViews() {
        db.child(fth.getCurrentUser().getUid()).child("User_details")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //email = (String) dataSnapshot.child("email").getValue();
                        name = (String) dataSnapshot.child("names").getValue();
                        if (name != null){
                            tv.setText(name);
                        }else {
                            tv.setText("----");
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

        FirebaseUser user = fth.getCurrentUser();
        if (user != null){
            String email = user.getEmail();
            t_email.setText(email);
        }

        setProfilePic();
     }

    private void setProfilePic() {
        db.child(Objects.requireNonNull(fth.getCurrentUser()).getUid()).child("Profile URL").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                photo = String.valueOf(dataSnapshot.getValue());
                if (photo != null && getContext() != null){
                    Glide.with(getContext()).load(photo).error(R.drawable.circled_user_male_104_px_2).listener(
                            new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    return false;
                                }
                            }
                    ).into(cv);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                if (requestCode == GALLERY) {
                    final Uri uri = data.getData();
                    assert uri != null;
                    uploadPhoto(uri);
                }
            } else if (requestCode == CAMERA) {
                try {
                    Log.d("pic_path", currentPhotoPath);
                    Uri uri = Uri.fromFile(new File(currentPhotoPath));
                    uploadPhoto(uri);
                    //convert to bitmap and compress
                }catch (Exception e) {
                    Log.d("pic_exception", e.getMessage());
                }
            }
        }
    }

    private void uploadPhoto(Uri uri) {
        final StorageReference filepath = mStorageRef.child("photos").child(uri.getLastPathSegment());
        pd.setCancelable(false);
        pd.setMessage("Uploading....");
        pd.show();

        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(final Uri uri) {
                        pd.dismiss();
                        db.child(Objects.requireNonNull(fth.getCurrentUser()).getUid())
                                .child("Profile URL").setValue(uri.toString())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @RequiresApi(api = Build.VERSION_CODES.M)
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            loadWithGlide(uri);
                                        }
                                    }
                                });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getContext(), "Upload failed", Toast.LENGTH_SHORT).show();
                Log.d("Upload failed", e.getMessage());
            }
        });
    }

    private void loadWithGlide(Uri uri) {
        Glide.with(getContext()).load(uri).error(R.drawable.circled_user_male_104_px_2).listener(
                new RequestListener<Uri, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e,
                                               Uri model,
                                               Target<GlideDrawable> target,
                                               boolean isFirstResource) {
                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, Uri model,
                                                   Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache,
                                                   boolean isFirstResource) {
                        return false;
                    }
                }
        ).into(cv);
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_itemdetail, menu);
        Drawable drawable = menu.getItem(0).getIcon();
        //for icon tint
        if(drawable!=null){
            drawable.mutate();
            drawable.setColorFilter(getResources().getColor(R.color.light), PorterDuff.Mode.SRC_ATOP);}
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        if (item.getItemId() == R.id.edit_profile) {
            Intent intent = new Intent(getActivity(), EditProfile.class);
            getActivity().startActivityForResult(intent, 0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    }

    private void setUpVibration() {
        if(set){
            sessionManager.checkVibrationStatus();
        } else {
            sessionManager.setVibrationStatus();
        }
    }

    private void setUpStatus(){
        if (isSet){
            sessionManager.trueStatus();
        }
        else{
            sessionManager.falseStatus();
        }
    }

    @Override
    public void onItemClick(int position) {
        if (position == CAMERA) {
            takePhotoFromCamera();
        } else {
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            if (i.resolveActivity(requireActivity().getPackageManager()) != null) {
                startActivityForResult(i, GALLERY);
            }
        }
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                // Error occurred while creating the File
                e.printStackTrace();
                photoFile = null;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(
                        requireContext(),
                        //"com.example.mike.pinklist.provider",
                            BuildConfig.APPLICATION_ID + ".provider",
                            photoFile
                );
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, CAMERA);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = String.valueOf(Calendar.getInstance().getTimeInMillis());
        String imageFileName = timeStamp + "_";
        //file created is within the app
        File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
