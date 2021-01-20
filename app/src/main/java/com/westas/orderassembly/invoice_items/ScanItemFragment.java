package com.westas.orderassembly.invoice_items;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.westas.orderassembly.MainActivity;
import com.westas.orderassembly.R;
import com.westas.orderassembly.barcode_reader.TOnReadBarcode;
import com.westas.orderassembly.calculator.ParseBarcode;
import com.westas.orderassembly.calculator.QRCode;
import com.westas.orderassembly.rest_service.TOnResponceChekItem;
import com.westas.orderassembly.rest_service.TResponceOfChekItem;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScanItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScanItemFragment extends Fragment implements TOnReadBarcode, TOnResponceChekItem {

    private ParseBarcode parseBarcode;
    private TOnSuccessSearchBarcode OnSuccessSearchBarcode;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ScanItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScanItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScanItemFragment newInstance(String param1, String param2) {
        ScanItemFragment fragment = new ScanItemFragment();
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

        parseBarcode = new ParseBarcode();
        MainActivity.GetBarcodeReader().SetListren(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan_item, container, false);
    }

    @Override
    public void OnReadCode(String code) {
        QRCode qr_code = null;
        try
        {
            qr_code = parseBarcode.ParseJSON(code);
        }
        catch(Exception e)
        {
            getActivity().runOnUiThread(() ->Toast.makeText(getActivity(), "Не удалось прочитать QRCode!", Toast.LENGTH_SHORT).show());
        }

        if (qr_code == null)
        {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), "Не удалось прочитать QRCode!", Toast.LENGTH_SHORT).show();
                }
            });
        }


        SearchItem(qr_code.code);
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
}