package com.xu0w0xu.SelfRating;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView scoreText;
    private int score = 0;
    private AppDatabase db; // 作为成员变量定义
    private ListView listView;
    private ScoreAdapter adapter;
    private List<Score> scoreList = new ArrayList<>();

    // 全局变量，用于存储最大值和最小值
    private int globalMaxScore = Integer.MIN_VALUE;
    private int globalMinScore = Integer.MAX_VALUE;

    public int getGlobalMaxScore() {
        return globalMaxScore;
    }

    public int getGlobalMinScore() {
        return globalMinScore;
    }

    public AppDatabase getDb() {
        return db;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 初始化数据库
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "score-database").build();

        //找到editScore的按钮
        Button editScore = (Button) findViewById(R.id.editScore);

        // 初始化ListView和Adapter
        listView = findViewById(R.id.score_list);
        adapter = new ScoreAdapter(this, scoreList);
        listView.setAdapter(adapter);

        // 设置editScore的点击事件为弹出一个对话框来修改分数
        editScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText input = new EditText(MainActivity.this);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("修改分数")
                        .setView(input)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String inputStr = input.getText().toString();
                                try {
                                    int newScore = Integer.parseInt(inputStr);
                                    int delta = newScore - score; // 计算分数改变的量
                                    changeScore(delta); // 使用新的 changeScore 方法来改变分数
                                } catch (NumberFormatException e) {
                                    Toast.makeText(MainActivity.this, "Invalid number format", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

        // 从数据库中获取上一次的分数
        fetchScoresFromDatabase();

        scoreText = (TextView) findViewById(R.id.score);
        updateScore();

        Button plusOne = (Button) findViewById(R.id.plusOne);
        Button plusTwo = (Button) findViewById(R.id.plusTwo);
        Button plusFive = (Button) findViewById(R.id.plusFive);
        Button plusTen = (Button) findViewById(R.id.plusTen);
        Button minusOne = (Button) findViewById(R.id.minusOne);
        Button minusTwo = (Button) findViewById(R.id.minusTwo);
        Button minusFive = (Button) findViewById(R.id.minusFive);
        Button minusTen = (Button) findViewById(R.id.minusTen);

        plusOne.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeScore(1);
            }
        });
        plusTwo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeScore(2);
            }
        });
        plusFive.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeScore(5);
            }
        });
        plusTen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeScore(10);
            }
        });
        minusOne.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeScore(-1);
            }
        });
        minusTwo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeScore(-2);
            }
        });
        minusFive.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeScore(-5);
            }
        });
        minusTen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeScore(-10);
            }
        });
    }

    // 创建一个新方法来将分数保存到数据库
    private void insertScoreToDatabase(AppDatabase db, int score, int scoreChange, int maxScore) {
        Score newScore = new Score();
        newScore.score = score;
        newScore.date = getCurrentDate();
        newScore.scoreChange = scoreChange;  // 保存分数变化
        newScore.maxScore = maxScore;  // 保存最高分

        new Thread(new Runnable() {
            @Override
            public void run() {
                db.scoreDao().insertAll(newScore);
                fetchScoresFromDatabase();
            }
        }).start();
    }

    private void updateScore() {
        scoreText.setText(String.valueOf(score));
    }

    //获取日期
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    //改变分数
    public void changeScore(int delta) {
        int oldScore = score;
        score += delta;
        int scoreChange = score - oldScore;
        int maxScore = Math.max(score, globalMaxScore);
        if (score > globalMaxScore) {
            globalMaxScore = score;
        }
        updateScore();
        insertScoreToDatabase(db, score, scoreChange, maxScore);  // 将新的分数、分数变化和最高分保存到数据库
    }


    // 从数据库中获取分数并更新ListView
    private void fetchScoresFromDatabase() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Score> scores = db.scoreDao().getAll();
                Log.d("MainActivity", "Scores from database: " + scores);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        scoreList.clear();
                        scoreList.addAll(scores);
                        Collections.sort(scoreList, (s1, s2) -> s2.date.compareTo(s1.date));
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }
}
