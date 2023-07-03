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
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView scoreText;
    private int score = 0;
    private AppDatabase db; // 作为成员变量定义

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

        //找到Fragment和editScore的按钮
        Button openFragment = (Button) findViewById(R.id.openFragment);
        Button editScore = (Button) findViewById(R.id.editScore);

        openFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openScoreFragment();
            }
        });

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
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Score> scores = db.scoreDao().getAll();
                Log.d("MainActivity", "Scores from database: " + scores);
                if (!scores.isEmpty()) {
                    Score lastScore = scores.get(scores.size() - 1);
                    score = lastScore.score;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateScore();
                            // 在获取分数并更新 UI 后，插入新的分数
                            insertScoreToDatabase(db, score);
                        }
                    });
                } else {
                    // 如果没有获取到分数（例如，在第一次运行应用时），插入一个初始分数
                    insertScoreToDatabase(db, score);
                }
            }
        }).start();

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
    private void insertScoreToDatabase(AppDatabase db, int score) {
        Score newScore = new Score();
        newScore.score = score;
        newScore.date = getCurrentDate();

        new Thread(new Runnable() {
            @Override
            public void run() {
                db.scoreDao().insertAll(newScore);
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

    private void openScoreFragment() {
        ScoreFragment fragment = new ScoreFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    //改变分数
    public void changeScore(int delta) {
        score += delta;
        if (score > globalMaxScore) {
            globalMaxScore = score;
        }
        if (score < globalMinScore) {
            globalMinScore = score;
        }
        updateScore();
        insertScoreToDatabase(db, score);  // 将新的分数保存到数据库
        openScoreFragment();    //更新Fragment
    }
}
