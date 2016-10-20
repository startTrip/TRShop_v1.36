package shop.trqq.com.bean;

import com.google.gson.JsonElement;

import java.util.List;

/**
 * 订单组实体类
 *
 * @author 星河
 * @version 1.0
 * @created 2015-02-11
 */
public class Store_Cart_ListBean extends Entity {

    /**
     *
     */

    private List<Goods_listBean> goods_list;

    private float store_goods_total;

    private String freight;

    private String store_freight;

    private String store_name;

    private store_mansong_rule_listBean store_mansong_rule_list;

    private JsonElement store_voucher_list;

    private String goods_weight_all;
    private String store_shipping;

    public String getGoods_weight_all() {
        return goods_weight_all;
    }

    public void setGoods_weight_all(String goods_weight_all) {
        this.goods_weight_all = goods_weight_all;
    }

    public String getStore_shipping() {
        return store_shipping;
    }

    public void setStore_shipping(String store_shipping) {
        this.store_shipping = store_shipping;
    }

    public JsonElement getStore_voucher_list() {
        return store_voucher_list;
    }

    public void setStore_voucher_list(JsonElement store_voucher_list) {
        this.store_voucher_list = store_voucher_list;
    }

    public List<Goods_listBean> getGoods_lists() {
        return goods_list;
    }

    public void setGoods_lists(List<Goods_listBean> goods_lists) {
        this.goods_list = goods_lists;
    }

    public float getStore_goods_total() {
        return store_goods_total;
    }

    public void setStore_goods_total(float store_goods_total) {
        this.store_goods_total = store_goods_total;
    }

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public List<Goods_listBean> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(List<Goods_listBean> goods_list) {
        this.goods_list = goods_list;
    }

    public store_mansong_rule_listBean getStore_mansong_rule_list() {
        return store_mansong_rule_list;
    }

    public void setStore_mansong_rule_list(store_mansong_rule_listBean store_mansong_rule_list) {
        this.store_mansong_rule_list = store_mansong_rule_list;
    }

    public String getStore_freight() {
        return store_freight;
    }

    public void setStore_freight(String store_freight) {
        this.store_freight = store_freight;
    }
}
