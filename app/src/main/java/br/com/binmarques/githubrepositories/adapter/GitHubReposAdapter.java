package br.com.binmarques.githubrepositories.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.List;
import java.util.Locale;

import br.com.binmarques.githubrepositories.R;
import br.com.binmarques.githubrepositories.model.Item;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Daniel Marques on 24/07/2018
 */

public class GitHubReposAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Item> mItems;
    private boolean mIsLoadingAddedItem = false;
    private boolean mRetryLoading = false;
    private PaginationCallback mCallback;
    private OnItemClickListener mListener;

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    public GitHubReposAdapter(Context context, List<Item> items, PaginationCallback callback) {
        this.mContext = context;
        this.mItems = items;
        this.mCallback = callback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM) {
            View rootView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_repositories, parent, false);

            return new ViewHolder(rootView);

        } else {
            View rootView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_loading, parent, false);

            return new LoadingViewHolder(rootView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Item item = getItem(position);

        if (getItemViewType(position) == ITEM) {
            final ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.mProgressBar.setVisibility(View.VISIBLE);
            viewHolder.mRepoName.setText(item.getName());
            viewHolder.mRepoDesc.setText(item.getDescription());
            viewHolder.mRepoOwner.setText(item.getOwner().getLogin());

            String owner = mContext.getString(R.string.title_author) + ": " + item.getOwner().getLogin();

            String stargazers = mContext.getString(R.string.title_stars) + ": " +
                    String.format(Locale.getDefault(), "%d", item.getStarCount());

            String forks = mContext.getString(R.string.title_forks) + ": " +
                    String.format(Locale.getDefault(), "%d", item.getForks());

            viewHolder.mRepoOwner.setText(owner);
            viewHolder.mStargazers.setText(stargazers);
            viewHolder.mForks.setText(forks);

            loadImage(mContext, item.getOwner().getAvatarUrl()).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                            Target<Drawable> target, boolean isFirstResource) {
                    viewHolder.mProgressBar.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                               DataSource dataSource, boolean isFirstResource) {
                    viewHolder.mProgressBar.setVisibility(View.GONE);
                    return false;
                }
            }).into(viewHolder.mRepoImage);

        } else if (getItemViewType(position) == LOADING) {
            LoadingViewHolder viewHolder = (LoadingViewHolder) holder;

            if (mRetryLoading) {
                viewHolder.mErrorContainer.setVisibility(View.VISIBLE);
                viewHolder.mProgressBar.setVisibility(View.GONE);
            } else {
                viewHolder.mErrorContainer.setVisibility(View.GONE);
                viewHolder.mProgressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == mItems.size() - 1 && mIsLoadingAddedItem) ? LOADING : ITEM;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    private void addItem(Item item) {
        mItems.add(item);
        notifyItemInserted(mItems.size() - 1);
    }

    public void addItems(List<Item> items) {
        for (Item item : items) {
            addItem(item);
        }
    }

    private RequestBuilder<Drawable> loadImage(@NonNull Context context, @NonNull String path) {
        return Glide
                .with(context)
                .load(path)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).centerCrop())
                .transition(new DrawableTransitionOptions().crossFade());
    }

    public void showReload(boolean show) {
        mRetryLoading = show;
        notifyItemChanged(mItems.size() - 1);
    }

    public boolean isShownReload() {
        return mRetryLoading;
    }

    public void addLoadingFooter() {
        mIsLoadingAddedItem = true;
        addItem(new Item());
    }

    public boolean isLoadingAdded() {
        return mIsLoadingAddedItem;
    }

    public void removeLoadingFooter() {
        mIsLoadingAddedItem = false;
        int position = mItems.size() - 1;
        Item item = getItem(position);

        if (item != null) {
            mItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clearItems() {
        mIsLoadingAddedItem = false;
        mItems.clear();
        notifyDataSetChanged();
    }

    public Item getItem(int position) {
        return mItems.get(position);
    }

    public List<Item> getItems() {
        return mItems;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvRepoName) TextView mRepoName;
        @BindView(R.id.tvRepoDesc) TextView mRepoDesc;
        @BindView(R.id.tvAuthorName) TextView mRepoOwner;
        @BindView(R.id.tvStargazers) TextView mStargazers;
        @BindView(R.id.tvForks) TextView mForks;
        @BindView(R.id.imgProgress) ProgressBar mProgressBar;
        @BindView(R.id.imgRepo) ImageView mRepoImage;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.cardViewRepo)
        void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.loadMoreProgress) ProgressBar mProgressBar;
        @BindView(R.id.loadMoreError) LinearLayout mErrorContainer;

        LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.loadMoreError)
        void onClick() {
            showReload(false);
            mCallback.onPageReloaded();
        }
    }

    public interface OnItemClickListener {

        void onItemClick(@NonNull View view, int position);

    }

    public interface PaginationCallback {

        void onPageReloaded();

    }
}
