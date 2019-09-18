package com.example.mike.pinklist;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.XmlRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.mike.pinklist.flux.SessionManager;
import com.example.mike.pinklist.models.PhotoClass;
import com.example.mike.pinklist.models.ProfileEdit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import de.hdodenhof.circleimageview.CircleImageView;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Profile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
    public class Profile extends Fragment implements
        SharedPreferences.OnSharedPreferenceChangeListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private CircleImageView cv;
    private SwitchCompat sw,s_vibrate,s_share,s_status;
    FirebaseUser user;
    ProgressDialog pd;
    FirebaseDatabase fb;
    DatabaseReference db;
    //for image
    SessionManager sessionManager;
    DatabaseReference image_ref;
    FirebaseAuth fth;
    String photo,name,email,fullName;
    private TextView tv,t_email;
    // TODO: Rename and change types of parameters
    private String mParam1;
    boolean set;
    boolean isSet;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private StorageReference mStorageRef;
    private SharedPreferences sp;
    private SharedPreferences sf;
    public static final String FILENAME = "My shared string";
    public static final String FILENAMES = "My_shared_string";
    public Profile() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     // @param param1 Parameter 1.
     // @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment newInstance() {
        Profile fragment = new Profile();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        sessionManager = new SessionManager(getContext());
        mStorageRef = FirebaseStorage.getInstance().getReference();
        fb = FirebaseDatabase.getInstance();
        pd = new ProgressDialog(getContext());
        fth = FirebaseAuth.getInstance();
        db = fb.getReference("users");
        //sp= getContext().getSharedPreferences(FILENAME,MODE_PRIVATE);
        //sf= getContext().getSharedPreferences(FILENAMES,MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
         tv= (TextView) view.findViewById(R.id.profile_namess);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(getResources().
                getDrawable(R.color.Colorpigpink));

        Typeface tp = Typeface.createFromAsset(getContext().getAssets(),"fonts/NotoSans-Bold.ttf");
        tv.setTypeface(tp);
        sw= (SwitchCompat) view.findViewById(R.id.set_notifications);
        s_vibrate= (SwitchCompat) view.findViewById(R.id.set_vibration);
        s_share= (SwitchCompat) view.findViewById(R.id.set_sharing);
        s_status= (SwitchCompat) view.findViewById(R.id.show_status);
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
        cv = (CircleImageView) view.findViewById(R.id.profile_image);
        //tt = (TextView) view.findViewById(R.id.profile_names);
        t_email = (TextView) view.findViewById(R.id.profile_email);
        //image_ref = db.child(fth.getCurrentUser().getUid());
        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i,0);
            }});
        setUpViews();
        return view;
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
                        //tv.setText(name);
                        tv.setText("Mike");
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
        db.child(Objects.requireNonNull(fth.getCurrentUser()).getUid()).child("Profile URL").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                photo = String.valueOf(dataSnapshot.getValue());
                /*Toast.makeText(getContext(),photo,Toast.LENGTH_SHORT).show();*/
                if (photo != null){
                    //Picasso.with(getContext()).load(photo).error(R.drawable.circled_user_male_104_px_2).into(cv);
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

                    /*Glide.with(getContext()).load(Uri.parse(photo)).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.circled_user_male_104_px_2)
                            .listener(new RequestListener<Uri, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {

                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            Toast.makeText(getContext(),"Updated",Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }).into(cv);*/

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
            final Uri uri = data.getData();
            assert uri != null;
            StorageReference filepath = mStorageRef.child("photos").child(Objects.requireNonNull(uri.getLastPathSegment()));
            pd.setMessage("Uploading....");
            pd.show();
            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(final Uri uri) {
                    pd.dismiss();
                    db.child(Objects.requireNonNull(fth.getCurrentUser()).getUid()).child("Profile URL").setValue(uri.toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @RequiresApi(api = Build.VERSION_CODES.M)
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Glide.with(getContext()).load(uri).error(R.drawable.circled_user_male_104_px_2).listener(
                                                new RequestListener<Uri, GlideDrawable>() {
                                                    @Override
                                                    public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                                                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                                        e.printStackTrace();
                                                        return false;
                                                    }

                                                    @Override
                                                    public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target,
                                                                                   boolean isFromMemoryCache, boolean isFirstResource) {
                                                        return false;
                                                    }
                                                }
                                        ).into(cv);
                                    }
                                }
                            });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(getContext(), "Upload failed", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            });
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
        switch (item.getItemId()) {
            case R.id.edit_profile:
                Intent intent =  new Intent(getActivity(),EditProfile.class);
                getActivity().startActivityForResult(intent,0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // TODO: Rename method, update argument and hook method into UI event

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
