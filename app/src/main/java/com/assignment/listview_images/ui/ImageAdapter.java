package com.assignment.listview_images.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.assignment.listview_images.R;
import com.assignment.listview_images.models.RowModel;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Image display Adapter class
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private List<RowModel> mItems;
    private PostItemListener mItemListener;

    /**
     * View holder to hold all views
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        // Butter Knife Dependency Injection
        @BindView(R.id.text_view_heading) AppCompatTextView tv_heading;
        @BindView(R.id.tv_description) AppCompatTextView tv_Description;
        @BindView(R.id.iv_animal) ImageView iv_Pics;

        PostItemListener mItemListener;
        // We'll use this field to showcase matching the holder from the test.
        private boolean mIsInTheMiddle = false;

        ViewHolder(View itemView, PostItemListener postItemListener) {
            super(itemView);

            // Butter Knife initialization for dependency injection
            ButterKnife.bind(this, itemView);

            this.mItemListener = postItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            RowModel item = getItem(getAdapterPosition());
            this.mItemListener.onPostClick(item.getTitle());
        }

        public boolean getIsInTheMiddle() {
            return mIsInTheMiddle;
        }

        void setIsInTheMiddle(boolean isInTheMiddle) {
            mIsInTheMiddle = isInTheMiddle;
        }
    }

    /**
     * Constructor to pass value to adapter
     * @param posts
     * @param itemListener
     */
    public ImageAdapter(List<RowModel> posts, PostItemListener itemListener) {
        mItems = posts;
        mItemListener = itemListener;
    }

    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.image_items, parent, false);

        return new ViewHolder(postView, this.mItemListener);
    }

    @Override
    public void onBindViewHolder(final ImageAdapter.ViewHolder holder, int position) {
        RowModel item = mItems.get(position);
        ImageLoader imageLoader = ImageLoader.getInstance();
        holder.tv_heading.setText(item.getTitle());
        if(null!= item.getDescription())
            holder.tv_Description.setText(item.getDescription());
        //download and display image from url
        imageLoader.displayImage((String) item.getImageHref(),  holder.iv_Pics, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String imageUri, View view) {
                holder.iv_Pics.setImageResource(R.drawable.ic_loading);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view,
                                        FailReason failReason) {
                // This will handle 404 and it will catch null exception
                // do here what you want to do
                holder.iv_Pics.setImageResource(R.drawable.ic_failed);
            }

            @Override
            public void onLoadingComplete(String imageUri,
                                          View view, Bitmap loadedImage) {
                if (holder.iv_Pics == null)
                    return;

                if(null != loadedImage)
                    holder.iv_Pics.setImageBitmap(loadedImage);
                else
                    holder.iv_Pics.setImageResource(R.drawable.ic_failed);
            }

            @Override
            public void onLoadingCancelled(String imageUri,
                                           View view) {
                holder.iv_Pics.setImageResource(R.drawable.ic_failed);
            }

        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    /**
     * Method to update Adapter
     * @param items
     */
    public void updateAnswers(List<RowModel> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    private RowModel getItem(int adapterPosition) {
        return mItems.get(adapterPosition);
    }

    /**
     * Interface to give callback on item clicked
     */
    public interface PostItemListener {
        void onPostClick(String str);
    }
}
