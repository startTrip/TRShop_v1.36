package shop.trqq.com.bean;

public class Msg {
    public static final int TYPE_RECEIVED = 1;
    public static final int TYPE_SENT = 0;
    private String content;
    private int type;

    public Msg(String content, int type) {
        this.content = content;
        this.type = type;
    }

    public Msg(String title, String date, String content, String image, int type) {
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }

}
