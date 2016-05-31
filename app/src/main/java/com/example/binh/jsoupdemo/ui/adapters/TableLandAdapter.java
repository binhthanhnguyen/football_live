package com.example.binh.jsoupdemo.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.binh.jsoupdemo.R;
import com.example.binh.jsoupdemo.data.models.Team;

import java.util.List;

/**
 * Created by binh on 5/30/16.
 */
public class TableLandAdapter extends RecyclerView.Adapter<TableLandAdapter.TeamHolder> {
    private List<Team> mTeams;

    public TableLandAdapter(List<Team> teams) {
        mTeams = teams;
    }
    @Override
    public TableLandAdapter.TeamHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.team_list_item_land, parent, false);
        return new TeamHolder(view);
    }

    @Override
    public void onBindViewHolder(TeamHolder holder, int position) {
        Team team = mTeams.get(position);
        holder.bindTeamItem(team);
    }

    @Override
    public int getItemCount() {
        return mTeams.size();
    }

    public class TeamHolder extends RecyclerView.ViewHolder {
        private View mView;
        private Context mContext;
        private TextView mTextPosition;
        private ImageView mImageTeam;
        private TextView mTextTeam;
        private TextView mTextPts;
        private TextView mTextGp;
        private TextView mTextWins;
        private TextView mTextDraws;
        private TextView mTextLosses;

        public TeamHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mContext = itemView.getContext();
            mTextPosition = (TextView) itemView.findViewById(R.id.text_position);
            mImageTeam = (ImageView) itemView.findViewById(R.id.image_team);
            mTextTeam = (TextView) itemView.findViewById(R.id.text_team);
            mTextPts = (TextView) itemView.findViewById(R.id.text_pts);
            mTextGp = (TextView) itemView.findViewById(R.id.text_gp);
            mTextWins = (TextView) itemView.findViewById(R.id.text_wins);
            mTextDraws = (TextView) itemView.findViewById(R.id.text_draws);
            mTextLosses = (TextView) itemView.findViewById(R.id.text_losses);
        }

        public void bindTeamItem(Team team) {
            mTextPosition.setText(String.valueOf(team.getPosition()));
            Glide.with(mContext).load(team.getImageUrl()).into(mImageTeam);
            mTextTeam.setText(team.getName());
            mTextPts.setText(String.valueOf(team.getPst()));
            mTextGp.setText(String.valueOf(team.getGp()));
            mTextWins.setText(String.valueOf(team.getWins()));
            mTextDraws.setText(String.valueOf(team.getDraws()));
            mTextLosses.setText(String.valueOf(team.getLosses()));
        }
    }
}
