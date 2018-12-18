package com.linglong.lame;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.xutils.DbManager;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "MainActivityRecord";
    //录音所保存的文件
    private File mAudioFile;
    private short[] mBuffer;
    private FileOutputStream mFileOutPutStream;
    //文件流录音API
    private AudioRecord mAudioRecord;
    private static final int BUFFER_SIZE = 2049;
    //线程操作
    private ExecutorService mExecutorService;
    private String mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audio2/";

    private Lame lame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mExecutorService = Executors.newSingleThreadExecutor();
        mBuffer = new short[BUFFER_SIZE];
        lame=new Lame();

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setOnClickListener(this);
        test();
    }


    private void test(){
        DbManager.DaoConfig config = new DbManager.DaoConfig();
    }

    private boolean record=false;

    @Override
    public void onClick(View v) {
        if (record){
            record = false;
        }else {
            record =true;

            mExecutorService.submit(new Runnable() {
                @Override
                public void run() {
                    startRecord();
                }
            });
        }
    }

    private void startRecord(){
        lame.init(AudioFormat.CHANNEL_IN_MONO,44100,128);
        try {
            //创建录音文件,.m4a为MPEG-4音频标准的文件的扩展名
            mAudioFile = new File(mFilePath + System.currentTimeMillis() + ".mp3");
            //创建父文件夹
            mAudioFile.getParentFile().mkdirs();
            //创建文件
            mAudioFile.createNewFile();
            //创建文件输出流
            mFileOutPutStream = new FileOutputStream(mAudioFile);
            //配置AudioRecord
            //从麦克风采集数据
            int audioSource = MediaRecorder.AudioSource.MIC;
            //设置采样频率
            int sampleRate = 44100;
            //设置单声道输入
            int channelConfig = AudioFormat.CHANNEL_IN_MONO;
            //设置格式，安卓手机都支持的是PCM16
            int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
            //计算AudioRecord内部buffer大小
            int minBufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
            //根据上面的设置参数初始化AudioRecord
            mAudioRecord = new AudioRecord(audioSource, sampleRate, channelConfig, audioFormat, Math.max(minBufferSize*2, BUFFER_SIZE));

            //开始录音
            mAudioRecord.startRecording();
            //记录开始时间
            //写入数据到文件
            while (record) {
                int read = mAudioRecord.read(mBuffer, 0, BUFFER_SIZE);
                if (read > 0) {
                    byte [] buf=lame.encode(mBuffer,read);
                    //保存到指定文件
                    mFileOutPutStream.write(buf, 0, buf.length);
                    Log.i(TAG,"read:"+read+"---buf:"+buf.length);
                }
            }
        }catch (Exception e){
            Log.i(TAG,"Exception:"+e.getMessage());
        }finally {
            stopRecord();
            Log.i(TAG,"mAudioFile:"+mAudioFile.getAbsolutePath());
            lame.destroy();
        }
    }

    private void stopRecord(){
        try {

            mAudioRecord.stop();
            mAudioRecord.release();
            mAudioRecord = null;
            mFileOutPutStream.flush();
            mFileOutPutStream.close();
            mFileOutPutStream=null;
        }catch (Exception e){
        }
    }
}
