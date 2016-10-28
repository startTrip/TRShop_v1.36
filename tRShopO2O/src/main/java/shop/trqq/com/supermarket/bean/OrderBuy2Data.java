package shop.trqq.com.supermarket.bean;

import java.io.Serializable;

/**
 * Project: TRShop_v1.36
 * Author: wm
 * Data:   2016/10/27
 */
public class OrderBuy2Data implements Serializable {

    /**
     * pay_sn : 650530897774818654
     * order_sn : 8000000004169601
     * add_time : 1477553774
     * store_name : 万能居生鲜超市
     * store_all_weight : 22
     * shipping_fee : 7
     * order_amount : 57
     */

    private String pay_sn;
    private String order_sn;
    private String add_time;
    private String store_name;
    private String store_all_weight;
    private String shipping_fee;
    private String order_amount;

    public String getPay_sn() {
        return pay_sn;
    }

    public void setPay_sn(String pay_sn) {
        this.pay_sn = pay_sn;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_all_weight() {
        return store_all_weight;
    }

    public void setStore_all_weight(String store_all_weight) {
        this.store_all_weight = store_all_weight;
    }

    public String getShipping_fee() {
        return shipping_fee;
    }

    public void setShipping_fee(String shipping_fee) {
        this.shipping_fee = shipping_fee;
    }

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }
}
