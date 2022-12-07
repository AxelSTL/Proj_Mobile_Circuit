package com.example.book2run;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.book2run.ui.data.LoginDataSource;
import com.example.book2run.ui.data.LoginRepository;

public class CommentaryPost extends AppCompatActivity implements View.OnClickListener{

    ImageView stars1,stars2,stars3,stars4,stars5;
    Button validate;
    EditText message;
    private LoginRepository user = LoginRepository.getInstance(new LoginDataSource());
    int circuitCode;
    TextView circuitName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commentary_post);
        stars1 = findViewById(R.id.stars1_PostCommentary);
        stars2 = findViewById(R.id.stars2_PostCommentary);
        stars3 = findViewById(R.id.stars3_PostCommentary);
        stars4 = findViewById(R.id.stars4_PostCommentary);
        stars5 = findViewById(R.id.stars5_PostCommentary);
        validate = findViewById(R.id.validate_postcommentary);
        circuitName = findViewById(R.id.circuitname_postCommentary);
        message = findViewById(R.id.avis_PostCommentary);
        circuitCode = getIntent().getIntExtra("code", 0);
        circuitName.setText(getIntent().getStringExtra("nom"));


        stars1.setOnClickListener(this);
        stars2.setOnClickListener(this);
        stars3.setOnClickListener(this);
        stars4.setOnClickListener(this);
        stars5.setOnClickListener(this);

        stars1.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {

                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.stars1_PostCommentary:
                stars1.setBackgroundColor(Color.rgb(255,255,0));
                stars2.setBackgroundColor(Color.rgb(255,255,255));
                stars3.setBackgroundColor(Color.rgb(255,255,255));
                stars4.setBackgroundColor(Color.rgb(255,255,255));
                stars5.setBackgroundColor(Color.rgb(255,255,255));
                break;
            case R.id.stars2_PostCommentary:
                stars1.setBackgroundColor(Color.rgb(255,255,0));
                stars2.setBackgroundColor(Color.rgb(255,255,0));
                stars3.setBackgroundColor(Color.rgb(255,255,255));
                stars4.setBackgroundColor(Color.rgb(255,255,255));
                stars5.setBackgroundColor(Color.rgb(255,255,255));
                break;
            case R.id.stars3_PostCommentary:
                stars1.setBackgroundColor(Color.rgb(255,255,0));
                stars2.setBackgroundColor(Color.rgb(255,255,0));
                stars3.setBackgroundColor(Color.rgb(255,255,0));
                stars4.setBackgroundColor(Color.rgb(255,255,255));
                stars5.setBackgroundColor(Color.rgb(255,255,255));
                break;
            case R.id.stars4_PostCommentary:
                stars1.setBackgroundColor(Color.rgb(255,255,0));
                stars2.setBackgroundColor(Color.rgb(255,255,0));
                stars3.setBackgroundColor(Color.rgb(255,255,0));
                stars4.setBackgroundColor(Color.rgb(255,255,0));
                stars5.setBackgroundColor(Color.rgb(255,255,255));
                break;
            case R.id.stars5_PostCommentary:
                stars1.setBackgroundColor(Color.rgb(255,255,0));
                stars2.setBackgroundColor(Color.rgb(255,255,0));
                stars3.setBackgroundColor(Color.rgb(255,255,0));
                stars4.setBackgroundColor(Color.rgb(255,255,0));
                stars5.setBackgroundColor(Color.rgb(255,255,0));
                break;
        }

    }
}