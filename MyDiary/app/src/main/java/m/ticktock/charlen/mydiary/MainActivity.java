package m.ticktock.charlen.mydiary;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

//数据库：DbDiary
//表：Diary

public class MainActivity extends AppCompatActivity {
    SearchView searchView;
    private DragFloatActionButton dragFloatActionButton;
    private MusicService musicService;
    private List<m.ticktock.charlen.mydiary.Diary> diaryList = new ArrayList<>();
    //使用ServiceConnection来监听Service状态的变化
    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            musicService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            //这里我们实例化audioService,通过binder来实现
            musicService = ((MusicService.MusicBinder) binder).getService();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //开启背景音乐
        Intent intent = new Intent();
        intent.setClass(this, MusicService.class);
        startService(intent);

//        SQLiteDatabase db = Connector.getDatabase();
        LitePal.getDatabase();//创建数据库 DbDiary

        diaryList = DataSupport.findAll(Diary.class);

        DiaryAdapter adapter = new DiaryAdapter(MainActivity.this, R.layout.diary_item, diaryList);
        final ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        //如果点击列表中的某项，就跳转到观察日记页WatchDiaryAvtivity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                m.ticktock.charlen.mydiary.Diary diary = diaryList.get(position);
                Intent intent = new Intent(MainActivity.this, m.ticktock.charlen.mydiary.EditDiaryAvtivity.class);
                intent.putExtra("selectedDiary", diary);//将选中的日记对象传递到活动EditDiaryActivity
                startActivity(intent);
            }
        });//跳转到观察日记页WatchDiaryAvtivity处理结束

        dragFloatActionButton= (DragFloatActionButton) findViewById(R.id.floatBtn);
        dragFloatActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu=new PopupMenu(MainActivity.this,v);
                getMenuInflater().inflate(R.menu.pop_item,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.action_add:       //添加日记
                                Intent intent = new Intent(MainActivity.this, m.ticktock.charlen.mydiary.AddDiaryActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.action_search:    //搜索日记
                                Intent intent1=new Intent(MainActivity.this, m.ticktock.charlen.mydiary.AddDiaryActivity.class);
                                startActivity(intent1);
                                break;
                            case R.id.action_delete:    //删除所有
                                DataSupport.deleteAll("Diary");
                                //删除后返回主页面
                                Intent intent2=new Intent(MainActivity.this, MainActivity.class);
                                startActivity(intent2);
                                break;
                        }

                        return false;
                    }
                });
                popupMenu.show();
                Log.e("****--->","float");
                // Toast.makeText(this,"flaot---",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //使用菜单填充器获取menu下的菜单资源文件
        getMenuInflater().inflate(R.menu.search_diary,menu);
        //获取搜索的菜单组件
        MenuItem menuItem = menu.findItem(R.id.search);
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        //设置搜索的事件
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                List<Diary> all = DataSupport.where("title=?",query).find(Diary.class);
                DiaryAdapter adapter = new DiaryAdapter(MainActivity.this, R.layout.diary_item, all);
                final ListView listView = (ListView) findViewById(R.id.list_view);
                listView.setAdapter(adapter);

//                for (int i = 0; i < all.size(); i++) {
//                    Log.e(TAG, "query: " + all.get(i).toString());
//                }

//                Toast t = Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT);
//                t.setGravity(Gravity.TOP,0,0);
//                t.show();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        //获取分享的菜单子组件
        MenuItem shareItem = menu.findItem(R.id.share);
        ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        //通过setShareIntent调用getDefaultIntent()获取所有具有分享功能的App
        shareActionProvider.setShareIntent(getDefaultIntent());
        return super.onCreateOptionsMenu(menu);
    }
    //设置可以调用手机内所有可以分享图片的应用
    private Intent getDefaultIntent() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        //这里的类型可以按需求设置
        intent.setType("image/*");
        return intent;
    }
}
