package m.ticktock.charlen.mydiary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditDiaryAvtivity extends AppCompatActivity {

    private EditText textTitle;
    private EditText textDate;
    private EditText textDiaryContent;

    private Button buttonSave;
    private Button buttonDelete;
    private Button buttonCancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_diary_avtivity);
        final Diary diary=(Diary)getIntent().getSerializableExtra("selectedDiary");//获取被选中的日记对象
        final int id=diary.getId();//获取其id号

        textTitle=(EditText)findViewById(R.id.watchPage_title);//标题框
        textDate=(EditText)findViewById(R.id.watchPage_date);//日期框(不可更改)
        textDiaryContent=(EditText)findViewById(R.id.watchPage_diaryContent);//内容框

        buttonSave=(Button)findViewById(R.id.buttonSaveEdit);
        buttonDelete=(Button)findViewById(R.id.buttonDeleteDiary);
        buttonCancel=(Button)findViewById(R.id.buttonCancelEdit);

        //填入选中日记对象的值，供读者观察
        textTitle.setText(diary.getTitle());
        textDate.setText(diary.getDate());
        textDiaryContent.setText(diary.getDiaryContent());


        //取消按钮事件处理
        buttonCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EditDiaryAvtivity.this, MainActivity.class);
                startActivity(intent);
            }
        });//取消按钮事件处理结束


        //删除按钮事件处理
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataSupport.deleteAll(Diary.class, "id=?", diary.getId()+"");
                //删除后返回主页面
                Intent intent=new Intent(EditDiaryAvtivity.this, MainActivity.class);
                startActivity(intent);
            }
        });//删除按钮事件处理结束


        //保存按钮事件处理
        buttonSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Diary diary=new Diary();//新建Diary类对象，将输入信息封装进其中
                String title=textTitle.getText().toString();//获取标题
                String date=getCurrentDate();//获取时间
                String diaryContent=textDiaryContent.getText().toString();//获取内容
                diary.setTitle(title);
                diary.setDate(date);
                diary.setDiaryContent(diaryContent);
                /*diary.save();//保存设置*/
                diary.updateAll("id=?", id+"");//根据id号删除旧日记对象

                //返回首页
                Intent intent=new Intent(EditDiaryAvtivity.this, MainActivity.class);
                startActivity(intent);
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
