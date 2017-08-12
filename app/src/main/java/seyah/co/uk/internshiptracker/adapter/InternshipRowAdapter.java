package seyah.co.uk.internshiptracker.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import seyah.co.uk.internshiptracker.pojo.ApplicationStatus;
import seyah.co.uk.internshiptracker.R;
import seyah.co.uk.internshiptracker.pojo.Internship;

public class InternshipRowAdapter extends BaseAdapter {

    private static ArrayList<Internship> internshipArrayList;
 
    private LayoutInflater mInflater;
 
    public InternshipRowAdapter(Context context, ArrayList<Internship> results) {
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
            convertView = mInflater.inflate(R.layout.custom_internship_row, null);
            holder = new ViewHolder();
            holder.txtInternshipId = (TextView) convertView.findViewById(R.id.internship_row_id);
            holder.txtInternshipName = (TextView) convertView.findViewById(R.id.internship_row_name);
            holder.txtInternshipProgress = (TextView) convertView.findViewById(R.id.internship_row_progress);
 
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
 
        holder.txtInternshipId.setText(String.valueOf(internshipArrayList.get(position).getId()));
        holder.txtInternshipName.setText(internshipArrayList.get(position).getName());
        holder.txtInternshipProgress.setText(String.valueOf(ApplicationStatus.statusAsPercentage(internshipArrayList.get(position).getStatus())) + "%");
 
        return convertView;
    }

    public void clear() {
        internshipArrayList.clear();
    }

    public void addAll(ArrayList<Internship> internships){
        internshipArrayList.addAll(internships);
    }
 
    static class ViewHolder {
        TextView txtInternshipId;
        TextView txtInternshipName;
        TextView txtInternshipProgress;
    }
}