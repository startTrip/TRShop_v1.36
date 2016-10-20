package shop.trqq.com.bean;

public class invoice_listBean extends Entity {

    /**
     *
     */
    private String inv_id;
    private String inv_title;
    private String inv_content;

    public String getInv_id() {
        return inv_id;
    }

    public void setInv_id(String inv_id) {
        this.inv_id = inv_id;
    }

    public String getInv_title() {
        return inv_title;
    }

    public void setInv_title(String inv_title) {
        this.inv_title = inv_title;
    }

    public String getInv_content() {
        return inv_content;
    }

    public void setInv_content(String inv_content) {
        this.inv_content = inv_content;
    }

}
