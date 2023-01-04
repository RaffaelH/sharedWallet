package de.hawlandshut.sharedwallet.views.components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.hawlandshut.sharedwallet.R;
import de.hawlandshut.sharedwallet.model.entities.FriendRequestDto;
import de.hawlandshut.sharedwallet.views.activities.FriendRequestActivity;


public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.FriendRequestHolder> {

    private List<FriendRequestDto> requests = new ArrayList<>();
    private Context context;

    public FriendRequestAdapter (Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public FriendRequestAdapter.FriendRequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate((R.layout.friend_request_item), parent, false);
        return new FriendRequestHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestHolder holder, int position) {
            FriendRequestDto request = requests.get(position);
            String title = "Einladung von: "+ request.getInviterName();
            holder.tvTitle.setText(title);
            FriendRequestDto friendRequestDto = request;
            holder.btnAccept.setOnClickListener(view -> {
                friendRequestDto.setProcessed(true);
                friendRequestDto.setDeclined(false);
                ((FriendRequestActivity)context).updateFriendRequest(friendRequestDto);
            });
            holder.btnDecline.setOnClickListener(view -> {
                friendRequestDto.setProcessed(true);
                friendRequestDto.setDeclined(true);
                ((FriendRequestActivity)context).updateFriendRequest(friendRequestDto);
            });

    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public void setRequests(List<FriendRequestDto> requests) {
        this.requests = requests;
        notifyDataSetChanged();
    }


    class FriendRequestHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private Button btnAccept;
        private Button btnDecline;

        public FriendRequestHolder(View itemView){
            super(itemView);
            tvTitle  =itemView.findViewById(R.id.tv_friend_request_title);
            btnAccept = itemView.findViewById(R.id.btn_accept_request);
            btnDecline = itemView.findViewById(R.id.btn_decline_request);

        }
    }

}


