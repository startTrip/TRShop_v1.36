package shop.trqq.com.bean;

/**
 * 虚拟订单可用码实体类
 *
 * @author 星河
 * @version 1.0
 * @created 2015-02-11
 */
public class Indate_CodeBean extends Entity {
    /**
     *
     */
    private String vr_code;
    private String vr_indate;

    public String getVr_code() {
        return vr_code;
    }

    public void setVr_code(String vr_code) {
        this.vr_code = vr_code;
    }

    public String getVr_indate() {
        return vr_indate;
    }

    public void setVr_indate(String vr_indate) {
        this.vr_indate = vr_indate;
    }

}
