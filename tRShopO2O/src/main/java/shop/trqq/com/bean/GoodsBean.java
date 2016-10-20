package shop.trqq.com.bean;

/**
 * 商品实体类
 *
 * @author 星河
 * @version 1.0
 * @created 2015-02-11
 */
public class GoodsBean extends Entity {

    /**
     *
     */

    // 商品名称
    private String goods_name;
    // 商品id
    private String goods_id;
    // 商品id
    private String fav_id;
    // 是否虚拟物品
    private String is_virtual;
    // 商品商场价
    private String goods_marketprice;
    // 商品促销价
    private String goods_promotion_price;
    // 商品图片
    private String goods_image_url;
    // 商品图片名称
    private String goods_image;
    // 商品价格
    private String goods_price;
    // 评论条数
    private int evaluation_count;
    // 销量
    private String goods_salenum;
    // 星级
    private float evaluation_good_star;
    // 是否抢购
    private Boolean group_flag;
    // 是否限时折扣
    private Boolean xianshi_flag;
    // 是否为F码商品
    private String is_fcode;
    // 是否是预约商品
    private String is_appoint;
    // 是否是预售商品
    private String is_presell;
    // 是否拥有赠品
    private String have_gift;
    // 店铺商品图片
    private String url;
    //首页商品排行图片地址
    private String goods_pic;
    //首页商品新品图片地址
    private String pic_img;
    //首页商品新品商品id
    private String pic_url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getGroup_flag() {
        return group_flag;
    }

    public void setGroup_flag(Boolean group_flag) {
        this.group_flag = group_flag;
    }

    public Boolean getXianshi_flag() {
        return xianshi_flag;
    }

    public void setXianshi_flag(Boolean xianshi_flag) {
        this.xianshi_flag = xianshi_flag;
    }

    public String getIs_fcode() {
        return is_fcode;
    }

    public void setIs_fcode(String is_fcode) {
        this.is_fcode = is_fcode;
    }

    public String getIs_appoint() {
        return is_appoint;
    }

    public void setIs_appoint(String is_appoint) {
        this.is_appoint = is_appoint;
    }

    public String getIs_presell() {
        return is_presell;
    }

    public void setIs_presell(String is_presell) {
        this.is_presell = is_presell;
    }

    public String getHave_gift() {
        return have_gift;
    }

    public void setHave_gift(String have_gift) {
        this.have_gift = have_gift;
    }

    public String getGoods_image() {
        return goods_image;
    }

    public void setGoods_image(String goods_image) {
        this.goods_image = goods_image;
    }

    public String getGoods_promotion_price() {
        return goods_promotion_price;
    }

    public void setGoods_promotion_price(String goods_promotion_price) {
        this.goods_promotion_price = goods_promotion_price;
    }

    public String getFav_id() {
        return fav_id;
    }

    public void setFav_id(String fav_id) {
        this.fav_id = fav_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getIs_virtual() {
        return is_virtual;
    }

    public void setIs_virtual(String is_virtual) {
        this.is_virtual = is_virtual;
    }

    public String getGoods_marketprice() {
        return goods_marketprice;
    }

    public void setGoods_marketprice(String goods_marketprice) {
        this.goods_marketprice = goods_marketprice;
    }

    public String getGoods_image_url() {
        return goods_image_url;
    }

    public void setGoods_image_url(String goods_image_url) {
        this.goods_image_url = goods_image_url;
    }

    public String getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    public int getEvaluation_count() {
        return evaluation_count;
    }

    public void setEvaluation_count(int evaluation_count) {
        this.evaluation_count = evaluation_count;
    }

    public String getGoods_salenum() {
        return goods_salenum;
    }

    public void setGoods_salenum(String goods_salenum) {
        this.goods_salenum = goods_salenum;
    }

    public Float getEvaluation_good_star() {
        return evaluation_good_star;
    }

    public void setEvaluation_good_star(float evaluation_good_star) {
        this.evaluation_good_star = evaluation_good_star;
    }

    public String getGoods_pic() {
        return goods_pic;
    }

    public void setGoods_pic(String goods_pic) {
        this.goods_pic = goods_pic;
    }

    public String getPic_img() {
        return pic_img;
    }

    public void setPic_img(String pic_img) {
        this.pic_img = pic_img;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }


}
