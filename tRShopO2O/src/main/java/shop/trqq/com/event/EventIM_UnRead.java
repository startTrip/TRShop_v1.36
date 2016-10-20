package shop.trqq.com.event;

public class EventIM_UnRead {

    private int u_id;
    private int unReadNum;


    public int getU_id() {
        return u_id;
    }


    public void setU_id(int u_id) {
        this.u_id = u_id;
    }


    public int getUnReadNum() {
        return unReadNum;
    }


    public void setUnReadNum(int unReadNum) {
        this.unReadNum = unReadNum;
    }


    public EventIM_UnRead(int u_id, int unReadNum) {
        this.u_id = u_id;
        this.unReadNum = unReadNum;
    }
}
