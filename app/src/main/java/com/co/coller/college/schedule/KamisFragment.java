package com.co.coller.college.schedule;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.co.coller.R;
import com.co.coller.adapter.scheduleAdapter;
import com.co.coller.api.api;
import com.co.coller.api.apiClient;
import com.co.coller.api.sharedPref;
import com.co.coller.college.AddNoteActivity;
import com.co.coller.model.schedule;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KamisFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KamisFragment extends Fragment {

    public static final String ID = "id_schedule";
    public static final String NAMA = "nama_schedule";
    public static final String JAM_MULAI = "waktu_mulai";
    public static final String JAM_BERAKHIR = "waktu_berakhir";
    public static final String HARI = "hari";

    RecyclerView rvKamis;
    scheduleAdapter adapter;
    ArrayList<schedule> listSchedule;
    api api;
    sharedPref sharedPref;
    Button btnNew;
    private View.OnClickListener onItemClicklistener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            schedule selectedItem = listSchedule.get(position);

            Intent intent = new Intent(getContext(), AddScheduleActivity.class);
            intent.putExtra(ID, selectedItem.getIdSchedule());
            intent.putExtra(NAMA, selectedItem.getNamaSchedule());
            intent.putExtra(JAM_MULAI, selectedItem.getWaktuMulai());
            intent.putExtra(JAM_BERAKHIR, selectedItem.getWaktuBerakhir());
            intent.putExtra(HARI, selectedItem.getHari());
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

    public KamisFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment KamisFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static KamisFragment newInstance(String param1, String param2) {
        KamisFragment fragment = new KamisFragment();
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
        View view = inflater.inflate(R.layout.fragment_kamis, container, false);

        sharedPref = new sharedPref(view.getContext());

        rvKamis = view.findViewById(R.id.rvKamis);
        btnNew = (Button) view.findViewById(R.id.btn_new_kamis);

        getMatkul();

        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), AddScheduleActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        return view;
    }

    private void getMatkul() {
        final String uid = sharedPref.getUid();
        final String hari = "Kamis";

        api = apiClient.getClient().create(api.class);
        Call<List<schedule>> getSchedule = api.getSchedule(uid, hari);

        getSchedule.enqueue(new Callback<List<schedule>>() {
            @Override
            public void onResponse(Call<List<schedule>> call, Response<List<schedule>> response) {
                listSchedule = new ArrayList<>(response.body());
                adapter = new scheduleAdapter(listSchedule);
                rvKamis.setAdapter(adapter);
                rvKamis.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter.setOnItemClicklistener(onItemClicklistener);
            }

            @Override
            public void onFailure(Call<List<schedule>> call, Throwable t) {
                Toast.makeText(getContext(),t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}