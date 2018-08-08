package com.gametimegiving.android.Adapters;

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

import com.gametimegiving.android.Helpers.GlideApp;
import com.gametimegiving.android.R;
import com.gametimegiving.android.models.Game;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {
    final String TAG = "GameAdapter";
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private Context mCtx;
    private List<Game> mGameList;

    public GameAdapter(Context ctx, List<Game> gameList) {
        mCtx = ctx;
        mGameList = gameList;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.gamelistitem, null);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        Game game = mGameList.get(position);
        holder.tvHomeTeamName.setText(game.getHometeam().getTeamname());
        holder.tvHomeTeamMascot.setText(game.getHometeam().getMascot());
        holder.tvAwayTeamName.setText(game.getAwayteam().getTeamname());
        holder.tvAwayTeamMascot.setText(game.getAwayteam().getMascot());
        holder.btnsavegame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.btnsavegame.getText() == mCtx.getString(R.string.followtext)) {
                    holder.btnsavegame.setText(R.string.unfollowtext);
                } else {
                    holder.btnsavegame.setText(R.string.followtext);
                }
                Toast.makeText(mCtx, "Saving this Game for you", Toast.LENGTH_SHORT).show();

            }
        });
        String homeTeamLogo = game.getHometeam().getLogo();
        String awayTeamLogo = game.getAwayteam().getLogo();
        StorageReference homeTeamLogoReference, awayTeamLogoReference;
        try {
            homeTeamLogoReference = storage.getReferenceFromUrl(homeTeamLogo);
            awayTeamLogoReference = storage.getReferenceFromUrl(awayTeamLogo);
        } catch (Exception ex) {
            homeTeamLogoReference = storage.getReferenceFromUrl(String.valueOf(R.string.defaultcharitylogo));
            awayTeamLogoReference = storage.getReferenceFromUrl(String.valueOf(R.string.defaultcharitylogo));

        }
        GlideApp.with(mCtx)
                .load(homeTeamLogoReference)
                .into(holder.ivHomeTeamLogo);
        GlideApp.with(mCtx)
                .load(awayTeamLogoReference)
                .into(holder.ivAwayTeamLogo);


    }

    @Override
    public int getItemCount() {
        return mGameList.size();
    }

    class GameViewHolder extends RecyclerView.ViewHolder {
        ImageView ivHomeTeamLogo, ivAwayTeamLogo;
        TextView tvHomeTeamName, tvAwayTeamName, tvHomeTeamMascot, tvAwayTeamMascot;
        Button btndetail, btnsavegame;

        private GameViewHolder(View itemView) {
            super(itemView);
            btndetail = itemView.findViewById(R.id.btnmoredetail);
            btnsavegame = itemView.findViewById(R.id.btnsavegame);
            ivHomeTeamLogo = itemView.findViewById(R.id.ivhometeamlogo);
            ivAwayTeamLogo = itemView.findViewById(R.id.ivawayteamlogo);
            tvHomeTeamName = itemView.findViewById(R.id.tvhometeamname);
            tvAwayTeamName = itemView.findViewById(R.id.tvawayteamname);
            tvHomeTeamMascot = itemView.findViewById(R.id.tvhometeammascot);
            tvAwayTeamMascot = itemView.findViewById(R.id.tvawayteammascot);

        }

    }
}
