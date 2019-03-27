package cc.brainbook.android.download.exception;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DownloadException extends RuntimeException {
    public static final int EXCEPTION_FILE_URL_NULL = 0;
    public static final int EXCEPTION_FILE_NAME_NULL = 1;
    public static final int EXCEPTION_FILE_NOT_FOUND = 2;
    public static final int EXCEPTION_FILE_IO_EXCEPTION = 3;
    public static final int EXCEPTION_NETWORK_MALFORMED_URL = 4;
    public static final int EXCEPTION_NETWORK_UNKNOWN_HOST = 5;
    public static final int EXCEPTION_NETWORK_IO_EXCEPTION = 6;
    public static final int EXCEPTION_NETWORK_PROTOCOL_EXCEPTION = 7;
    public static final int EXCEPTION_NETWORK_RESPONSE_CODE_EXCEPTION = 8;
    public static final int EXCEPTION_SAVE_PATH_MKDIR = 9;

    private int code;

    public DownloadException(@ExceptionType int code) {
        this.code = code;
    }

    public DownloadException(@ExceptionType int code, String message) {
        super(message);
        this.code = code;
    }

    public DownloadException(@ExceptionType int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public DownloadException(@ExceptionType int code, Throwable cause) {
        super(cause);
        this.code = code;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    /**
     * Download exception type.
     */
    @IntDef({EXCEPTION_FILE_URL_NULL,
            EXCEPTION_FILE_NAME_NULL,
            EXCEPTION_FILE_NOT_FOUND,
            EXCEPTION_FILE_IO_EXCEPTION,
            EXCEPTION_NETWORK_MALFORMED_URL,
            EXCEPTION_NETWORK_UNKNOWN_HOST,
            EXCEPTION_NETWORK_IO_EXCEPTION,
            EXCEPTION_NETWORK_PROTOCOL_EXCEPTION,
            EXCEPTION_NETWORK_RESPONSE_CODE_EXCEPTION,
            EXCEPTION_SAVE_PATH_MKDIR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ExceptionType {}

}
