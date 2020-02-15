package com.jv.hnreader.ui.screens.comments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.jv.hnreader.R;
import com.jv.hnreader.datamodels.Comment;
import com.jv.hnreader.datamodels.Story;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommentListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommentListFragment extends Fragment implements CommentListContract.View {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CommentsPresenter mCommentsPresenter;
    private RecyclerView mCommentsRecyclerView;
    private CommentsAdapter mCommentsAdapter;


    public CommentListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommentListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CommentListFragment newInstance(String param1, String param2) {
        CommentListFragment fragment = new CommentListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mCommentsPresenter = new CommentsPresenter(this);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCommentsRecyclerView = view.findViewById(R.id.recycler_view_comments);
        mCommentsAdapter = new CommentsAdapter(mCommentsPresenter);
        mCommentsRecyclerView.setAdapter(mCommentsAdapter);
        //+mCommentsPresenter.onLoadComments();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCommentsPresenter.onDestroy();
    }

    @Override
    public void showComments(List<Comment> comment) {

    }

    @Override
    public void showStoryDetail(Story story) {

    }

    @Override
    public void showNoMoreComments() {

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.comment_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.web_menu_item:
                return true;
            case R.id.comment_share_menu_item:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
