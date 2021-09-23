package com.hostelfinder.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.hostelfinder.app.HostelInfo;
import com.hostelfinder.app.R;

import java.util.List;

public class HostelDashboardAdapter extends RecyclerView.Adapter<HostelDashboardAdapter.ViewHolder>{

    List<HostelInfo> hostelInfo;
    Context context;
    OnHostelDashboardClickListener onHostelDashboardClickListener;

    public HostelDashboardAdapter(Context context, List<HostelInfo> hostelInfo,
                                  OnHostelDashboardClickListener hostelDashboardClickListener) {
        this.hostelInfo = hostelInfo;
        this.context = context;
        this.onHostelDashboardClickListener = hostelDashboardClickListener;
    }

    @NonNull
    @Override
    public HostelDashboardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hostel_dashboard_item, parent, false);

        // Passing view to ViewHolder
        return new HostelDashboardAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HostelDashboardAdapter.ViewHolder holder, int position) {
        holder.bindView(hostelInfo.get(position));
    }

    @Override
    public int getItemCount() {
        return hostelInfo.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView infoIcon;
        private TextView infoLabel;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            infoIcon = itemView.findViewById(R.id.ivInfoIcon);
            infoLabel = itemView.findViewById(R.id.tvInfoLabel);
            itemView.setOnClickListener(view -> {
              onHostelDashboardClickListener.onClick(getAdapterPosition());
            });
        }

        public void bindView(HostelInfo hostelInfo){
            infoIcon.setImageDrawable(ContextCompat.getDrawable(context, hostelInfo.getIcon()));
            infoLabel.setText(hostelInfo.getName());
        }
    }

    public interface OnHostelDashboardClickListener{
        void onClick(int index);
    }
}
