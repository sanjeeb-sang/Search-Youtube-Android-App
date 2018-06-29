package sangraula.sanjeeb.wissionapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import sangraula.sanjeeb.wissionapp.R;
import sangraula.sanjeeb.wissionapp.models.YoutubeVideoItem;

public class VideoListAdapter extends Adapter<VideoListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<YoutubeVideoItem> mItems = new ArrayList<>();


    public VideoListAdapter(Context c, @Nullable ArrayList<YoutubeVideoItem> items) {

        mContext = c;
        setItems(items);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_video_list_single_item, parent, false);

        ViewHolder v = new ViewHolder(view);

        return v;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        YoutubeVideoItem item = mItems.get(position);

        holder.mTitleTV.setText(item.title);
        holder.mLikesCountTV.setText(String.valueOf(item.likes));
        Picasso.get().load(item.thumbnailUrl).into(holder.mImageView);
    }

    @Override
    public int getItemCount() {

        return (mItems == null) ? 0 : mItems.size();

    }


    public void setItems(ArrayList<YoutubeVideoItem> items) {

        mItems.clear();

        if (items != null) {
            mItems.addAll(items);
        }
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitleTV, mLikesCountTV;
        private EditText mCommentET;
        private ImageView mImageView;
        private Button mLikeButton, mCommentButton;


        public ViewHolder(View itemView) {
            super(itemView);

            mTitleTV = itemView.findViewById(R.id.title_tv);
            mLikesCountTV = itemView.findViewById(R.id.likes_count_tv);
            mCommentET = itemView.findViewById(R.id.comment_et);
            mCommentButton = itemView.findViewById(R.id.comment_button);
            mLikeButton = itemView.findViewById(R.id.like_button);
            mImageView = itemView.findViewById(R.id.image_view);

            mCommentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCommentET.setVisibility(View.VISIBLE);
                }
            });

            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    YoutubeVideoItem i = mItems.get(getAdapterPosition());

                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + i.videoId)));
                }
            });
        }
    }
}
