package sangraula.sanjeeb.wissionapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import sangraula.sanjeeb.wissionapp.R;
import sangraula.sanjeeb.wissionapp.adapter.VideoListAdapter;
import sangraula.sanjeeb.wissionapp.models.YoutubeVideoItem;
import sangraula.sanjeeb.wissionapp.utils.YoutubeSearchHelper;

public class VideoListFragment extends Fragment {

    private VideoListAdapter mAdapter;
    private SwipeRefreshLayout mSwipe;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_video_list, container, false);

        mSwipe = v.findViewById(R.id.swipe);

        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                startLoadingVideos();
            }
        });

        RecyclerView rv = v.findViewById(R.id.recycler_view);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        rv.setLayoutManager(manager);

        mAdapter = new VideoListAdapter(getActivity(), null);

        rv.setAdapter(mAdapter);

        startLoadingVideos();

        return v;
    }


    private void startLoadingVideos () {

        YoutubeSearchHelper h = new YoutubeSearchHelper();

        h.searchYoutube("comedy", new YoutubeSearchHelper.OnSearchCompleteListener() {
            @Override
            public void onSearchComplete(ArrayList<YoutubeVideoItem> videos) {

                 mAdapter.setItems(videos);

                 if (mSwipe.isRefreshing()) {
                     mSwipe.setRefreshing(false);
                 }
            }

            @Override
            public void onGetLikesTaskFinished(ArrayList<YoutubeVideoItem> videos) {

                mAdapter.setItems(videos);

                if (mSwipe.isRefreshing()) {
                    mSwipe.setRefreshing(false);
                }
            }
        });

    }
}
