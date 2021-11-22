package com.co.coller.college;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.co.coller.R;
import com.co.coller.adapter.noteAdapter;
import com.co.coller.api.api;
import com.co.coller.api.apiClient;
import com.co.coller.api.sharedPref;
import com.co.coller.model.note;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteFragment extends Fragment {

    public static final String JUDUL = "judul_note";
    public static final String BODY = "isi_note";
    public static final String ID = "id_note";

    RecyclerView rvNote;
    ProgressBar progressBar;
    noteAdapter adapter;
    ArrayList<note> listNote;
    api api;
    sharedPref sharedPref;
    Button addNote;
    private View.OnClickListener onItemClicklistener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            note thisitem = listNote.get(position);

            Intent intent = new Intent(getContext(), AddNoteActivity.class);
            intent.putExtra(JUDUL, thisitem.getJudulNote());
            intent.putExtra(BODY, thisitem.getIsiNote());
            intent.putExtra(ID, thisitem.getIdNote());
            startActivity(intent);
            //Toast.makeText(getContext(), "You Clicked: " + thisitem.getJudulNote(), Toast.LENGTH_SHORT).show();
        }
    };

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NoteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NoteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NoteFragment newInstance(String param1, String param2) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        sharedPref = new sharedPref(view.getContext());

        rvNote = view.findViewById(R.id.rvNote);
        progressBar = view.findViewById(R.id.progressBar);
        addNote = (Button) view.findViewById(R.id.btn_add_note);

        getNote();
        progressBar.setVisibility(View.VISIBLE);

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), AddNoteActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        return view;
    }

    private void getNote() {
        final String uid = sharedPref.getUid();
        api = apiClient.getClient().create(api.class);
        Call<List<note>> getNote = api.getNote(uid);

        getNote.enqueue(new Callback<List<note>>() {
            @Override
            public void onResponse(Call<List<note>> call, Response<List<note>> response) {
                listNote = new ArrayList<>(response.body());
                adapter = new noteAdapter(listNote);
                rvNote.setAdapter(adapter);
                rvNote.setLayoutManager(new GridLayoutManager(getContext(), 2));
                progressBar.setVisibility(View.GONE);
                adapter.setOnItemClicklistener(onItemClicklistener);
                //Toast.makeText(getContext(), "sukses", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<note>> call, Throwable t) {
                Toast.makeText(getContext(),t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}