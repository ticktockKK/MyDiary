package m.ticktock.charlen.mydiary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddDiaryActivity extends AppCompatActivity {

    private EditText addPage_title;
    private EditText addPage_diaryContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);

        Button buttonConfirm=(Button)findViewById(R.id.buttonConfirm);//添加页确认按钮
        Button buttonCancel=(Button)findViewById(R.id.buttonCancel);//添加页取消按钮
        addPage_title=(EditText) findViewById(R.id.addPage_title);
        addPage_diaryContent=(EditText)findViewById(R.id.addPage_diaryContent);

        //取消按钮：返回用户首页
        buttonCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddDiaryActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });//取消按钮处理结束

        //确认按钮：将输入的信息添加进数据库中
        buttonConfirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Diary diary=new Diary();//新建Diary类对象，将输入信息封装进其中
                String title=addPage_title.getText().toString();//获取标题
                if(title.length()==0){
                    Toast.makeText(AddDiaryActivity.this, "你还没有输入标题呢!", Toast.LENGTH_SHORT).show();
                }else {
                    String date = getCurrentDate();
                    String diaryContent = addPage_diaryContent.getText().toString();//获取内容
                    diary.setTitle(title);
                    diary.setDate(date);
                    diary.setDiaryContent(diaryContent);
                    diary.save();//保存设置
                    Intent intent = new Intent(AddDiaryActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            public String getCurrentDate(){
                String currentDate="";
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                Date date=new Date(System.currentTimeMillis());
                currentDate=simpleDateFormat.format(date);
                return currentDate;
            }
        });
    }
}
