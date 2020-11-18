package com.myapplicationdev.android.sustproj;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHistory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHistory extends Fragment {

    View v;
    AsyncHttpClient client;
    ArrayList<Prescription> al;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentHistory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentHistory.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentHistory newInstance(String param1, String param2) {
        FragmentHistory fragment = new FragmentHistory();
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
        v = inflater.inflate(R.layout.frag_history, container, false);
        client = new AsyncHttpClient();
        //Retrieve username and password
        RequestParams params = new RequestParams();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        params.add("username", sp.getString("username", ""));
        params.add("password", sp.getString("password", ""));

        //Set the Pending ListView
        ListView lv = v.findViewById(R.id.lv2);
        al = new ArrayList<>();
        client.post("http://10.0.2.2/sustproj/getOtherPrescriptions.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    for (int i=0; i<response.length(); i++) {
                        JSONObject obj = response.getJSONObject(i);
                        Prescription p = new Prescription(obj.getInt("prescription_id"), obj.getInt("quantity"), obj.getString("date"), obj.getString("bnf_code"), obj.getString("bnf_name"), obj.getString("vending_location"), obj.getDouble("act_cost"), obj.getString("status"));
                        al.add(p);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        AdapterHistory ca = new AdapterHistory(this.getContext(), R.layout.row_history, al);
        lv.setAdapter(ca);

        //Set PrescriptionDetailsActivity
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), PrescriptionDetailsActivity.class);
                intent.putExtra("prescription", al.get(i));
                startActivity(intent);
            }
        });
        return v;
    }
}