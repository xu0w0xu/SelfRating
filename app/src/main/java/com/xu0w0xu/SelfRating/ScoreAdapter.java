package com.xu0w0xu.SelfRating;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ScoreAdapter extends ArrayAdapter<Score> {
    private LayoutInflater inflater;

    public ScoreAdapter(@NonNull Context context, List<Score> scores) {
        super(context, 0, scores);
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Score score = getItem(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_score, parent, false);
        }

        TextView date = convertView.findViewById(R.id.date);
        TextView finalScore = convertView.findViewById(R.id.final_score);
        TextView highestScore = convertView.findViewById(R.id.highest_score);
        TextView lowestScore = convertView.findViewById(R.id.lowest_score);

        // 这里需要根据你的数据库设计来更新 TextView 的内容
        date.setText(score.date);
        finalScore.setText(String.valueOf(score.score)); // 假设这是当天最后的分数
        highestScore.setText(""); // 这里应该显示当天的最高分
        lowestScore.setText(""); // 这里应该显示当天的最低分

        return convertView;
    }
}
