package com.example.foodies.ui.event;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.foodies.R;
import com.example.foodies.ViewEventActivity;
import com.example.foodies.structures.Event;
import com.example.foodies.ui.dummy.DummyContent;
import com.example.foodies.ui.dummy.DummyContent.DummyItem;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class EventsListFragment extends Fragment {

    // TODO: Customize parameters
    private OnListFragmentInteractionListener mListener;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView eventsList;
    private FirestoreRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventsListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static EventsListFragment newInstance(int columnCount) {
        EventsListFragment fragment = new EventsListFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events_list_list, container, false);
        eventsList = view.findViewById(R.id.events_list);
        linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        eventsList.setLayoutManager(linearLayoutManager);
        Query query = db.collection("events").limit(20).orderBy("date");

        FirestoreRecyclerOptions<Event> response = new FirestoreRecyclerOptions.Builder<Event>()
                .setQuery(query, Event.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Event, EventsHolder>(response) {
            @Override
            public void onBindViewHolder(EventsHolder holder, final int position, Event model) {
                //progressBar.setVisibility(View.GONE);
                holder.textName.setText(model.getName());
                holder.textDescription.setText(model.getDescription());
                Date date;
                if (model.getDate() == null) {
                    date = Timestamp.now().toDate();
                } else {
                    date = model.getDate().toDate();
                }
                String dateString = DateFormat.format("dd-MM-yyyy hh:mm", date).toString();
                holder.textDate.setText(dateString);
                Glide.with(getActivity().getApplicationContext())
                        .load(model.getImageUrl())
                        .into(holder.imageView);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity().getBaseContext(), ViewEventActivity.class);
                        intent.putExtra("event_id", getSnapshots().getSnapshot(position).getId());
                        startActivity(intent);
                    }
                });
            }
            @Override
            public EventsHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.fragment_events_list, group, false);
                return new EventsHolder(view);
            }
            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        adapter.notifyDataSetChanged();

        eventsList.setAdapter(adapter);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Event event);
    }

    public class EventsHolder extends RecyclerView.ViewHolder {

        public final View mView;

        public final TextView textName;
        public final TextView textDescription;
        public final TextView textDate;
        public final CircleImageView imageView;

        public EventsHolder(View view) {
            super(view);
            mView = view;
            textName = view.findViewById(R.id.event_name);
            textDescription = view.findViewById(R.id.event_description);
            textDate = view.findViewById(R.id.event_date);
            imageView = view.findViewById(R.id.event_image);
        }

    }

}
