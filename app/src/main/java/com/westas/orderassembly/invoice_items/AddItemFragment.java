package com.westas.orderassembly.invoice_items;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.westas.orderassembly.MainActivity;
import com.westas.orderassembly.R;
import com.westas.orderassembly.rest_service.RestClient;
import com.westas.orderassembly.rest_service.TOnResponceChekItem;
import com.westas.orderassembly.rest_service.TResponceOfChekItem;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddItemFragment extends Fragment implements TOnResponceChekItem {

    private TOnSuccessSearchBarcode OnSuccessSearchBarcode;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddItemFragment newInstance(String param1, String param2) {
        AddItemFragment fragment = new AddItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof TOnSuccessSearchBarcode) {
            OnSuccessSearchBarcode = (TOnSuccessSearchBarcode) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement TOnSuccessSearchBarcode");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    private void SearchItem(String barcode_str) {
        MainActivity.rest_client.SetEventChekItems(this);
        MainActivity.rest_client.CheckItem(barcode_str);
    }

    @Override
    public void OnSuccessResponce(TResponceOfChekItem responce) {

        if (responce.success == true) {
            OnSuccessSearchBarcode.OnSuccessSearchBarcode(responce.item);
        }
        else {
            Toast.makeText(getActivity(), responce.message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void OnFailureResponce(Throwable t) {
        Toast.makeText(getActivity(), "Ошибка! "+ t.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_item, container, false);

        TextView text_view_barcode = view.findViewById(R.id.editTextBarcode);
        Button button_search = view.findViewById(R.id.button_search);

        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String barcode_str =  text_view_barcode.getText().toString();
                SearchItem(barcode_str);
            }
        });

        return view;
    }


}