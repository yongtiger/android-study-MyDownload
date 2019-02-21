package cc.brainbook.study.mydownload.download.httpurlconnection.simple;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.nio.channels.FileChannel;

import android.os.Handler;

import cc.brainbook.study.mydownload.download.httpurlconnection.simple.base.BaseDownloadThread;
import cc.brainbook.study.mydownload.download.httpurlconnection.simple.bean.FileInfo;
import cc.brainbook.study.mydownload.download.httpurlconnection.simple.config.Config;
import cc.brainbook.study.mydownload.download.httpurlconnection.simple.exception.DownloadException;
import cc.brainbook.study.mydownload.download.httpurlconnection.simple.handler.DownloadHandler;
import cc.brainbook.study.mydownload.download.httpurlconnection.util.Util;

public class DownloadThread extends BaseDownloadThread {
    private static final String TAG = "TAG";

    public DownloadThread(FileInfo fileInfo, Config config, Handler handler, boolean hasOnProgressListener) {
        super(fileInfo, config, handler, hasOnProgressListener);
    }

    @Override
    public void run() {
        super.run();

        ///由下载文件的URL网址建立Http网络连接connection
        HttpURLConnection connection = openConnection(mFileInfo.getFileUrl());

        ///如果网络连接connection的响应码为200，则开始下载过程
        handleResponseCode(connection);

        ///获得下载文件名
        if (mFileInfo.getFileName().isEmpty()) {
            mFileInfo.setFileName(Util.getUrlFileName(connection));
            if (mFileInfo.getFileName().isEmpty()) {
                throw new DownloadException(DownloadException.EXCEPTION_FILE_NAME_NULL, "The file name cannot be null.");
            }
        }
        ///获得文件长度（建议用long类型，int类型最大为2GB）
        mFileInfo.setFileSize(connection.getContentLength());

        ///获得网络连接connection的输入流对象
        BufferedInputStream bufferedInputStream = getBufferedInputStream(connection);

        ///创建文件输出流对象
        File saveFile = new File(mFileInfo.getSavePath(), mFileInfo.getFileName());
        FileOutputStream fileOutputStream = getFileOutputStream(saveFile);
        FileChannel channel = fileOutputStream.getChannel();

        ///发送消息：下载开始
        mHandler.obtainMessage(DownloadHandler.MSG_START).sendToTarget();

        ///控制更新下载进度的周期
        long currentTimeMillis = System.currentTimeMillis();
        long currentFinishedBytes = mFileInfo.getFinishedBytes();

        ///输入流每次读取的内容（字节缓冲区）
        ///BufferedInputStream的默认缓冲区大小是8192字节。当每次读取数据量接近或远超这个值时，两者效率就没有明显差别了
        ///https://blog.csdn.net/xisuo002/article/details/78742631
        byte[] bytes = new byte[mConfig.bufferSize];
        ///每次循环读取的内容长度，如为-1表示输入流已经读取结束
        int readLength;
        while ((readLength = inputStreamRead(bufferedInputStream, bytes)) != -1) {
            ///写入字节缓冲区内容到文件输出流
            channelWrite(channel, bytes, readLength);

            mFileInfo.setFinishedBytes(mFileInfo.getFinishedBytes() + readLength);
            Log.d(TAG, "DownloadThread#run(): thread name is: " + Thread.currentThread().getName());
            Log.d(TAG, "DownloadThread#run()#finishedBytes: " + mFileInfo.getFinishedBytes() + ", fileSize: " + mFileInfo.getFileSize());

            if (mHasOnProgressListener) {
                ///控制更新下载进度的周期
                if (System.currentTimeMillis() - currentTimeMillis > mConfig.progressInterval) {
                    mFileInfo.setDiffTimeMillis(System.currentTimeMillis() - currentTimeMillis);   ///下载进度的时间（毫秒）
                    currentTimeMillis = System.currentTimeMillis();
                    mFileInfo.setDiffFinishedBytes(mFileInfo.getFinishedBytes() - currentFinishedBytes);  ///下载进度的下载字节数
                    currentFinishedBytes = mFileInfo.getFinishedBytes();
                    ///发送消息：更新下载进度
                    mHandler.obtainMessage(DownloadHandler.MSG_PROGRESS).sendToTarget();
                }
            }

            ///停止下载线程
            if (mFileInfo.getStatus() == FileInfo.FILE_STATUS_STOP) {
                ///发送消息：下载停止
                Log.d(TAG, "DownloadThread#run()#mFileInfo.getStatus(): FILE_STATUS_STOP");
                mHandler.obtainMessage(DownloadHandler.MSG_STOP).sendToTarget();
                return;
            }
        }

        mFileInfo.setStatus(FileInfo.FILE_STATUS_COMPLETE);

        ///发送消息：下载完成
        Log.d(TAG, "DownloadThread#run()#mHandler.obtainMessage(MSG_COMPLETE, mFileInfo).sendToTarget();");
        mHandler.obtainMessage(DownloadHandler.MSG_COMPLETE).sendToTarget();

        ///关闭连接
        connection.disconnect();
        ///关闭流Closeable
        Util.closeIO(bufferedInputStream, fileOutputStream);
    }

}
