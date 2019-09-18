package com.example.mike.pinklist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mike.pinklist.adapters.UpcomingAdapterr;
import com.example.mike.pinklist.models.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Notifications.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Notifications#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Notifications extends Fragment implements SearchView.OnQueryTextListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebaseDatabase database;
    DatabaseReference databaseRef;
    FirebaseAuth firebaseAuth;
    List<Task> taskList = new ArrayList<>();
    TextView textView,tv,message,textView2,message2;
    CardView cardView,cardView2;
    List<String> todayList = new ArrayList<String>();
    String complete = "Hooray! You have completed all the tasks for today.";
    String update = "A new update is available! Go to Play Store";
    String missed_task = "You missed one task yesterday";
    private OnFragmentInteractionListener mListener;

    public Notifications() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
    // * @param param1 Parameter 1.
     //* @param param2 Parameter 2.
     * @return A new instance of fragment Notifications.
     */
    // TODO: Rename and change types and number of parameters
    public static Notifications newInstance() {
        Notifications fragment = new Notifications();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseRef = database.getReference("users");
        }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        tv= (TextView) view.findViewById(R.id.notify);
        cardView = (CardView) view.findViewById(R.id.cardView);
        cardView2 = (CardView) view.findViewById(R.id.cardView2);
        textView = (TextView) view.findViewById(R.id.tvDate);
        textView.setText("Today");
        textView2 = (TextView) view.findViewById(R.id.tvDate2);
        textView2.setText("Yesterday");
        message = (TextView) view.findViewById(R.id.message);
        message2 = (TextView)view.findViewById(R.id.message2);
        Typeface tp = Typeface.createFromAsset(getContext().getAssets(),"fonts/NotoSans-Bold.ttf");
        tv.setTypeface(tp);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(getResources().getDrawable
                (R.color.Colorpigpink));
        //Toast.makeText(getContext(),yesterdays(),Toast.LENGTH_SHORT).show();
        fetchData();
        return view;
    }

    private void fetchData(){
        message.setText("No task added today");
        message2.setText("No task added yesterday");
        //today's date
        if (taskList.size()>0)
            taskList.clear();
        databaseRef.child(firebaseAuth.getCurrentUser().getUid()).child("task_list").orderByChild("date").equalTo(str())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("checks", String.valueOf(dataSnapshot));
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Task task = dataSnapshot1.getValue(Task.class);

                    taskList.add(task);
                    if (taskList.size() == 0){
                     //todayList.add(complete);
                        message.setText(complete);
                    }
                    else {
                        String status = "You have "+taskList.size()+" uncompleted task(s)";
                        message.setText(status);
                    }
                    //updateListView();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        //yesterday's date
        if (taskList.size() > 0)
            taskList.clear();
        databaseRef.child(firebaseAuth.getCurrentUser().getUid()).child("task_list")
                .orderByChild("date").equalTo(yesterdays()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("checks", String.valueOf(dataSnapshot));
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                            Task task = dataSnapshot1.getValue(Task.class);
                            taskList.add(task);
                            if (taskList.size() > 0){
                                //todayList.add("You missed "+taskList.size()+" task yesterday.");
                                String status = "You missed "+taskList.size()+" task(s)";
                                message2.setText(status);
                            }
                            else{
                                message2.setText(complete);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
        //updateListView();
    }

    /*private void updateListView() {
        adapter.setTasks(taskList);
        adapter.notifyDataSetChanged();
    }*/

    private String str(){
        Calendar cal = Calendar.getInstance();
        DateFormat spd = new SimpleDateFormat("dd/MM/yyyy");
        long me = System.currentTimeMillis();
        String dt = spd.format(me);
        Log.i("present", cal.getTime().toString());
        return dt;
    }

    private String yesterday(){
        Calendar cal = Calendar.getInstance();
        DateFormat spd = new SimpleDateFormat("dd/MM/yyyy");
        long me = System.currentTimeMillis();
        String dt = spd.format(me);
        Log.i("present", cal.getTime().toString());
        return dt;
    }
    private String yesterdays(){
        Calendar cal = Calendar.getInstance();
        DateFormat spd = new SimpleDateFormat("dd/MM/yyyy");
        cal.add(Calendar.DATE,-1);
        String dt = spd.format(cal.getTime());
        return dt;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            super.onCreateOptionsMenu(menu, inflater);
        /*MenuItem search = menu.findItem(R.id.search);//search button
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        //searchView.setMaxWidth(570);
        //searchView.setMinimumHeight(30);
        searchView.setIconifiedByDefault(false);//hides default search icon inside edittext
        int magId = getResources().getIdentifier("android:id/search_mag_icon",null,null);
        int search_plate = getResources().getIdentifier("android:id/search_plate",null,null);
        ImageView imageView = (ImageView) searchView.findViewById(magId);
        imageView.setImageDrawable(null);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(0,0));//removing search icon in hint
        View v = searchView.findViewById(search_plate);// making the underline transparent
        v.setBackgroundColor(Color.TRANSPARENT);
        searchView.setQueryHint("Search Task");
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search Task");
        MenuItemCompat.setOnActionExpandListener(search, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Set styles for expanded state here/
                *//*if (((AppCompatActivity)getActivity()).getSupportActionBar() != null) {
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                }*//*
                return true;
            }
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Set styles for collapsed state here
                if (((AppCompatActivity)getActivity()).getSupportActionBar() != null) {
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.Colorpigpink));
                }
                final FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content, Notifications.newInstance());
                transaction.commit();
                //Toast.makeText(getContext(),tasks.size(),Toast.LENGTH_SHORT).show();
                return true;
            }
        });*/
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.edit_profile:
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
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String date1 = textView.getText().toString();
        String date = textView2.getText().toString();
        if (date1.toLowerCase().contains(newText.toLowerCase())){
            cardView2.setVisibility(View.GONE);
        }else if ( date.toLowerCase().contains(newText.toLowerCase())) {
            cardView.setVisibility(View.GONE);
        }
        return false;
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
