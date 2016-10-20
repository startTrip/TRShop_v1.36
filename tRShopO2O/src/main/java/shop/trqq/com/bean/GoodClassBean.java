package shop.trqq.com.bean;

/**
 * 订单组实体类
 *
 * @author 星河
 * @version 1.0
 * @created 2015-02-11
 */
public class GoodClassBean extends Entity {
    /**
     *
     */
    private String gc_id;
    private String gc_name;
    private String image;
    private String text;

    public String getGc_id() {
        return gc_id;
    }

    public void setGc_id(String gc_id) {
        this.gc_id = gc_id;
    }

    public String getGc_name() {
        return gc_name;
    }

    public void setGc_name(String gc_name) {
        this.gc_name = gc_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
