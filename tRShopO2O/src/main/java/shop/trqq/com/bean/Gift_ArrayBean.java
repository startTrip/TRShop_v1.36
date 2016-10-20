package shop.trqq.com.bean;

/**
 * 礼物实体类
 *
 * @author 星河
 * @version 1.0
 * @created 2015-02-11
 */
public class Gift_ArrayBean extends Entity {
    /**
     *
     */

    private String gift_id;

    private String goods_id;

    private String goods_commonid;

    private String gift_goodsid;

    private String gift_goodsname;

    private String gift_goodsimage;

    private String gift_amount;

    public String getGift_id() {
        return gift_id;
    }

    public void setGift_id(String gift_id) {
        this.gift_id = gift_id;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_commonid() {
        return goods_commonid;
    }

    public void setGoods_commonid(String goods_commonid) {
        this.goods_commonid = goods_commonid;
    }

    public String getGift_goodsid() {
        return gift_goodsid;
    }

    public void setGift_goodsid(String gift_goodsid) {
        this.gift_goodsid = gift_goodsid;
    }

    public String getGift_goodsname() {
        return gift_goodsname;
    }

    public void setGift_goodsname(String gift_goodsname) {
        this.gift_goodsname = gift_goodsname;
    }

    public String getGift_goodsimage() {
        return gift_goodsimage;
    }

    public void setGift_goodsimage(String gift_goodsimage) {
        this.gift_goodsimage = gift_goodsimage;
    }

    public String getGift_amount() {
        return gift_amount;
    }

    public void setGift_amount(String gift_amount) {
        this.gift_amount = gift_amount;
    }

}
