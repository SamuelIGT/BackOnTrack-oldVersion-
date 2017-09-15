package br.ufc.quixada.backontrack.Fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufc.quixada.backontrack.R;
import br.ufc.quixada.backontrack.StorageController;
import br.ufc.quixada.backontrack.activities.ExerciseActivity;
import br.ufc.quixada.backontrack.adapter.ExpandableListAdapter;
import br.ufc.quixada.backontrack.model.Exercise;
import br.ufc.quixada.backontrack.model.Level;
import br.ufc.quixada.backontrack.model.Section;
import br.ufc.quixada.backontrack.model.User;

import static android.app.Activity.RESULT_OK;

/**
 * Created by samue on 22/07/2017.
 */

public class SectionsFragment extends Fragment {
    //Constructor
    public SectionsFragment() {
    }

    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private Map<String, List<String>> listDataChild;
    int previousGroup = -1;
    private View rootView;
    private StorageController storage = new StorageController();

    //TESTING PURPOSE
    User user = new User(1);
    List<Level> levels;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.exercise_fragment, container, false);
        expListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);
        //Prepare list data
        loadData();
        setupLockers();
        //-------------------------------------------------------------------------

        //Bind list
        listAdapter = new ExpandableListAdapter(rootView.getContext(), listDataHeader, listDataChild /*, user.getLevelPermission()*/, levels);
        expListView.setAdapter(listAdapter);

        FnClickEvents(rootView);
        return rootView;
    }

    private void loadData() {
        //Load the List<Level> object from the local database (sharedPreferences)
        levels = storage.getLevels(getString(R.string.LEVELS_KEY), rootView.getContext());

        if (levels != null) {
            Log.v("LOAD_DATA", "levels not null");
            listDataHeader = new ArrayList<String>();   //Prepares the headers(Level) titles
            listDataChild = new HashMap<String, List<String>>();    //Prepares the child(Section) titles

            // Adding header(Level) titles
            for (Level level : levels) {
                listDataHeader.add("Nível " + level.getLevel());
            }
            //Create a List of the type that the expandableListView require
            List<List<String>> headersSectionsList = new ArrayList<>();

            // Adding section titles on the List that will be added on the expandable list view
            for (int i = 0; i < levels.size(); i++) {
                headersSectionsList.add(new ArrayList<String>());
                for (Section section : levels.get(i).getSectionsList()) {
                    Log.v("isSectionUnlocked", "Level " + levels.get(i).getLevel() + ": " + section.isUnlocked());
                    headersSectionsList.get(i).add(section.getTitle());
                }
            }
            // Adding the Child(Section) titles
            for (int i = 0; i < listDataHeader.size(); i++) {
                listDataChild.put(listDataHeader.get(i), headersSectionsList.get(i));
            }
        } else {
            createSections();
        }
    }

    public void setupLockers() {
        boolean hasChanges = false;
        for (int i = 0; i < levels.size(); i++) {
            if (i > 0) {
                if (levels.get(i - 1).isLevelCompleted()) {
                    levels.get(i).setUnlocked(true);
                    if (!hasChanges)
                        hasChanges = true;
                }
            }

            for (int j = 1; j < levels.get(i).getSectionsList().size(); j++) {
                Log.v("UNLOCK_SECTION", "FOR ENTERED: Section "+ levels.get(i).getSectionsList().get(j - 1).getTitle()+" is "+levels.get(i).getSectionsList().get(j - 1).isSectionCompleted());
                if (levels.get(i).getSectionsList().get(j - 1).isSectionCompleted()) {
                    Log.v("UNLOCK_SECTION", "Level " + levels.get(i).getLevel() + ":" + levels.get(i).getSectionsList().get(j).getTitle() + " UNLOCKED");
                    levels.get(i).getSectionsList().get(j).setUnlocked(true);
                    if (!hasChanges)
                        hasChanges = true;
                }
            }
        }
        if (hasChanges)
            saveData();

    }

    void FnClickEvents(final View thisView) {
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            //Header Click
            //if the onGroupClick returns true, it will hold the click(not expand). If it returns false, the the group will be expanded.
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
                Log.v("LevelID", "" + groupPosition);

                if (levels.get(groupPosition).isUnlocked()) {
                    return false;
                } else {
                    return true;
                }
            }
        });

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            //Listening to child item selection
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {

                Intent exerciseIntent = new Intent(getActivity(), ExerciseActivity.class);

                switch (groupPosition) {
                    case 0:
                        exerciseIntent.putExtra("LEVEL", levels.get(0).getSectionsList().get(childPosition));
                    case 1:
                        exerciseIntent.putExtra("LEVEL", levels.get(1).getSectionsList().get(childPosition));
                    case 2:
                        exerciseIntent.putExtra("LEVEL", levels.get(2).getSectionsList().get(childPosition));
                }
                startActivityForResult(exerciseIntent, 1);


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

    private void createSections() {
        //User
        user = new User(1);

        //Levels
        levels = new ArrayList<>();
        levels.add(new Level(1, new ArrayList<Section>()));
        levels.add(new Level(2, new ArrayList<Section>()));
        levels.add(new Level(3, new ArrayList<Section>()));

        //Sections
        Section alg = new Section(1, "Alongamento", new ArrayList<Exercise>(), true);
        Section fortB = new Section(2, "Fortalecimento do braço", new ArrayList<Exercise>(), false);
        Section fortM = new Section(3, "Fortalecimento da mão", new ArrayList<Exercise>(), false);
        Section coord = new Section(4, "Cordenação", new ArrayList<Exercise>(), false);
        Section habiM = new Section(5, "Habilidades manuais", new ArrayList<Exercise>(), false);

        List<Integer> audioSteps = new ArrayList<Integer>();
        audioSteps.add(R.raw.alng_tt_br_1);
        audioSteps.add(R.raw.alng_tt_br_2);
        audioSteps.add(R.raw.alng_tt_br_3);
        audioSteps.add(R.raw.alng_tt_br_4);
        audioSteps.add(R.raw.alng_tt_br_5);
        audioSteps.add(R.raw.alng_tt_br_6);


        //Exercises
        Exercise ex1 = new Exercise(1, "Total Arm Stretch", "Sit straight in your chair and lean forward over your knees.", R.drawable.thumbnail_1, R.raw.video_sample, true, audioSteps);
        Exercise ex2 = new Exercise(2, "Shoulder Shrug", "Sit down a chair with your arms by your side.", R.drawable.thumbnail_2, R.raw.video_sample, false, audioSteps);
        Exercise ex3 = new Exercise(3, "Push Ups", "Place the table against a wall", R.drawable.thumbnail_3, R.raw.video_sample, true, audioSteps);
        Exercise ex4 = new Exercise(4, "One Arm Push-Ups", "Place your weaker hand flat on the table. Use your stronger hand to help keep your hand in place.", R.drawable.thumbnail_4, R.raw.video_sample, false, audioSteps);
        Exercise ex5 = new Exercise(5, "Grip Power", "Place your weaker arm on the table.", R.drawable.thumbnail_5, R.raw.video_sample, true, audioSteps);
        Exercise ex6 = new Exercise(6, "Finger Power", "Place the putty on the table and roll into a thick rope. Use your weaker hand as much as possible.", R.drawable.thumbnail_6, R.raw.video_sample, false, audioSteps);
        Exercise ex7 = new Exercise(7, "Waiter– Ball", "Place the bean bag in your weaker hand.", R.drawable.thumbnail_7, R.raw.video_sample, true, audioSteps);
        Exercise ex8 = new Exercise(8, "Waiter– Cup", "Place a cup in your weaker hand.", R.drawable.thumbnail_8, R.raw.video_sample, false, audioSteps);
        Exercise ex9 = new Exercise(9, "Laundry", "Use both hands for the following exercise.", R.drawable.thumbnail_9, R.raw.video_sample, true, audioSteps);
        Exercise ex10 = new Exercise(10, "Buttons", "Take a shirt with buttons out of your closet.", R.drawable.thumbnail_10, R.raw.video_sample, false, audioSteps);

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

        levels.get(0).setUnlocked(true);


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

        List<String> lstTwo = new ArrayList<String>();
        for (Section sec : levels.get(1).getSectionsList()) {
            lstTwo.add(sec.getTitle());
        }

        List<String> lstThree = new ArrayList<String>();
        for (Section sec : levels.get(2).getSectionsList()) {
            lstThree.add(sec.getTitle());
        }

        // Header, Child data
        listDataChild.put(listDataHeader.get(0), lstOne);
        listDataChild.put(listDataHeader.get(1), lstTwo);
        listDataChild.put(listDataHeader.get(2), lstThree);

        saveData();
    }

    private void saveData() {
        storage.saveLevels(getString(R.string.LEVELS_KEY), levels, rootView.getContext());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Log.v("ACTIVITY_RESULT", "EXPECTED CODE: " + RESULT_OK + " | GIVEN CODE: " + resultCode);
            if (resultCode == RESULT_OK) {
                Log.v("ACTIVITY_RESULT", "ENTERED3");
                if (data.getBooleanExtra("HAS_CHANGE", false)) {
                    Log.v("ACTIVITY_RESULT", "ENTERED4");
                    loadData();
                    setupLockers();
                    ((BaseAdapter)expListView.getAdapter()).notifyDataSetChanged();//NOT WORKING
                }
            }
        }
    }

    /*
    private void restoreSession(Bundle savedInstanceState){
        SharedPreferences settings = this.getSharedPreferences(getString(R.string.restoreSessionMainActivity), 0);

        //Restores the session
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            String userSerialization = savedInstanceState.getString(getString(R.string.restoreStateMainActivity));
            userControl.setUsuario(JsonSerializator.jsonBuilder.fromJson(userSerialization, Usuario.class));
        } else if(!settings.getString("Usuario", "PREFERENCE NAO EXISTE").equals("PREFERENCE NAO EXISTE")) {
            String userSerialization = settings.getString("Usuario", "PREFERENCE NAO EXISTE" *//*valor default caso a preference nao exista*//*);
            userControl.setUsuario(JsonSerializator.jsonBuilder.fromJson(userSerialization, Usuario.class));
        }
    }

    private void saveSession(){
        SharedPreferences settings = this.getSharedPreferences(getString(R.string.restoreSessionMainActivity), 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("Usuario", JsonSerializator.jsonBuilder.toJson(userControl.getUsuario()));
        // Commit the edits!
        editor.commit();
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putString(getString(R.string.restoreStateMainActivity), JsonSerializator.jsonBuilder.toJson(userControl.getUsuario()));
        Log.v("onSave", "onSaveInstaceState CALLED");

    }*/

}