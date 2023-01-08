package de.hawlandshut.sharedwallet.views.components;

import android.content.Context;
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
import de.hawlandshut.sharedwallet.model.entities.UserInfoDto;
import de.hawlandshut.sharedwallet.views.activities.AddFriendToGroupActivity;

public class AddFriendToGroupAdapter extends RecyclerView.Adapter<AddFriendToGroupAdapter.AddFriendHolder> {

    private List<UserInfoDto> friends = new ArrayList<>();
    private Context context;

    public AddFriendToGroupAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public AddFriendToGroupAdapter.AddFriendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate((R.layout.friend_list_item), parent, false);
        return new AddFriendHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AddFriendHolder holder, int position) {

        holder.tvFriendName.setText(friends.get(position).getDisplayName());
        holder.selectedUser = friends.get(position);
        if (position % 2 == 0) {
            holder.cvFriendItem.setBackgroundResource(R.color.white);
        } else {

            holder.cvFriendItem.setBackgroundResource(R.color.light_grey);
        }
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public void setFriends(List<UserInfoDto> friends) {
        this.friends = friends;
        notifyDataSetChanged();
    }

    class AddFriendHolder extends RecyclerView.ViewHolder {
        private TextView tvFriendName;
        private CardView cvFriendItem;
        private UserInfoDto selectedUser;

        public AddFriendHolder(View itemView) {
            super(itemView);
            tvFriendName = itemView.findViewById(R.id.tv_friend_name);
            cvFriendItem = itemView.findViewById(R.id.cv_friend_item);

            itemView.setOnClickListener(view ->{
                ((AddFriendToGroupActivity)context).updateMembers(selectedUser);
            });
        }
    }
}


