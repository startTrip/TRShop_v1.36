package shop.trqq.com.bean;

import java.util.List;


/**
 * 订单组实体类
 *
 * @author 星河
 * @version 1.0
 * @created 2015-02-11
 */
public class OrderGroupHomeListBean extends Entity {

    /**
     *
     */

    private List<Order_listBean> order_list;

    private String pay_amount;

    private String add_time;

    private String pay_sn;

    public void setOrder_list(List<Order_listBean> order_list) {
        this.order_list = order_list;
    }

    public List<Order_listBean> getOrder_list() {
        return this.order_list;
    }

    public void setPay_amount(String pay_amount) {
        this.pay_amount = pay_amount;
    }

    public String getPay_amount() {
        return this.pay_amount;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getAdd_time() {
        return this.add_time;
    }

    public void setPay_sn(String pay_sn) {
        this.pay_sn = pay_sn;
    }

    public String getPay_sn() {
        return this.pay_sn;
    }

}
