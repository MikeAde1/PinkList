package com.example.mike.pinklist;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mike.pinklist.adapters.UpcomingAdapterr;
import com.example.mike.pinklist.models.Task;
import com.example.mike.pinklist.models.Upcoming;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Scheduler.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Scheduler#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Scheduler extends Fragment implements SearchView.OnQueryTextListener{
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ListView listV;
    UpcomingAdapterr adapter;
    List<Task> upcoming_list = new ArrayList<>();
    FirebaseDatabase database;
    private Task task;
    private DatabaseReference databaseRef;
    FirebaseAuth firebaseAuth;
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public Scheduler() {
    }
    public static Scheduler newInstance() {
        Scheduler fragment = new Scheduler();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseRef = database.getReference("users");
        //if (getArguments() != null) {
          //  mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container ,Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_scheduler, container, false);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(getResources().
                getDrawable(R.color.Colorpigpink));
            TextView tv= (TextView) view.findViewById(R.id.schedule);
            Typeface tp = Typeface.createFromAsset(getContext().getAssets(),"fonts/NotoSans-Bold.ttf");
            tv.setTypeface(tp);
            listV = (ListView) view.findViewById(R.id.lv);
            //Toast.makeText(getContext(),nextDays(),Toast.LENGTH_SHORT).show();
            setUpAdapter();
            return view;
    }
    private void setUpAdapter() {
        adapter = new UpcomingAdapterr(getContext(), upcoming_list);
        listV.setAdapter(adapter);
        fetchdata();
    }
    private void fetchdata() {
        if (upcoming_list.size()>0)
            upcoming_list.clear();
        databaseRef.child(firebaseAuth.getCurrentUser().getUid())
                //folder for the data
                .child("task_list").orderByChild("completed_by").startAt(nextDays()).
                addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Task task = dataSnapshot.getValue(Task.class);
                        upcoming_list.add(task);
                        Log.e("dataSnapshot", String.valueOf(upcoming_list));
                        updateListView();
                    }
                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    }
                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                    }
                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                //checks if the task has been completed
                //.orderByChild("completed").equalTo(false)
                /*.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            //displays the current list of tasks
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                task = snapshot.getValue(Task.class);
                                upcoming_list.add(task);
                                Log.e("dataSnapshot", String.valueOf(task));
                            }
                        }}
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });*/
    }

    private void updateListView() {
        adapter.setTasks(upcoming_list);
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.search_icon, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem search = menu.findItem(R.id.search);//search button
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
                // Set styles for expanded state here
           /*     if (((AppCompatActivity)getActivity()).getSupportActionBar() != null) {
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                }
           */     return true;
            }
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Set styles for collapsed state here
                if (((AppCompatActivity)getActivity()).getSupportActionBar() != null) {
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.Colorpigpink));
                }
                final FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content, Scheduler.newInstance());
                transaction.commit();
                //Toast.makeText(getContext(),tasks.size(),Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection)
        switch (item.getItemId()) {
            case R.id.edit_profile:
                //Intent intent =  new Intent();
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

    private String stri(){
        //for tomorrow
        Date date;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,1);
        date = cal.getTime();
        SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy");
        // long me = System.currentTimeMillis();
        String dt = spd.format(date);
        Log.i("present",dt);
        return dt;
    }


    private String nextDays(){
        //for today
        Date date;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,1);
        cal.add(Calendar.HOUR_OF_DAY, -1);
        cal.add(Calendar.MINUTE, 0);
        cal.add(Calendar.SECOND, 0);
        date = cal.getTime();
        long time = date.getTime();
        Log.i("present", String.valueOf(time));
        return String.valueOf(time);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(final String newText) {
        databaseRef.child(firebaseAuth.getCurrentUser().getUid()).child("task_list").
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //listView.invalidateViews();
                        Date present_date = null, assigned_date = null;
                        upcoming_list.clear();
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                            String date = String.valueOf(snapshot.child("date").getValue());
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH);
                            try {
                                assigned_date = formatter.parse(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            String name = String.valueOf(snapshot.child("content").getValue());
                            String today_date = stri();
                            try {
                                present_date = formatter.parse(today_date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (name.toLowerCase().contains(newText.toLowerCase())) {
                                if (assigned_date != null && assigned_date.after(present_date)) {
                                    Task task = snapshot.getValue(Task.class);
                                    if (upcoming_list.size() > 0)
                                        Toast.makeText(getContext(), "is available", Toast.LENGTH_SHORT).show();
                                        upcoming_list.add(task);
                                }
                            }
                        }
                        adapter.setFilter(upcoming_list);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
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
