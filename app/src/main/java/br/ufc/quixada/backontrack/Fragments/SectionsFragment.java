package br.ufc.quixada.backontrack.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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

        FnClickEvents(rootView);
        return rootView;
    }

    void FnClickEvents(final View thisView) {
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            //Listening to child item selection
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

                // Create new fragment and transaction
                Fragment newFragment = new ExerciseFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, newFragment,"EXCSE_FRGMNT")
                        .addToBackStack(null)
                        .commit();
            /*    // Create new fragment and transaction
                Fragment newFragment = new ExerciseFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.container, newFragment, "EXCSE_FRGMNT");
                transaction.addToBackStack(null);
                // Commit the transaction
                transaction.commit();
*/
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
        listDataHeader.add("Nível 1");
        listDataHeader.add("Nível 2");
        listDataHeader.add("Nível 3");

        // Adding child data
        List<String> lstOne = new ArrayList<String>();
        lstOne.add("Alongamento");
        lstOne.add("Fortalecimento do braço");
        lstOne.add("Fortalecimento da mão");
        lstOne.add("Cordenação");

        List<String> lstTwo = new ArrayList<String>();
        lstTwo.add("Alongamento");
        lstTwo.add("Fortalecimento do braço");
        lstTwo.add("Fortalecimento da mão");
        lstTwo.add("Cordenação");
        lstTwo.add("Habilidades manuais");


        List<String> lstThree = new ArrayList<String>();
        lstThree.add("Alongamento");
        lstThree.add("Fortalecimento do braço");
        lstThree.add("Fortalecimento da mão");
        lstThree.add("Cordenação");
        lstThree.add("Habilidades manuais");

        // Header, Child data
        listDataChild.put(listDataHeader.get(0), lstOne);
        listDataChild.put(listDataHeader.get(1), lstTwo);
        listDataChild.put(listDataHeader.get(2), lstThree);
    }
}
