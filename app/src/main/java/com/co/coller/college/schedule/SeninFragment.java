package com.co.coller.college.schedule;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.co.coller.R;
import com.co.coller.adapter.scheduleSeninAdapter;
import com.co.coller.api.api;
import com.co.coller.api.apiClient;
import com.co.coller.api.sharedPref;
import com.co.coller.model.schedule;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SeninFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SeninFragment extends Fragment {

    RecyclerView rvSenin;
    scheduleSeninAdapter adapter;
    ArrayList<schedule> listSchedule;
    api api;
    sharedPref sharedPref;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SeninFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SeninFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SeninFragment newInstance(String param1, String param2) {
        SeninFragment fragment = new SeninFragment();
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
        View view = inflater.inflate(R.layout.fragment_senin, container, false);

        sharedPref = new sharedPref(view.getContext());

        rvSenin = view.findViewById(R.id.rvSenin);

        getMatkul();

        return view;
    }

    private void getMatkul() {
        final String uid = sharedPref.getUid();
        final String hari = "senin";

        api = apiClient.getClient().create(api.class);
        Call<List<schedule>> getSchedule = api.getSchedule(uid, hari);

        getSchedule.enqueue(new Callback<List<schedule>>() {
            @Override
            public void onResponse(Call<List<schedule>> call, Response<List<schedule>> response) {
                listSchedule = new ArrayList<>(response.body());
                adapter = new scheduleSeninAdapter(listSchedule);
                rvSenin.setAdapter(adapter);
                rvSenin.setLayoutManager(new LinearLayoutManager(getContext()));
            }

            @Override
            public void onFailure(Call<List<schedule>> call, Throwable t) {
                Toast.makeText(getContext(),t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}