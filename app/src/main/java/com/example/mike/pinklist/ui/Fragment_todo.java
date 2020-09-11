    package com.example.mike.pinklist.ui;

    import android.app.AlertDialog;
    import android.app.SearchManager;
    import android.content.BroadcastReceiver;
    import android.content.Context;
    import android.content.DialogInterface;
    import android.graphics.Color;
    import android.graphics.Typeface;
    import android.net.Uri;
    import android.os.Build;
    import android.os.Bundle;
    import androidx.annotation.NonNull;
    import androidx.annotation.RequiresApi;
    import androidx.fragment.app.Fragment;
    import androidx.fragment.app.FragmentTransaction;
    import androidx.core.view.MenuItemCompat;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.recyclerview.widget.RecyclerView;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.Menu;
    import android.view.MenuInflater;
    import android.view.MenuItem;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.AdapterView;
    import android.widget.Button;
    import android.widget.CheckBox;
    import android.widget.EditText;
    import android.widget.ImageView;
    import android.widget.LinearLayout;
    import android.widget.ListView;
    import android.widget.SearchView;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.example.mike.pinklist.R;
    import com.example.mike.pinklist.adapters.TaskAdapter;
    import com.example.mike.pinklist.models.Store;
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
    import java.util.Date;
    import java.util.List;
    import java.util.Objects;
    import java.util.TimeZone;

    /**
     * A simple {@link Fragment} subclass.
     * Activities that contain this fragment must implement the
     * {@link Fragment_todo.OnFragmentInteractionListener} interface
     * to handle interaction events.
     * Use the {@link Fragment_todo#newInstance} factory method to
     * create an instance of this fragment.
     */
    public class Fragment_todo extends Fragment implements SearchView.OnQueryTextListener, AdapterView.OnItemLongClickListener{
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private static final String ARG_PARAM1 = "param1";
        private static final String ARG_PARAM2 = "param2";
        List<Task> tasklist = new ArrayList<>();
        public static List<String> task_position = new ArrayList<>();
        private ListView listView;
        CheckBox cb;
        EditText editText;
        Button button;
        boolean show;
        View view;
        RecyclerView recyclerView;
        FirebaseDatabase database;
        DatabaseReference databaseRef;
        FirebaseAuth firebaseAuth;
        Store store;
        BroadcastReceiver broadcastReceiver;
        private String mParam1;
        private String mParam2;
        TaskAdapter taskAdapter;
        private OnFragmentInteractionListener mListener;

        public Fragment_todo() {
            // Required empty public constructor
        }
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         *  param1 Parameter 1.
         *  param2 Parameter 2.
         * @return A new instance of fragment Fragment_todo.
         */
        // TODO: Rename and change types and number of parameters
        public static Fragment_todo newInstance() {
            Fragment_todo fragment = new Fragment_todo();
            //Bundle args = new Bundle();
            //args.putString(ARG_PARAM1, param1);
            //args.putString(ARG_PARAM2, param2);
            //fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);//enables fragments to populate menu items
            database = FirebaseDatabase.getInstance();
            firebaseAuth = FirebaseAuth.getInstance();
            databaseRef = database.getReference("users");
        }

        private void setUpReceivers() {
             /*broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Dispatcher.Broadcast broadcastType = (Dispatcher.Broadcast) intent.getExtras().get("broadcast_type");
                    final String message = intent.getExtras().getString("message");
                    assert broadcastType != null;
                    switch (broadcastType){
                        case ACTION_DELETE_ITEM:
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("Delete?");
                            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                   int position = Integer.parseInt(message);
                                    //deleteFromDatabase(position);
                                    Task task;
                                    task = tasklist.get(position);
                                    tasklist.remove(task);
                                    taskAdapter.notifyDataSetChanged();
                                    Toast.makeText(getContext(),task_position.get(position),Toast.LENGTH_SHORT).show();
                                }
                            });
                            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    alertDialog.dismiss();
                                    taskAdapter.setCheckBox();
                                }
                            });
                            alertDialog = builder.show();
                            break;
                        }
                    }
                };*/
             }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        //View for the to-do fragment
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.fragment_fragment_todo, container, false);
            TextView tv= (TextView) view.findViewById(R.id.messag);
            Typeface tp = Typeface.createFromAsset(getContext().getAssets(),"fonts/NotoSans-Bold.ttf");
            tv.setTypeface(tp);
            listView = (ListView)view.findViewById(R.id.listview);
            /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {*/
            listView.setOnItemLongClickListener(this);
          /*recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));*/
            /*((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.to_do);*/
            //setupAdapter();
            //if (taskAdapter == null)
            setupAdapter();
            fetchData();
            return view;
        }

        private void setupAdapter() {
            tasklist = new ArrayList<>();
            taskAdapter = new TaskAdapter(getContext(),tasklist);
            listView.setAdapter(taskAdapter);
        }

        private void fetchData() {
         /*   if (tasklist.size() > 0)
                tasklist.clear();
            if (task_position.size() >0)
                task_position.clear();
            String st = Integer.toString(startOfDay());
            String ed = Integer.toString(endOfDay());
            databaseRef.child(firebaseAuth.getCurrentUser().getUid()).child("task_list")
                    .orderByChild("date").equalTo(str())
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Log.e("datnapshot", String.valueOf(dataSnapshot));
                            Task task = dataSnapshot.getValue(Task.class);
                            tasklist.add(task);
                            task_position.add(dataSnapshot.getKey());
                            Log.d("checking", String.valueOf(dataSnapshot));
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
                    });*/
            String st = Integer.toString(startOfDay());
            String ed = Integer.toString(endOfDay());
            databaseRef.child(firebaseAuth.getCurrentUser().getUid()).child("task_list")
                    .orderByChild("date").equalTo(str())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (tasklist.size() > 0)
                                tasklist.clear();
                            if (task_position.size() >0)
                                task_position.clear();
                            Log.d("checks", String.valueOf(dataSnapshot));
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                Task task = dataSnapshot1.getValue(Task.class);
                                tasklist.add(task);
                                task_position.add(dataSnapshot1.getKey());
                                updateListView();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        }
        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.search_icon, menu);//menu xml file/**/
            super.onCreateOptionsMenu(menu, inflater);
            MenuItem search = menu.findItem(R.id.search);//search button
            SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
            //searchView.setMaxWidth(570);
            //searchView.setMinimumHeight(30);
            searchView.setIconifiedByDefault(false);//hides default search icon inside edittext
            //searchView.setBackgroundResource(R.drawable.button);// background color and shape for searchview
            int magId = getResources().getIdentifier("android:id/search_mag_icon",null,null);
            int search_plate = getResources().getIdentifier("android:id/search_plate",null,null);
            ImageView imageView = (ImageView) searchView.findViewById(magId);
            imageView.setImageDrawable(null);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(0,0));//removing search icon in hint
            View v = searchView.findViewById(search_plate);// making the underline transparent
            v.setBackgroundColor(Color.TRANSPARENT);
            searchView.setQueryHint("Search Task");
            searchView.setOnQueryTextListener(this);
            MenuItemCompat.setOnActionExpandListener(search, new MenuItemCompat.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    // Set styles for expanded state here
                    /*if (((AppCompatActivity)getActivity()).getSupportActionBar() != null) {
                        ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                    }*/
                    return true;
                }
                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    // Set styles for collapsed state here
                    if (((AppCompatActivity)getActivity()).getSupportActionBar() != null) {
                        ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.Colorpigpink));
                    }
                    final FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.content, Fragment_todo.newInstance());
                    transaction.commit();
                    return true;
                }
            });
        }
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            if (item.getItemId() == R.id.edit_profile) {
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
        //using this app you must try how to setup the single adapter for recycler/listview//weekend
        //also use search for suugestion adapter
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
        private static String str(){
            Calendar cal = Calendar.getInstance();
            DateFormat spd = new SimpleDateFormat("dd/MM/yyyy");
            long me = System.currentTimeMillis();
            String dt = spd.format(me);

           // Log.i("present", cal.getTime().toString());
            return dt;
        }

        private static String stri(Date date){
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE,1);
            date = cal.getTime();
            DateFormat spd = new SimpleDateFormat("dd/MM/yyyy");
            //long me = System.currentTimeMillis();
            String dt = spd.format(date);
            Log.i("present", cal.getTime().toString());
            return dt;
        }

        private void updateListView() {
            taskAdapter.setTasks(tasklist);
            taskAdapter.notifyDataSetChanged();
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

        public static int startOfDay() {
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            //12.00am
            cal.set(Calendar.HOUR_OF_DAY, -1); //set hours to zero
            cal.set(Calendar.MINUTE, 0); // set minutes to zero
            cal.set(Calendar.SECOND, 0); //set seconds to zero
            // Log.i("StartTime", cal.getTime().toString());
            Log.i("stime", String.valueOf(cal.getTimeInMillis()));
            return (int) cal.getTimeInMillis();
        }

        public static int endOfDay() {
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            //11:59pm
            cal.set(Calendar.HOUR_OF_DAY, 22);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            Log.i("EndTime", cal.getTime().toString());
            return (int) cal.getTimeInMillis();
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
        public boolean onQueryTextChange(final String newText) {
            databaseRef.child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).child("task_list").
                    addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //listView.invalidateViews();
                    tasklist.clear();
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        String date = String.valueOf(snapshot.child("date").getValue());
                        String name = String.valueOf(snapshot.child("content").getValue());
                        if (name.toLowerCase().contains(newText.toLowerCase())) {
                            if (date.equals(str())) {
                                Task task = snapshot.getValue(Task.class);
                                if (tasklist.size() > 0)
                                    Toast.makeText(getContext(),"is available",Toast.LENGTH_SHORT).show();
                                tasklist.add(task);
                            }
                        }
                    }
                    taskAdapter.setFilter(tasklist);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
          /*ArrayList<Task> tasklist = new ArrayList<>();
            if (tasklist.size() == 0)
                Log.d("arrays", "jfjti");
            String date = null;
            Task tasklists = store.getTask();
            for( Task task : tasklist){
                SimpleDateFormat formatter = new SimpleDateFormat("MM/yy", Locale.ENGLISH);
                SimpleDateFormat dateformate = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.ENGLISH);
                try {
                    Date dat = formatter.parse(task.getDate());//converts string date to normal time
                    date = dateformate.format(dat);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(date != null && date.contains(nText)) {
                    tasklist.add(task);
                }
                String content = task.getContent().toLowerCase();
                if(content.contains(nText)){// if text in newText
                    Toast.makeText(getContext(),"here",Toast.LENGTH_SHORT).show();
                    tasklist.add(task);
                }
            }
            Toast.makeText(getContext(),"hete",Toast.LENGTH_SHORT).show();

       */     return false;
        }

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            Task task =  tasklist.get(position);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            String[] options = new String[2];
            if ( task.getContent().length() > 15 ){
                options[0] = "Edit  " + task.getContent().substring(0,10) + "...";
                options[1] = "Delete  " + task.getContent().substring(0,10) + "...";
            }else {
                options[0] = "Edit " + task.getContent();
                options[1] = "Delete " + task.getContent();
            }
            alertDialogBuilder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        setEditText(position);
                    } else if (which == 1) {
                        deleteItem(position);
                    }
                }
            });
            alertDialogBuilder.create().show();
            return false;
        }

        private void setEditText(final int position){
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View v = inflater.inflate(R.layout.task_details, null);
            editText = (EditText) v.findViewById(R.id.text);
            button = (Button) v.findViewById(R.id.buttons);
            editText.setText(tasklist.get(position).getContent());
            editText.setSelection(tasklist.get(position).getContent().length());
            final AlertDialog alertDialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setView(v);
            alertDialog = builder.create();
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    databaseRef.child(firebaseAuth.getCurrentUser().getUid())
                            .child("task_list").child(task_position.get(position)).child("content").setValue
                            (editText.getText().toString());
                                final FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                                        .beginTransaction();
                                transaction.replace(R.id.content, Fragment_todo.newInstance());
                                transaction.commit();
                                if(alertDialog.isShowing()){
                                    alertDialog.dismiss();
                                }
                            }
            });
            alertDialog.show();
        }

        private void deleteItem(final int position){
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Delete?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    deleteFromDatabase(position);
                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.create().show();
        }

        public void deleteFromDatabase(final int position) {
            int time = tasklist.get(position).getTime();
            databaseRef.child(firebaseAuth.getCurrentUser().getUid()).child("task_list").child(tasklist.get(position).getKey())
                    .setValue(null);
                    Task tasklists;
                    tasklists = tasklist.get(position);
                    tasklist.remove(tasklists);
                    taskAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(),task_position.get(position),Toast.LENGTH_SHORT).show();
        }

        public interface OnFragmentInteractionListener {
                // TODO: Update argument type and name
                void onFragmentInteraction(Uri uri);
            }
    }

