package br.ufc.quixada.backontrack.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.Resource;

import java.util.Dictionary;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;

import br.ufc.quixada.backontrack.R;

/**
 * Created by samue on 23/07/2017.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; //header titles
    // child data in format of header title, child title
    private Map<String, List<String>> _listDataChild;
    LayoutInflater inflater;


    public ExpandableListAdapter(Context context, List<String> listDataHeader, Map<String, List<String>> listDataChild) {
        _context = context;
        _listDataHeader = listDataHeader;
        _listDataChild = listDataChild;
        inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_expandable_listview_list_item, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
        txtListChild.setText(childText);

/*//Sets space between child items
        if (childPosition == groups.get(groupPosition).getChilds().size() - 1) {
            convertView.setPadding(0, 0, 0, 40);
        } else
            convertView.setPadding(0, 0, 0, 0);*/
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /*class ViewHolderItem extends Object {
    }*/
}