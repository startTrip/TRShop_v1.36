package shop.trqq.com.bean;

import java.io.Serializable;

public class MsgBean implements Serializable {
    /**
     *
     */
    public static final int TYPE_RECEIVED = 1;
    public static final int TYPE_SENT = 0;
    private String content;
    private int type;
    private String date;
    private String image;
    private String title;

    private String clicktype;
    private String clicktypedata;

    public String getClicktype() {
        return clicktype;
    }

    public void setClicktype(String clicktype) {
        this.clicktype = clicktype;
    }

    public String getClicktypedata() {
        return clicktypedata;
    }

    public void setClicktypedata(String clicktypedata) {
        this.clicktypedata = clicktypedata;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
