package de.hawlandshut.sharedwallet.views.components;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.hawlandshut.sharedwallet.R;
import de.hawlandshut.sharedwallet.model.entities.GroupDto;
import de.hawlandshut.sharedwallet.model.entities.GroupInfoDto;
import de.hawlandshut.sharedwallet.viewmodel.GroupViewModel;
import de.hawlandshut.sharedwallet.views.activities.GroupEditActivity;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.GroupHolder> {

    private List<GroupInfoDto> groups = new ArrayList<>();

    @NonNull
    @Override
    public GroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate((R.layout.group_list_item),parent,false);
        return new GroupHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupHolder holder, int position) {
        if(position % 2 == 0)
        {
            holder.cvCard.setBackgroundResource(R.color.light_grey);
        }
        else
        {
            holder.cvCard.setBackgroundResource(R.color.white);
        }
        GroupInfoDto groupInfo = groups.get(position);
        holder.tvTitle.setText(groupInfo.getTitle());
        holder.tvMembers.setText(String.valueOf(groupInfo.getMemberNames()));
        holder.groupId = groups.get(position).getGroupId();
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public void setGroups(List<GroupInfoDto> groups){
        this.groups = groups;
        notifyDataSetChanged();
    }

    class GroupHolder extends RecyclerView.ViewHolder{
        private CardView cvCard;
        private TextView tvTitle;
        private TextView tvMembers;
        private String groupId;
        private GroupViewModel groupViewModel;

        public GroupHolder(View itemView){
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_group_list_title);
            tvMembers = itemView.findViewById(R.id.tv_members);
            cvCard = itemView.findViewById(R.id.cv_group_item);

            itemView.setOnClickListener(view ->{
                Log.d("GroupHolder",groupId);
                itemView.getContext()
                        .startActivity(new Intent(itemView.getContext(), GroupEditActivity.class)
                                .putExtra("groupId",groupId));
                    });
        }

    }


}
