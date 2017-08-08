package br.ufc.quixada.backontrack.Fragments;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufc.quixada.backontrack.R;
import br.ufc.quixada.backontrack.activities.ExerciseActivity;
import br.ufc.quixada.backontrack.adapter.ExpandableListAdapter;
import br.ufc.quixada.backontrack.model.Exercise;
import br.ufc.quixada.backontrack.model.Level;
import br.ufc.quixada.backontrack.model.Section;
import br.ufc.quixada.backontrack.model.User;

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

    //TESTING PURPOSE
    User user;
    List<Level> levels;
//    Level lOne, lTwo, lThree;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.exercise_fragment, container, false);
        expListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);


        //Prepare list data
        prepareSections();


        //-------------------------------------------------------------------------

        //Bind list
        listAdapter = new ExpandableListAdapter(rootView.getContext(), listDataHeader, listDataChild, user.getLevelPermission(), levels);

        expListView.setAdapter(listAdapter);
//        expListView.setDivider(null);
//        expListView.setDividerHeight(8);

        FnClickEvents(rootView);

        return rootView;
    }

    void FnClickEvents(final View thisView) {
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            //Header Click
            //if the onGroupClick returns true, it will hold the click(not expand). If it returns false, the the group will be expanded.
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                Log.v("LevelID", "" + i);

                switch (user.getLevelPermission()) {
                    case 1:
                        if (i == 0) {
                            break;
                        } else {
                            return true;
                        }
                    case 2:
                        if (i <= 1) {
                            break;
                        } else {
                            return true;
                        }
                    case 3:
                        if(i <= 2){
                            break;
                        }else{
                            return true;
                        }
                        default:
                            Log.v("USER_PERMISSION", "WRONG LEVEL PERMISSION");

                }
                return false;
            }
        });

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            //Listening to child item selection
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
                // switch ()

                Intent exerciseIntent = new Intent(getActivity(), ExerciseActivity.class);

                switch (groupPosition) {
                    case 0:
                        exerciseIntent.putExtra("LEVEL", levels.get(0).getSectionsList().get(childPosition));
                    case 1:
                        exerciseIntent.putExtra("LEVEL", levels.get(1).getSectionsList().get(childPosition));
                    case 2:
                        exerciseIntent.putExtra("LEVEL", levels.get(2).getSectionsList().get(childPosition));
                }
                getActivity().startActivity(exerciseIntent);


                //Working
//                fragmentTransaction=getFragmentManager().beginTransaction();
//                ExerciseActivity loginFragment = new ExerciseActivity();
//                fragmentTransaction.add(android.R.id.content,loginFragment,"exercise").commit();


                //Not working fully
                // Create new fragment and transaction
