package com.co.coller.college;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.co.coller.R;
import com.co.coller.college.schedule.JumatFragment;
import com.co.coller.college.schedule.KamisFragment;
import com.co.coller.college.schedule.MingguFragment;
import com.co.coller.college.schedule.RabuFragment;
import com.co.coller.college.schedule.SabtuFragment;
import com.co.coller.college.schedule.SelasaFragment;
import com.co.coller.college.schedule.SeninFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleFragment extends Fragment {

    BottomNavigationView day1Nav, day2Nav;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScheduleFragment newInstance(String param1, String param2) {
        ScheduleFragment fragment = new ScheduleFragment();
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
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        day1Nav = view.findViewById(R.id.day1Nav);
        day2Nav = view.findViewById(R.id.day2Nav);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentday_container, new SeninFragment()).commit();

        day1Nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()){
                    case R.id.senin:
                        selectedFragment = new SeninFragment();
                        break;
                    case R.id.selasa:
                        selectedFragment = new SelasaFragment();
                        break;
                    case R.id.rabu:
                        selectedFragment = new RabuFragment();
                        break;
                    case R.id.kamis:
                        selectedFragment = new KamisFragment();
                        break;
                }
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentday_container, selectedFragment).commit();

                return true;
            }
        });

        day2Nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()){
                    case R.id.jumat:
                        selectedFragment = new JumatFragment();
                        break;
                    case R.id.sabtu:
                        selectedFragment = new SabtuFragment();
                        break;
                    case R.id.minggu:
                        selectedFragment = new MingguFragment();
                        break;
                }
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentday_container, selectedFragment).commit();

                return true;
            }
        });
        return view;
    }
}