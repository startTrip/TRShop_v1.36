package shop.trqq.com.bean;

/**
 * 活动实体类
 *
 * @author 星河
 * @version 1.0
 * @created 2015-02-11
 */
public class Mansong_RulesBean extends Entity {
    /**
     *
     */

    private String rule_id;

    private String mansong_id;

    private String price;

    private String discount;

    private String mansong_goods_name;

    private String goods_id;

    private String goods_image;

    private String goods_image_url;

    private String goods_storage;

    private String goods_url;

    public String getRule_id() {
        return rule_id;
    }

    public void setRule_id(String rule_id) {
        this.rule_id = rule_id;
    }

    public String getMansong_id() {
        return mansong_id;
    }

    public void setMansong_id(String mansong_id) {
        this.mansong_id = mansong_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getMansong_goods_name() {
        return mansong_goods_name;
    }

    public void setMansong_goods_name(String mansong_goods_name) {
        this.mansong_goods_name = mansong_goods_name;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_image() {
        return goods_image;
    }

    public void setGoods_image(String goods_image) {
        this.goods_image = goods_image;
    }

    public String getGoods_image_url() {
        return goods_image_url;
    }

    public void setGoods_image_url(String goods_image_url) {
        this.goods_image_url = goods_image_url;
    }

    public String getGoods_storage() {
        return goods_storage;
    }

    public void setGoods_storage(String goods_storage) {
        this.goods_storage = goods_storage;
    }

    public String getGoods_url() {
        return goods_url;
    }

    public void setGoods_url(String goods_url) {
        this.goods_url = goods_url;
    }

}
