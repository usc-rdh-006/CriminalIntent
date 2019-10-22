package android.bignerdranch.com;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CheckInListFragment extends Fragment {
    private boolean mSubtitleVisible;
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private MyCheckIn mMyCheckIn;

        public void bind(MyCheckIn myCheckIn) {
            mMyCheckIn = myCheckIn;
            mTitleTextView.setText(mMyCheckIn.getTitle());
            mDateTextView.setText(mMyCheckIn.getDate().toString());
        }

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_ui, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_date);
        }

        @Override
        public void onClick(View view) {
            Intent intent = CheckInPagerActivity.newIntent(getActivity(), mMyCheckIn.getId());
            startActivity(intent);
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        private List<MyCheckIn> mMyCheckIns;

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CrimeHolder(layoutInflater, parent);
        }
        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            MyCheckIn myCheckIn = mMyCheckIns.get(position);
            holder.bind(myCheckIn);

        }
        @Override
        public int getItemCount() {

            return mMyCheckIns.size();
        }

        public CrimeAdapter(List<MyCheckIn> myCheckIns) {
            mMyCheckIns = myCheckIns;
        }

        public void setMyCheckIns(List<MyCheckIn> myCheckIns) {
            mMyCheckIns = myCheckIns;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkin_list, container, false);
        mCrimeRecyclerView = (RecyclerView) view
                .findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_checkin_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_crime:
                MyCheckIn myCheckIn = new MyCheckIn();
                CheckInLab.get(getActivity()).addCrime(myCheckIn);
                Intent intent = CheckInPagerActivity
                        .newIntent(getActivity(), myCheckIn.getId());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void updateSubtitle() {
        CheckInLab checkInLab = CheckInLab.get(getActivity());
        int crimeCount = checkInLab.getCrimes().size();
        String subtitle = getString(R.string.subtitle_format, crimeCount);
        if (!mSubtitleVisible) {
            subtitle = null;
        }
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private void updateUI() {
        CheckInLab checkInLab = CheckInLab.get(getActivity());
        List<MyCheckIn> myCheckIns = checkInLab.getCrimes();
        if (mAdapter == null) {
        mAdapter = new CrimeAdapter(myCheckIns);
        mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setMyCheckIns(myCheckIns);
            mAdapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }

}
