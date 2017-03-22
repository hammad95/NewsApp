package com.android.hammad.testapp.ui.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.hammad.testapp.R;
import com.android.hammad.testapp.ui.WebActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Hassan on 3/20/2017.
 */

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ArticleViewHolder> {

    private RecyclerView mRecyclerView;
    private ArrayList<ContentValues> mDataArray;
    private Context mContext;

    public NewsListAdapter(RecyclerView recyclerView, ArrayList<ContentValues> dataArray, Context context) {
        super();

        // Get the RecyclerView
        mRecyclerView = recyclerView;

        // Get the data from the passed array
        mDataArray = new ArrayList<>();
        mDataArray.addAll(dataArray);

        // Get the context
        mContext = context;
    }

    @Override
    public int getItemCount() {
        return mDataArray.size();
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.news_list_item, parent, false);

        // Set a click listener on the view to launch WebActivity which
        // loads and displays the full article from its url in a WebView
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = mRecyclerView.getChildAdapterPosition(view);
                Intent intent = new Intent(mContext, WebActivity.class);
                intent.putExtra(WebActivity.INTENT_EXTRA_KEY_ARTICLE_URL,
                        (String) mDataArray.get(position).get(mContext.getString(R.string.key_article_url)));
                mContext.startActivity(intent);
            }
        });

        // Return an ArticleViewHolder created from the inflated view
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ArticleViewHolder holder, int position) {
        // Get the image url from the data array
        final String IMAGE_URL = (String) mDataArray.get(position).get(
                mContext.getString(R.string.key_article_url_to_image));

        // Set the image
        // Try to load image from cache, if not successful, then, start a network request
        Picasso.with(mContext).
                load(IMAGE_URL).                                // original
                networkPolicy(NetworkPolicy.OFFLINE).           // only set image from cache
                into(holder.mIvArticleImage, new Callback() {   // try to load and register callback
            @Override
            public void onSuccess() {
                // Do nothing if successful
            }

            @Override
            public void onError() {
                // If error, then download image
                Picasso.with(mContext).
                        load(IMAGE_URL).                        // original
                        into(holder.mIvArticleImage);           // load into ImageView
            }
        });

        // Set the description
        holder.mTvDescription.setText((String)
                mDataArray.get(position).get(mContext.getString(R.string.key_article_description)));

        // set the source
        holder.mTvSource.setText((String)
                mDataArray.get(position).get(mContext.getString(R.string.key_article_source)));
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder {
        protected ImageView mIvArticleImage;
        protected TextView mTvDescription;
        protected TextView mTvSource;

        public ArticleViewHolder(View itemView) {
            super(itemView);

            // Get the views
            mIvArticleImage = (ImageView) itemView.findViewById(R.id.iv_article_image);
            mTvDescription = (TextView) itemView.findViewById(R.id.tv_description);
            mTvSource = (TextView) itemView.findViewById(R.id.tv_source);
        }
    }
}