//                Fragment newFragment = new ExerciseActivity();
//                getFragmentManager().beginTransaction()
//                        .replace(R.id.container, newFragment,"EXCSE_FRGMNT")
//                        .addToBackStack(null)
//                        .commit();
            /*    // Create new fragment and transaction
                Fragment newFragment = new ExerciseActivity();
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
              /*  Toast.makeText(rootView.getContext(), "OnClickGroupCollapse", Toast.LENGTH_SHORT).show();*/

            }
        });
    }

    private void prepareSections() {
        //User
        user = new User(1);

        //Levels
        levels = new ArrayList<>();
        levels.add(new Level(1, new ArrayList<Section>()));
        levels.add(new Level(2, new ArrayList<Section>()));
        levels.add(new Level(3, new ArrayList<Section>()));

        /*lOne = );
        lTwo = ;
        lThree =;*/


        //Sections
        Section alg = new Section(1, "Alongamento", new ArrayList<Exercise>(), true);
        Section fortB = new Section(2, "Fortalecimento do braço", new ArrayList<Exercise>(), false);
        Section fortM = new Section(3, "Fortalecimento da mão", new ArrayList<Exercise>(), false);
        Section coord = new Section(4, "Cordenação", new ArrayList<Exercise>(), false);
        Section habiM = new Section(5, "Habilidades manuais", new ArrayList<Exercise>(), false);

        //Exercises
        Exercise ex1 = new Exercise(1, "Total Arm Stretch", "Sit straight in your chair and lean forward over your knees.", R.drawable.thumbnail_1, R.raw.video_sample, true);
        Exercise ex2 = new Exercise(1, "Shoulder Shrug", "Sitin a chair with your arms by your side.", R.drawable.thumbnail_2, R.raw.video_sample, false);
        Exercise ex3 = new Exercise(2, "Push Ups", "Place the table against a wall", R.drawable.thumbnail_3, R.raw.video_sample, true);
        Exercise ex4 = new Exercise(2, "One Arm Push-Ups", "Place your weaker hand flat on the table. Use your stronger hand to help keep your hand in place.", R.drawable.thumbnail_4, R.raw.video_sample, false);
        Exercise ex5 = new Exercise(3, "Grip Power", "Place your weaker arm on the table.", R.drawable.thumbnail_5, R.raw.video_sample, true);
        Exercise ex6 = new Exercise(3, "Finger Power", "Place the putty on the table and roll into a thick rope. Use your weaker hand as much as possible.", R.drawable.thumbnail_6, R.raw.video_sample, false);
        Exercise ex7 = new Exercise(4, "Waiter– Ball", "Place the bean bag in your weaker hand.", R.drawable.thumbnail_7, R.raw.video_sample, true);
        Exercise ex8 = new Exercise(4, "Waiter– Cup", "Place a cup in your weaker hand.", R.drawable.thumbnail_8, R.raw.video_sample, false);
        Exercise ex9 = new Exercise(5, "Laundry", "Use both hands for the following exercise.", R.drawable.thumbnail_9, R.raw.video_sample, true);
        Exercise ex10 = new Exercise(5, "Buttons", "Take a shirt with buttons out of your closet.", R.drawable.thumbnail_10, R.raw.video_sample, false);

        //Setting up the sections
        alg.addExerc(ex1);
        alg.addExerc(ex2);
        fortB.addExerc(ex3);
        fortB.addExerc(ex4);
        fortM.addExerc(ex5);
        fortM.addExerc(ex6);
        coord.addExerc(ex7);
        coord.addExerc(ex8);
        habiM.addExerc(ex9);
        habiM.addExerc(ex10);

        //Seeting up the Levels
        //LEVEL 1

        levels.get(0).addSec(alg);
        levels.get(0).addSec(fortB);
        levels.get(0).addSec(fortM);
        levels.get(0).addSec(coord);
        //LEVEL 2
        levels.get(1).addSec(alg);
        levels.get(1).addSec(fortB);
        levels.get(1).addSec(fortM);
        levels.get(1).addSec(coord);
        levels.get(1).addSec(habiM);
        //LEVEL 3
        levels.get(2).addSec(alg);
        levels.get(2).addSec(fortB);
        levels.get(2).addSec(fortM);
        levels.get(2).addSec(coord);
        levels.get(2).addSec(habiM);


        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Nível " + levels.get(0).getLevel());
        listDataHeader.add("Nível " + levels.get(1).getLevel());
        listDataHeader.add("Nível " + levels.get(2).getLevel());

        // Adding child data
        List<String> lstOne = new ArrayList<String>();
        for (Section sec : levels.get(0).getSectionsList()) {
            lstOne.add(sec.getTitle());
        }

       /* lstOne.add("Alongamento");
        lstOne.add("Fortalecimento do braço");
        lstOne.add("Fortalecimento da mão");
        lstOne.add("Cordenação");*/

        List<String> lstTwo = new ArrayList<String>();
        for (Section sec : levels.get(1).getSectionsList()) {
            lstTwo.add(sec.getTitle());
        }
       /* lstTwo.add("Alongamento");
        lstTwo.add("Fortalecimento do braço");
        lstTwo.add("Fortalecimento da mão");
        lstTwo.add("Cordenação");
        lstTwo.add("Habilidades manuais");*/


        List<String> lstThree = new ArrayList<String>();
        for (Section sec : levels.get(2).getSectionsList()) {
            lstThree.add(sec.getTitle());
        }
        /*lstThree.add("Alongamento");
        lstThree.add("Fortalecimento do braço");
        lstThree.add("Fortalecimento da mão");
        lstThree.add("Cordenação");
        lstThree.add("Habilidades manuais");*/

        // Header, Child data
        listDataChild.put(listDataHeader.get(0), lstOne);
        listDataChild.put(listDataHeader.get(1), lstTwo);
        listDataChild.put(listDataHeader.get(2), lstThree);
    }


}
