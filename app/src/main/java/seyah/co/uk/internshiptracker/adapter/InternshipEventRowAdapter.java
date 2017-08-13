package seyah.co.uk.internshiptracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import seyah.co.uk.internshiptracker.R;
import seyah.co.uk.internshiptracker.pojo.ApplicationStatus;
import seyah.co.uk.internshiptracker.pojo.Internship;
import seyah.co.uk.internshiptracker.pojo.InternshipEvent;

public class InternshipEventRowAdapter extends BaseAdapter {

    private static ArrayList<InternshipEvent> internshipArrayList;

    private LayoutInflater mInflater;

    public InternshipEventRowAdapter(Context context, ArrayList<InternshipEvent> results) {
        internshipArrayList = results;
        mInflater = LayoutInflater.from(context);
    }
 
    public int getCount() {
        return internshipArrayList.size();
    }
 
    public Object getItem(int position) {
        return internshipArrayList.get(position);
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_internship_event_row, null);
            holder = new ViewHolder();
            holder.txtEventName = (TextView) convertView.findViewById(R.id.internship_event_name);
            holder.txtEventId = (TextView) convertView.findViewById(R.id.internship_event_id);
            holder.txtEventDescription = (TextView) convertView.findViewById(R.id.internship_event_description);
            holder.txtEventStatus = (TextView) convertView.findViewById(R.id.internship_event_status);
            holder.txtEventDate = (TextView) convertView.findViewById(R.id.internship_event_date);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
 
        holder.txtEventName.setText(internshipArrayList.get(position).getName());
        holder.txtEventId.setText(String.valueOf(internshipArrayList.get(position).getId()));
        holder.txtEventDescription.setText(internshipArrayList.get(position).getDescription());
        holder.txtEventStatus.setText(internshipArrayList.get(position).getStatus().toString());
        holder.txtEventDate.setText(internshipArrayList.get(position).getDate().toString());

        return convertView;
    }

    public void clear() {
        internshipArrayList.clear();
    }

    public void addAll(ArrayList<InternshipEvent> internships){
        internshipArrayList.addAll(internships);
    }
 
    static class ViewHolder {
        TextView txtEventName;
        TextView txtEventId;
        TextView txtEventStatus;
        TextView txtEventDescription;
        TextView txtEventDate;
    }
}