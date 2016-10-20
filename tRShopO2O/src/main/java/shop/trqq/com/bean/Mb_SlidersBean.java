package shop.trqq.com.bean;

/**
 * 店铺广告轮播实体类
 *
 * @author 星河
 * @version 1.0
 * @created 2015-02-11
 */
public class Mb_SlidersBean extends Entity {

    /**
     *
     */
    private String img;
    private String imgUrl;
    private String type;
    private String link;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
