package com.example.zhangyu.foodtruck;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.zhangyu.foodtruck.db_Layout.Dao_Sqlite;
import com.example.zhangyu.foodtruck.entities.Event;
import com.example.zhangyu.foodtruck.entities.Vender;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 *
 */
public class VendorsFragmentTab extends Fragment {
    ListView vendor_ListView;
    private Dao_Sqlite dao_sqlite;
    private List<Vender> venders = new ArrayList<Vender>();
    private VendorAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_vendors_fragment_tab, container, false);

        vendor_ListView = (ListView) rootView.findViewById(R.id.vendors_ListView);

        dao_sqlite = new Dao_Sqlite(getActivity());

        dao_sqlite.addVendorsToArrayList(venders);

        setupEventsListView();

        return rootView;
    }

    private void setupEventsListView() {
        adapter = new VendorAdapter(getActivity(), R.layout.vendor_item, venders);
        vendor_ListView.setAdapter(adapter);

        // click the contact item to show the profile view. Added by Yu Zhang
        vendor_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                // TODO Auto-generated method stub
                Vender event = venders.get(position);

                // show the profile of the contact.
                Intent intent = new Intent(getActivity(), VenderActivity.class);

                intent.putExtra("name", event.getName());
                startActivity(intent);
            }
        });
    }
}
