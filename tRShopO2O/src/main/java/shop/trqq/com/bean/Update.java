package shop.trqq.com.bean;

import java.io.Serializable;


public class Update implements Serializable {

    /**
     *
     */
    public final static String UTF8 = "UTF-8";
    public final static String NODE_ROOT = "trqq";

    private float version;
    private String url;

    public float getVersion() {
        return version;
    }

    public void setVersion(float version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
