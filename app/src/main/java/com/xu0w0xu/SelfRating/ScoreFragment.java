package com.xu0w0xu.SelfRating;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreFragment extends Fragment {
    private ListView listView;
    private ScoreAdapter adapter;
    private List<Score> scoreList = new ArrayList<>();
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score, container, false);

        listView = view.findViewById(R.id.score_list);
        adapter = new ScoreAdapter(getActivity(), scoreList);
        listView.setAdapter(adapter);

        // 获取数据库实例
        db = ((MainActivity) getActivity()).getDb();

        fetchScoresFromDatabase();

        return view;
    }

    private void fetchScoresFromDatabase() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 这里是在新的线程中执行的，所以可以执行数据库操作
                List<Score> scores = db.scoreDao().getAll();
                // 更新 scoreList
                List<Score> temp = new ArrayList<>(scores);
                Collections.reverse(temp);
                scoreList.clear();
                scoreList.addAll(temp);

                // 更新视图需要在主线程中执行
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }
}
