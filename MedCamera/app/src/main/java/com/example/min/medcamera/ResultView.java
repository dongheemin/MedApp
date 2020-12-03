package com.example.min.medcamera;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.min.MedCamera.R;

public class ResultView extends AppCompatActivity {
    ImageView img1, img2, img3, img4;
    TextView textView, textView2, textView3;
    Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        textView = findViewById(R.id.name);
        textView2 = findViewById(R.id.type);
        textView3 = findViewById(R.id.classi);

        button = findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        String result = getIntent().getStringExtra("response");

        if(result.contains("Anaprox")){
            img1.setImageResource(R.drawable.anaf);
            img2.setImageResource(R.drawable.anab);
            textView.setText("AnaproxTab");
            textView2.setText("청색의 타원형 필름코팅정제");
            textView3.setText("해열, 진통, 소염제");
            img3.setImageResource(R.drawable.a1);
            img4.setImageResource(R.drawable.a2);
        }else if(result.contains("Codaewon")) {
            img1.setImageResource(R.drawable.codf);
            img2.setImageResource(R.drawable.codb);
            textView.setText("CodaewonTab");
            textView2.setText("황색의 원형 정제");
            textView3.setText("진해거담제");
            img3.setImageResource(R.drawable.b1);
            img4.setImageResource(R.drawable.b2);
        }else if(result.contains("Mucolase")) {
            img1.setImageResource(R.drawable.mucf);
            img2.setImageResource(R.drawable.mucb);
            textView.setText("MucolaseTab");
            textView2.setText("흰색의 원형 정제");
            textView3.setText("해열, 진통, 소염제");
            img3.setImageResource(R.drawable.c1);
            img4.setImageResource(R.drawable.c2);
        }else if(result.contains("Loxodifen")) {
            img1.setImageResource(R.drawable.loxf);
            img2.setImageResource(R.drawable.loxb);
            textView.setText("LoxodifenTab");
            textView2.setText("미황색의 원형 정제");
            textView3.setText("효소제제");
            img3.setImageResource(R.drawable.a1);
            img4.setImageResource(R.drawable.a2);//a
        }else if(result.contains("Mosacolin")) {
            img1.setImageResource(R.drawable.mosf);
            img2.setImageResource(R.drawable.mosb);
            textView.setText("MosacolinTab");
            textView2.setText("흰색의 장방형 필름코팅정");
            textView3.setText("기타의 소화기관용약");
            img3.setImageResource(R.drawable.b1);//b1
            img4.setVisibility(View.GONE);
        }else if(result.contains("Cefalox")) {
            img1.setImageResource(R.drawable.ceff);
            img2.setImageResource(R.drawable.cefb);
            textView.setText("CefaloxCap");
            textView2.setText("흰색 또는 황백색의 결정성 분말이 충진된 상부 암청색, 하부흰색 경질캡슐제");
            textView3.setText("주로 그람양성, 음성균에 작용하는 것");
            img3.setImageResource(R.drawable.a2);//a2
            img4.setVisibility(View.GONE);
        }else {

        }




    }
}
