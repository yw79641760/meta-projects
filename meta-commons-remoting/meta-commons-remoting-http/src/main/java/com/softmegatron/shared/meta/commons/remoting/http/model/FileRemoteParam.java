package com.softmegatron.shared.meta.commons.remoting.http.model;

/**
 * FileRemoteParam
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 5/4/20 3:19 PM
 */
public class FileRemoteParam extends HttpRemoteParam{

    private static final long serialVersionUID = -2731242280034546407L;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 媒体类型
     */
    private String mimeType;

    public FileRemoteParam() {
        super();
    }

    public FileRemoteParam(String name, Object argument, String fileName, String mimeType) {
        super(name, argument);
        this.fileName = fileName;
        this.mimeType = mimeType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public static FileRemoteParam ofData(String fileName, String mimeType,
                                         String name, Object argument) {
        return new FileRemoteParam(name, argument, fileName, mimeType);
    }
}
