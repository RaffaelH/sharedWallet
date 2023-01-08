package de.hawlandshut.sharedwallet.views.components;

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

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendHolder> {

    private List<UserInfoDto> friends = new ArrayList<>();

    @NonNull
    @Override
    public FriendAdapter.FriendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate((R.layout.friend_list_item), parent, false);
        return new FriendHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendHolder holder, int position) {

        holder.tvFriendName.setText(friends.get(position).getDisplayName());
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

    class FriendHolder extends RecyclerView.ViewHolder {
        private TextView tvFriendName;
        private CardView cvFriendItem;

        public FriendHolder(View itemView) {
            super(itemView);
            tvFriendName = itemView.findViewById(R.id.tv_friend_name);
            cvFriendItem = itemView.findViewById(R.id.cv_friend_item);
        }
    }
}


