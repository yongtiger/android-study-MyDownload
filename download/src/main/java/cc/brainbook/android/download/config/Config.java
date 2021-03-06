package cc.brainbook.android.download.config;

public class Config {

    /**
     * 网络连接超时（缺省为10秒）
     */
    public int connectTimeout = 10000;

    /**
     * 缓冲区大小（缺省为1k字节）
     *
     * 注意：BufferedInputStream的默认缓冲区大小是8192字节，
     * 当每次读取数据量接近或远超这个值时，两者效率就没有明显差别了。
     * https://blog.csdn.net/xisuo002/article/details/78742631
     */
    public int bufferSize = 16 * 1024;

    /**
     * 下载进度的更新周期（缺省为1秒）
     */
    public int progressInterval = 1000;

}
