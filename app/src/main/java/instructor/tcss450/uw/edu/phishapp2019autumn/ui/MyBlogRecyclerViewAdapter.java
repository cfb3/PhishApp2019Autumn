package instructor.tcss450.uw.edu.phishapp2019autumn.ui;

import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import instructor.tcss450.uw.edu.phishapp2019autumn.R;
import instructor.tcss450.uw.edu.phishapp2019autumn.ui.BlogFragment.OnListFragmentInteractionListener;
import instructor.tcss450.uw.edu.phishapp2019autumn.model.BlogPost;

import java.util.List;


public class MyBlogRecyclerViewAdapter extends RecyclerView.Adapter<MyBlogRecyclerViewAdapter.ViewHolder> {

    private final List<BlogPost> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyBlogRecyclerViewAdapter(List<BlogPost> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_blog, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).getTitle());
        holder.mPubDateView.setText(mValues.get(position).getPubDate());
        final String preview =  Html.fromHtml(
                mValues.get(position).getTeaser(),
                Html.FROM_HTML_MODE_COMPACT)
                .toString().substring(0,100)
                + "...";
        holder.mPreviewView.setText(preview);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mPubDateView;
        public final TextView mPreviewView;
        public BlogPost mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = view.findViewById(R.id.text_item_blog_title);
            mPubDateView = view.findViewById(R.id.text_item_blog_pubdate);
            mPreviewView = view.findViewById(R.id.text_item_blog_preview);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}
