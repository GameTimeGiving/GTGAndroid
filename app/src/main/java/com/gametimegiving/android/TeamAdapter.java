package com.gametimegiving.android;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder> {
    final String TAG = "TeamAdapter";
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private Context mCtx;
    private List<Team> mTeamList;

    public TeamAdapter(Context ctx, List<Team> teamList) {
        mCtx = ctx;
        mTeamList = teamList;
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.teamlistitem, null);
        TeamViewHolder holder = new TeamViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        Team team = mTeamList.get(position);
        holder.tvTeamName.setText(team.getTeamname());
        holder.btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.btnsave.getText() == mCtx.getString(R.string.followtext)) {
                    holder.btnsave.setText(R.string.unfollowtext);
                } else {
                    holder.btnsave.setText(R.string.followtext);
                }
                Toast.makeText(mCtx, "Saving this team for you", Toast.LENGTH_SHORT).show();

            }
        });
        holder.btndetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mCtx, "Taking your to the team details", Toast.LENGTH_SHORT).show();
            }
        });
        // holder.tvCharityPurpose.setText(charity.getPurpose());
        // holder.tvCharityMission.setText(charity.getMission());
        String teamLogo = team.getLogo();
        StorageReference teamLogoReference;
        try {
            teamLogoReference = storage.getReferenceFromUrl(teamLogo);
        } catch (Exception ex) {
            teamLogoReference = storage.getReferenceFromUrl(String.valueOf(R.string.defaultcharitylogo));

        }
        GlideApp.with(mCtx)
                .load(teamLogoReference)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return mTeamList.size();
    }

    class TeamViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvTeamName;
        Button btndetail, btnsave;

        public TeamViewHolder(View itemView) {
            super(itemView);
            btndetail = itemView.findViewById(R.id.btnmoredetail);
            btnsave = itemView.findViewById(R.id.btnsavecharity);
            imageView = itemView.findViewById(R.id.teamlogo);
            tvTeamName = itemView.findViewById(R.id.teamname);
            //     tvCharityMission=itemView.findViewById(R.id.charitymission);
            //      tvCharityPurpose=itemView.findViewById(R.id.charitypurpose);
        }

    }
}
