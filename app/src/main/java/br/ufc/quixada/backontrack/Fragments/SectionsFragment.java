package br.ufc.quixada.backontrack.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufc.quixada.backontrack.R;
import br.ufc.quixada.backontrack.adapter.ExpandableListAdapter;

/**
 * Created by samue on 22/07/2017.
 */

public class SectionsFragment extends Fragment {
    //Contructor
    public SectionsFragment() {
    }

    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    List<String> listDataHeader;
    Map<String, List<String>> listDataChild;
    int previousGroup = -1;
    private View rootView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.exercise_fragment, container, false);
        expListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);

        //Prepare list data
        prepareSections();

        //Bind list
        listAdapter = new ExpandableListAdapter(rootView.getContext(), listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        FnClickEvents();
        return rootView;
    }

    void FnClickEvents() {
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            //Listening to child item selection
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                Toast.makeText(rootView.getContext(), "OnClickChild", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        //Listening to group expand
        // modified so that on selection of one group other opened group has been closed
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup)
                    expListView.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });
        //Listening to group collapse
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {
                Toast.makeText(rootView.getContext(), "OnClickGroupCollapse", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void prepareSections() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        // Adding child data
        listDataHeader.add("Computer science");
        listDataHeader.add("Electrocs & comm.");
        listDataHeader.add("Mechanical");

        // Adding child data
        List<String> lstCS = new ArrayList<String>();
        lstCS.add("Data structure");
        lstCS.add("C# Programming");
        lstCS.add("Java programming");
        lstCS.add("ADA");
        lstCS.add("Operation reserach");
        lstCS.add("OOPS with C");
        lstCS.add("C++ Programming");

        List<String> lstEC = new ArrayList<String>();
        lstEC.add("Field Theory");
        lstEC.add("Logic Design");
        lstEC.add("Analog electronics");
        lstEC.add("Network analysis");
        lstEC.add("Micro controller");
        lstEC.add("Signals and system");

        List<String> lstMech = new ArrayList<String>();
        lstMech.add("Instrumentation technology");
        lstMech.add("Dynamics of machinnes");
        lstMech.add("Energy engineering");
        lstMech.add("Design of machine");
        lstMech.add("Turbo machine");
        lstMech.add("Energy conversion");

        // Header, Child data
        listDataChild.put(listDataHeader.get(0), lstCS);
        listDataChild.put(listDataHeader.get(1), lstEC);
        listDataChild.put(listDataHeader.get(2), lstMech);
    }
}
