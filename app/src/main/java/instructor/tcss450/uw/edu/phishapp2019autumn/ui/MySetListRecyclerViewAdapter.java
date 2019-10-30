package instructor.tcss450.uw.edu.phishapp2019autumn.ui;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import instructor.tcss450.uw.edu.phishapp2019autumn.R;
import instructor.tcss450.uw.edu.phishapp2019autumn.model.SetList;
import instructor.tcss450.uw.edu.phishapp2019autumn.ui.SetListFragment.OnListFragmentInteractionListener;

import java.util.List;


public class MySetListRecyclerViewAdapter extends RecyclerView.Adapter<MySetListRecyclerViewAdapter.ViewHolder> {

    private final List<SetList> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MySetListRecyclerViewAdapter(List<SetList> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_setlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mDateView.setText(mValues.get(position).getDate());
        holder.mVenueView.setText(mValues.get(position).getVenue());
        holder.mLocationView.setText(mValues.get(position).getLocation() + ": ");

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
        public final TextView mDateView;
        public final TextView mLocationView;
        public final TextView mVenueView;
        public SetList mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDateView = view.findViewById(R.id.text_setlist_longdate);
            mLocationView = view.findViewById(R.id.text_setlist_location);
            mVenueView = view.findViewById(R.id.text_setlist_venue);


        }

        @Override
        public String toString() {
            return super.toString() + " '" + mDateView.getText() + "'";
        }
    }
}
