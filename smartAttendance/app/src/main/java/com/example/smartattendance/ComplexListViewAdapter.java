package com.example.smartattendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ComplexListViewAdapter extends BaseAdapter {

    private ArrayList<ComplexListViewItem> complexListViewItem = new ArrayList<>();

    @Override
    public int getCount() {
        return complexListViewItem.size();
    }

    @Override
    public Object getItem(int position) {
        return complexListViewItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Context context = parent.getContext();
        if (convertView == null) {

            //inflater 는 다른 xml파일을 view에 담아 참조하기 위해서 메모리에 올려놓는 과정이다.
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_complex, parent, false);
        }

        TextView listViewDate_tv = (TextView) convertView.findViewById(R.id.listview_date_tv) ;
        TextView listViewAttend_tv = (TextView) convertView.findViewById(R.id.listview_attend_tv) ;
        TextView listViewOffWork_tv = (TextView) convertView.findViewById(R.id.listview_offwork_tv) ;


        /* 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용 */
        ComplexListViewItem item = (ComplexListViewItem)getItem(position);

        listViewDate_tv.setText(item.getDay());
        listViewAttend_tv.setText(item.getAttendTime());
        listViewOffWork_tv.setText(item.getOffWorkTime());

        return convertView;
    }

    public void addItem(String day, String attend, String offwork){
        ComplexListViewItem item = new ComplexListViewItem();

        item.setDay(day);
        item.setAttendTime(attend);
        item.setOffWorkTime(offwork);

        complexListViewItem.add(item);
    }
    public void ItemClear(){
        complexListViewItem.clear();
    }
}
