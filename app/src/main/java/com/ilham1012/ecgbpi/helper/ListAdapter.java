package com.ilham1012.ecgbpi.helper;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ilham1012.ecgbpi.R;

import java.util.List;

/**
 * Created by ilham on 11/02/2016.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.CustomViewHolder> {
    private List<FeedItem> feedItemList;
    private String[] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ListAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, null);
        // set the view's size, margins, paddings and layout parameters ...
        CustomViewHolder vh = new CustomViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int position) {
        FeedItem feedItem = feedItemList.get(position);

        //Setting text view title
        customViewHolder.title.setText(Html.fromHtml(feedItem.getTitle()));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
