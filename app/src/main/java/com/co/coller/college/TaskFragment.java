package com.co.coller.college;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.co.coller.R;
import com.co.coller.adapter.noteAdapter;
import com.co.coller.adapter.taskAdapter;
import com.co.coller.api.api;
import com.co.coller.api.apiClient;
import com.co.coller.api.sharedPref;
import com.co.coller.model.note;
import com.co.coller.model.task;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskFragment extends Fragment {

    public static final String ID = "id_task";
    public static final String JENIS = "jenis";
    public static final String TANGGAL = "tanggal";
    public static final String DETAIL = "detail_task";

    RecyclerView rvTask;
    ArrayList<task> listTask;
    com.co.coller.api.api api;
    com.co.coller.api.sharedPref sharedPref;
    taskAdapter adapter;
    TextView addTask;

    private View.OnClickListener onItemClicklistener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            task thisitem = listTask.get(position);

            Intent intent = new Intent(getContext(), AddTaskActivity.class);
            intent.putExtra(ID, thisitem.getIdTask());
            intent.putExtra(JENIS, thisitem.getIdJenis());
            intent.putExtra(TANGGAL, thisitem.getTglDdline());
            intent.putExtra(DETAIL, thisitem.getDetailTask());
            startActivity(intent);
        }
    };


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TaskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TaskFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TaskFragment newInstance(String param1, String param2) {
        TaskFragment fragment = new TaskFragment();
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
        View view = inflater.inflate(R.layout.fragment_task, container, false);

        sharedPref = new sharedPref(view.getContext());
        rvTask = view.findViewById(R.id.rvTask);
        addTask = (TextView) view.findViewById(R.id.add_task);

        getTask();

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), AddTaskActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        return view;
    }

    private void getTask() {
        final String uid = sharedPref.getUid();
        api = apiClient.getClient().create(api.class);
        Call<List<task>> getTask = api.getTask(uid);

        getTask.enqueue(new Callback<List<task>>() {
            @Override
            public void onResponse(Call<List<task>> call, Response<List<task>> response) {
                listTask = new ArrayList<>(response.body());
                adapter = new taskAdapter(listTask);
                rvTask.setAdapter(adapter);
                rvTask.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter.setOnItemClicklistener(onItemClicklistener);
            }

            @Override
            public void onFailure(Call<List<task>> call, Throwable t) {

            }
        });
    }
}