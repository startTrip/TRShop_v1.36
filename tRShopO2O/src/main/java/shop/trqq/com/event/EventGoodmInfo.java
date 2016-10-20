package shop.trqq.com.event;

public class EventGoodmInfo {

    private String goods_id;
    private String msg;

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public EventGoodmInfo(String goods_id, String msg) {
        this.goods_id = goods_id;
        this.msg = msg;
    }
}
