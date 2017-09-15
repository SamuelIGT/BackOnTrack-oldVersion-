package br.ufc.quixada.backontrack.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import br.ufc.quixada.backontrack.R;
import br.ufc.quixada.backontrack.model.Level;
import br.ufc.quixada.backontrack.model.Section;

/**
 * Created by samue on 23/07/2017.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; //header titles
    // child data in format of header title, child title
    private Map<String, List<String>> _listDataChild;
    LayoutInflater inflater;
    private int _levelPermission;
    private List<Boolean> levelPermissionList;
    private List<Level> _levels;


    public ExpandableListAdapter(Context context, List<String> listDataHeader, Map<String, List<String>> listDataChild, /*int levelPermission*/ List<Level> levels) {
        _context = context;
        _listDataHeader = listDataHeader;
        _listDataChild = listDataChild;
        inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //_levelPermission = levelPermission;
        _levels = levels;
        this.levelPermissionList = levelPermissionList;
    }

    //for child item view
    @Override
    public int getGroupCount() {
        return _listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return _listDataChild.get(_listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return _listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return _listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosition);
    }

    //Returns a ID based on position of the root list (childPosition)
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //Returns a ID based on position of the list item  (childPosition)
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        convertView = (convertView != null) ? convertView : inflater.inflate(R.layout.custom_expandable_listview_header, null); //operação térnaria
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        ImageView imgLock = (ImageView) convertView.findViewById(R.id.img_lock);
        //LinearLayout layoutHeader = (LinearLayout)convertView.findViewById(R.id.layout_listHeader);

        if(_levels.get(groupPosition).isUnlocked()){
            imgLock.setImageResource(R.color.transparent);
        }else{
            imgLock.setImageResource(R.drawable.ic_lock);
        }
        lblListHeader.setText(headerTitle);


        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //converts 8dp to px
        int px = (int) Math.ceil(8 * convertView.getContext().getResources().getDisplayMetrics().density);
        //if is expanded, removes the margin bottom
        //if is not expanded, sets a margin bottom
        if(isExpanded){
            lp.setMargins(0, 0, 0, 0);

            LinearLayout header = (LinearLayout) convertView.findViewById(R.id.layout_header);
            header.setLayoutParams(lp);
        }else{
            lp.setMargins(0, 0, 0, px);

            LinearLayout header = (LinearLayout) convertView.findViewById(R.id.layout_header);
            header.setLayoutParams(lp);
        }

/*        //Makes the list header being in front
        convertView.bringToFront();
        layoutHeader.bringToFront();
        layoutHeader.bringChildToFront(imgLock);
        layoutHeader.bringChildToFront(lblListHeader);
        layoutHeader.requestLayout();
        layoutHeader.invalidate();*/

        return convertView;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String childText = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_expandable_listview_list_item, null);
        }

        ImageView lock = (ImageView) convertView.findViewById(R.id.view_child_list_lock);
        TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
        txtListChild.setText(childText);
        Log.d("GetChildView",""+groupPosition + ", "+childPosition);
        if(_levels.get(groupPosition).getSectionsList().get(childPosition).isUnlocked()){
            Log.d("GetChildView","Level "+_levels.get(groupPosition).getSectionsList().get(childPosition)+" Unlocked");
            lock.setVisibility(View.GONE);
        }

        /*//Sets space between child items
        if (childPosition == groups.get(groupPosition).getChilds().size() - 1) {
            convertView.setPadding(0, 0, 0, 40);
        } else
            convertView.setPadding(0, 0, 0, 0);*/
/*        parent.bringToFront();
        Animation slideAnim = AnimationUtils.loadAnimation(convertView.getContext(), R.anim.move_down);
        slideAnim.setZAdjustment(Animation.ZORDER_BOTTOM);
        convertView.startAnimation(slideAnim);*/
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /*class ViewHolderItem extends Object {
    }*/
}