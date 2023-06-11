package cal.calculator;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Memory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Memory extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    SendEventListener sendEventListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            sendEventListener = (SendEventListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement SendEvent");
        }
    }

    String FILENAME = "";

    CustomDividerItemDecoration itemDecoration;
    private RecyclerViewAdapter rAdapter;
    ViewGroup defv;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Memory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Memory.
     */
    // TODO: Rename and change types and number of parameters
    public static Memory newInstance(String param1, String param2) {
        Memory fragment = new Memory();
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


        Bundle bundle = getArguments();
        ArrayList<String> al, el;
        al = bundle.getStringArrayList("key1");
        el = bundle.getStringArrayList("key2");
        String[] array = new String[al.size()];
        String[] array2 = new String[el.size()];
        int size = 0;
        for(String t : al){
            array[size++] = t;
        }
        size = 0;
        for(String k : el){
            array2[size++] = k;
        }


        defv = (ViewGroup) inflater.inflate(R.layout.fragment_memory, container, false);
        ImageButton cl_button = defv.findViewById(R.id.close_button);
        ImageButton tr_button = defv.findViewById(R.id.trash_button);
        cl_button.setOnClickListener(this);
        tr_button.setOnClickListener(this);
        RecyclerView rv = defv.findViewById(R.id.recView);

        rAdapter = new RecyclerViewAdapter(getActivity(), array, array2);

        rAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                sendEventListener.sendMessage(position);
            }
        });


        itemDecoration = new CustomDividerItemDecoration(10F, Color.DKGRAY);
        rv.addItemDecoration(itemDecoration);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);

        rv.setLayoutManager(layoutManager);
        rv.setAdapter(rAdapter);


        return defv;
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        String arr1[] = {"아직 기록이 없음"}, arr2[] = {"XX"};

        if(vId == R.id.close_button){
            FragmentManager manager = getActivity().getSupportFragmentManager();
            manager.beginTransaction().remove(Memory.this).commit();
            manager.popBackStack();
        }
        else if(vId == R.id.trash_button){
            RecyclerView rv = defv.findViewById(R.id.recView);
            rv.removeItemDecoration(itemDecoration);
            RecyclerViewAdapter cAdapter = new RecyclerViewAdapter(getActivity(), arr1, arr2);
            rv.setAdapter(cAdapter);
            sendEventListener.sendMessage(-1);


        }
    }
}