package shop.trqq.com.bean;

/**
 * 商品实体类
 *
 * @author 星河
 * @version 1.0
 * @created 2015-02-11
 */
public class Order_goodsBean extends Entity {

    /**
     *
     */
    private String buyer_id;
    private String commis_rate;
    private String goods_id;
    private String goods_image;
    private String goods_image_url;
    private String goods_name;
    private String goods_num;
    private String goods_pay_price;
    private String goods_price;
    private String goods_type;
    private String order_id;
    private String promotions_id;
    private String rec_id;
    private String store_id;

    public String getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(String buyer_id) {
        this.buyer_id = buyer_id;
    }

    public String getCommis_rate() {
        return commis_rate;
    }

    public void setCommis_rate(String commis_rate) {
        this.commis_rate = commis_rate;
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

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(String goods_num) {
        this.goods_num = goods_num;
    }

    public String getGoods_pay_price() {
        return goods_pay_price;
    }

    public void setGoods_pay_price(String goods_pay_price) {
        this.goods_pay_price = goods_pay_price;
    }

    public String getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    public String getGoods_type() {
        return goods_type;
    }

    public void setGoods_type(String goods_type) {
        this.goods_type = goods_type;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPromotions_id() {
        return promotions_id;
    }

    public void setPromotions_id(String promotions_id) {
        this.promotions_id = promotions_id;
    }

    public String getRec_id() {
        return rec_id;
    }

    public void setRec_id(String rec_id) {
        this.rec_id = rec_id;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }
}
