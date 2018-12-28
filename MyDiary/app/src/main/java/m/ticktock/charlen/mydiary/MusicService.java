package m.ticktock.charlen.mydiary;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

public class MusicService extends Service implements MediaPlayer.OnCompletionListener{
    MediaPlayer mediaPlayer;
    private final IBinder binder = new MusicBinder();
    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        if(!mediaPlayer.isPlaying()){
            // 开始播放
            mediaPlayer.start();
            // 允许循环播放
            mediaPlayer.setLooping(true);
        }
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //我们从raw目录中获取一个应用自带的mp3文件
        mediaPlayer = MediaPlayer.create(this,R.raw.bgmusic);
        mediaPlayer.setOnCompletionListener(this);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        if(!mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }
        return START_STICKY;
    }
    @Override
    public void onDestroy(){
        //super.onDestroy();
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        mediaPlayer.release();
    }
    @Override
    public void onCompletion(MediaPlayer player) {
        // TODO Auto-generated method stub
        stopSelf();//结束了。则结束Service
    }
    //为了和Activity交互，我们须要定义一个Binder对象
    class MusicBinder extends Binder {

        //返回Service对象
        MusicService getService(){
            return MusicService.this;
        }
    }
}
