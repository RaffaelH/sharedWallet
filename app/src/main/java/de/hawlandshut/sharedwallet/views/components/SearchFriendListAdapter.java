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
import de.hawlandshut.sharedwallet.views.activities.InviteFriendsActivity;

public class SearchFriendListAdapter extends RecyclerView.Adapter<SearchFriendListAdapter.SearchFriendHolder> {

    private List<UserInfoDto> users = new ArrayList<>();
    private Context context;

    public SearchFriendListAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public SearchFriendListAdapter.SearchFriendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate((R.layout.search_friends_item), parent, false);
        return new SearchFriendHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchFriendHolder holder, int position) {
        if(users.size() >0){
            holder.cvCard.setBackgroundResource(R.color.light_grey);
            UserInfoDto user = users.get(position);
            holder.tvDisplayName.setText(user.getDisplayName());
            holder.cvCard.setOnClickListener(view -> {
                ((InviteFriendsActivity)context).inviteFriends(users.get(position));
            });
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setUsers(List<UserInfoDto> users) {
        this.users = users;
        notifyDataSetChanged();
    }


    class SearchFriendHolder extends RecyclerView.ViewHolder {
        private CardView cvCard;
        private TextView tvDisplayName;

        public SearchFriendHolder(View itemView){
            super(itemView);
            tvDisplayName  =itemView.findViewById(R.id.tv_search_friend_name);
            cvCard =itemView.findViewById(R.id.cv_search_friend_item);

        }


    }

}


