package com.assignment.listview_images.presenter;

import android.content.Context;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.assignment.listview_images.R;
import com.assignment.listview_images.models.RowModel;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Image display Adapter class
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private List<RowModel> mItems;
    private Context mContext;
    private PostItemListener mItemListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        AppCompatTextView tv_heading;
        AppCompatTextView tv_Description;
        ImageView iv_Pics;
        PostItemListener mItemListener;

        public ViewHolder(View itemView, PostItemListener postItemListener) {
            super(itemView);
            tv_heading = itemView.findViewById(R.id.text_view_heading);
            tv_Description = itemView.findViewById(R.id.tv_description);
            iv_Pics = itemView.findViewById(R.id.iv_animal);

            this.mItemListener = postItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            RowModel item = getItem(getAdapterPosition());
            this.mItemListener.onPostClick(item.getTitle());

            notifyDataSetChanged();
        }
    }

    public ImageAdapter(Context context, List<RowModel> posts, PostItemListener itemListener) {
        mItems = posts;
        mContext = context;
        mItemListener = itemListener;
    }

    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.image_items, parent, false);

        ViewHolder viewHolder = new ViewHolder(postView, this.mItemListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ImageAdapter.ViewHolder holder, int position) {
        RowModel item = mItems.get(position);
        ImageLoader imageLoader = ImageLoader.getInstance();
        holder.tv_heading.setText(item.getTitle());
        holder.tv_Description.setText(item.getDescription());
        //download and display image from url
        imageLoader.displayImage((String) item.getImageHref(),  holder.iv_Pics);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void updateAnswers(List<RowModel> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    private RowModel getItem(int adapterPosition) {
        return mItems.get(adapterPosition);
    }

    public interface PostItemListener {
        void onPostClick(String str);
    }
}
