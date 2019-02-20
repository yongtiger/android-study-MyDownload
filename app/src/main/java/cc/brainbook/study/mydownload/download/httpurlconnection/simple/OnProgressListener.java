package cc.brainbook.study.mydownload.download.httpurlconnection.simple;

import cc.brainbook.study.mydownload.download.httpurlconnection.simple.bean.FileInfo;

/**
 * 下载进度监听器
 */
public interface OnProgressListener {

    /**
     * 下载进度的监听回调方法
     *
     * @param fileInfo
     */
    void onProgress(FileInfo fileInfo);

}
