package br.com.binmarques.githubrepositories.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import br.com.binmarques.githubrepositories.R;
import br.com.binmarques.githubrepositories.model.GitHubPullRequest;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Daniel Marques on 25/07/2018
 */

public class GitHubPullRequestsAdapter extends RecyclerView.Adapter<GitHubPullRequestsAdapter.ViewHolder> {

    private Context mContext;
    private List<GitHubPullRequest> mItems;
    private OnItemClickListener mListener;

    public GitHubPullRequestsAdapter(Context context, List<GitHubPullRequest> items) {
        this.mContext = context;
        this.mItems = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pull_requests, parent, false);

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GitHubPullRequest item = getItem(position);
        holder.mUserName.setText(item.getUser().getLogin());
        holder.mTitle.setText(item.getTitle());
        holder.mBody.setText(item.getBody() != null ? item.getBody().trim() : "");
        loadImage(mContext, item.getUser().getAvatarUrl()).into(holder.mUserImage);

        try {
            holder.mCreateAt.setText(item.getDateFormatted());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public void addItems(List<GitHubPullRequest> items) {
        mItems.clear();
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    private RequestBuilder<Drawable> loadImage(@NonNull Context context, @NonNull String path) {
        int duration = mContext.getResources()
                .getInteger(android.R.integer.config_mediumAnimTime);

        return Glide
                .with(context)
                .load(path)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).circleCrop())
                .transition(new DrawableTransitionOptions().crossFade(duration));
    }

    public GitHubPullRequest getItem(int position) {
        return mItems.get(position);
    }

    public List<GitHubPullRequest> getItems() {
        return mItems;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvAuthorName) TextView mUserName;
        @BindView(R.id.tvTitle) TextView mTitle;
        @BindView(R.id.tvCreateAt) TextView mCreateAt;
        @BindView(R.id.tvBody) TextView mBody;
        @BindView(R.id.imgUser) ImageView mUserImage;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.mainContent)
        void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    public interface OnItemClickListener {

        void onItemClick(@NonNull View view, int position);

    }
}
