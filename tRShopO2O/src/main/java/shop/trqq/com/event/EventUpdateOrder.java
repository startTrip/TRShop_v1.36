package shop.trqq.com.event;

public class EventUpdateOrder {

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public EventUpdateOrder(String msg) {
        this.msg = msg;
    }
}
