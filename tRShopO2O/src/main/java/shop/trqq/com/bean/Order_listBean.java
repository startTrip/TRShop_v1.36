package shop.trqq.com.bean;

import java.util.List;

/**
 * 订单组实体类
 *
 * @author 星河
 * @version 1.0
 * @created 2015-02-11
 */
public class Order_listBean extends Entity {

    /**
     *
     */

    private String order_id;

    private String order_sn;

    private String pay_sn;

    private String store_id;

    private String store_name;

    private String buyer_id;

    private String buyer_name;

    private String buyer_email;

    private String add_time;

    private String payment_code;

    private String payment_time;

    private String finnshed_time;

    private String goods_amount;

    private String order_amount;

    private String rcb_amount;

    private String pd_amount;

    private String shipping_fee;

    private String evaluation_state;

    private String order_state;

    private String refund_state;

    private String lock_state;

    private String delete_state;

    private String refund_amount;

    private String delay_time;

    private String order_from;

    private String shipping_code;

    private String state_desc;

    private String payment_name;

    private List<Order_goodsBean> extend_order_goods;

    private boolean if_cancel;

    private boolean if_receive;

    private boolean if_lock;

    private boolean if_deliver;

    public String getIs_free_charge() {
        return is_free_charge;
    }

    public void setIs_free_charge(String is_free_charge) {
        this.is_free_charge = is_free_charge;
    }

    private String is_free_charge;

    public boolean isIf_free() {
        return if_free;
    }

    public void setIf_free(boolean if_free) {
        this.if_free = if_free;
    }

    private boolean if_free;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getPay_sn() {
        return pay_sn;
    }

    public void setPay_sn(String pay_sn) {
        this.pay_sn = pay_sn;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(String buyer_id) {
        this.buyer_id = buyer_id;
    }

    public String getBuyer_name() {
        return buyer_name;
    }

    public void setBuyer_name(String buyer_name) {
        this.buyer_name = buyer_name;
    }

    public String getBuyer_email() {
        return buyer_email;
    }

    public void setBuyer_email(String buyer_email) {
        this.buyer_email = buyer_email;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getPayment_code() {
        return payment_code;
    }

    public void setPayment_code(String payment_code) {
        this.payment_code = payment_code;
    }

    public String getPayment_time() {
        return payment_time;
    }

    public void setPayment_time(String payment_time) {
        this.payment_time = payment_time;
    }

    public String getFinnshed_time() {
        return finnshed_time;
    }

    public void setFinnshed_time(String finnshed_time) {
        this.finnshed_time = finnshed_time;
    }

    public String getGoods_amount() {
        return goods_amount;
    }

    public void setGoods_amount(String goods_amount) {
        this.goods_amount = goods_amount;
    }

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }

    public String getRcb_amount() {
        return rcb_amount;
    }

    public void setRcb_amount(String rcb_amount) {
        this.rcb_amount = rcb_amount;
    }

    public String getPd_amount() {
        return pd_amount;
    }

    public void setPd_amount(String pd_amount) {
        this.pd_amount = pd_amount;
    }

    public String getShipping_fee() {
        return shipping_fee;
    }

    public void setShipping_fee(String shipping_fee) {
        this.shipping_fee = shipping_fee;
    }

    public String getEvaluation_state() {
        return evaluation_state;
    }

    public void setEvaluation_state(String evaluation_state) {
        this.evaluation_state = evaluation_state;
    }

    public String getOrder_state() {
        return order_state;
    }

    public void setOrder_state(String order_state) {
        this.order_state = order_state;
    }

    public String getRefund_state() {
        return refund_state;
    }

    public void setRefund_state(String refund_state) {
        this.refund_state = refund_state;
    }

    public String getLock_state() {
        return lock_state;
    }

    public void setLock_state(String lock_state) {
        this.lock_state = lock_state;
    }

    public String getDelete_state() {
        return delete_state;
    }

    public void setDelete_state(String delete_state) {
        this.delete_state = delete_state;
    }

    public String getRefund_amount() {
        return refund_amount;
    }

    public void setRefund_amount(String refund_amount) {
        this.refund_amount = refund_amount;
    }

    public String getDelay_time() {
        return delay_time;
    }

    public void setDelay_time(String delay_time) {
        this.delay_time = delay_time;
    }

    public String getOrder_from() {
        return order_from;
    }

    public void setOrder_from(String order_from) {
        this.order_from = order_from;
    }

    public String getShipping_code() {
        return shipping_code;
    }

    public void setShipping_code(String shipping_code) {
        this.shipping_code = shipping_code;
    }

    public String getState_desc() {
        return state_desc;
    }

    public void setState_desc(String state_desc) {
        this.state_desc = state_desc;
    }

    public String getPayment_name() {
        return payment_name;
    }

    public void setPayment_name(String payment_name) {
        this.payment_name = payment_name;
    }

    public List<Order_goodsBean> getExtend_order_goods() {
        return extend_order_goods;
    }

    public void setExtend_order_goodss(List<Order_goodsBean> extend_order_goods) {
        this.extend_order_goods = extend_order_goods;
    }

    public boolean isIf_cancel() {
        return if_cancel;
    }

    public void setIf_cancel(boolean if_cancel) {
        this.if_cancel = if_cancel;
    }

    public boolean isIf_receive() {
        return if_receive;
    }

    public void setIf_receive(boolean if_receive) {
        this.if_receive = if_receive;
    }

    public boolean isIf_lock() {
        return if_lock;
    }

    public void setIf_lock(boolean if_lock) {
        this.if_lock = if_lock;
    }

    public boolean isIf_deliver() {
        return if_deliver;
    }

    public void setIf_deliver(boolean if_deliver) {
        this.if_deliver = if_deliver;
    }

}
