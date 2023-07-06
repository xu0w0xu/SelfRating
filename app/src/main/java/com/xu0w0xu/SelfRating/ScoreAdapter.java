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
        TextView change_score = convertView.findViewById(R.id.change_score);
        TextView highest_score = convertView.findViewById(R.id.highest_score);

        date.setText(score.date);
        finalScore.setText(String.valueOf(score.score));
        change_score.setText(String.valueOf(score.scoreChange));  // 显示分数变化
        highest_score.setText(String.valueOf(score.maxScore));  // 显示最高分

        return convertView;
    }

}
