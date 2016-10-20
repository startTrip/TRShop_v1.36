package shop.trqq.com.bean;

/**
 * 资讯实体类
 *
 * @author 星河
 * @version 1.0
 * @created 2015-02-11
 */
public class Home1Bean extends Entity {

    /**
     *
     */
    private String title;
    private String image;
    private String type;
    private String data;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
