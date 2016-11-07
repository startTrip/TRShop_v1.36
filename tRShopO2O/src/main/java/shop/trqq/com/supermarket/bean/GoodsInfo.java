package shop.trqq.com.supermarket.bean;

import java.util.List;

/**
 * Project: TRShop_v1.36
 * Author: wm
 * Data:   2016/10/16
 */
public class GoodsInfo {

    /**
     * goods_list : [{"cart_id":"11579","buyer_id":"14654","store_id":"126","store_name":"万能居生鲜超市","goods_id":"36142","goods_name":"新鲜海胆","goods_price":"45.00","goods_num":"1","goods_image":"126_05295545384398540.jpg","bl_id":"0","state":true,"storage_state":true,"goods_commonid":"106600","gc_id":"656","transport_id":"0","goods_freight":"0.00","goods_vat":"0","goods_storage":"7","goods_storage_alarm":"0","is_fcode":"0","have_gift":"0","groupbuy_info":null,"xianshi_info":null,"goods_weight":"20.00","goods_total":"45.00","goods_image_url":"http://shop.wushi3.com/data/upload/shop/store/goods/126/126_05295545384398540_240.jpg"},{"cart_id":"11580","buyer_id":"14654","store_id":"126","store_name":"万能居生鲜超市","goods_id":"36141","goods_name":"牛肉干","goods_price":"5.00","goods_num":"1","goods_image":"126_05289207602625132.jpg","bl_id":"0","state":true,"storage_state":true,"goods_commonid":"106599","gc_id":"288","transport_id":"0","goods_freight":"0.00","goods_vat":"0","goods_storage":"3319","goods_storage_alarm":"0","is_fcode":"0","have_gift":"0","groupbuy_info":null,"xianshi_info":null,"goods_weight":"2.00","goods_total":"5.00","goods_image_url":"http://shop.wushi3.com/data/upload/shop/store/goods/126/126_05289207602625132_240.jpg"}]
     * store_goods_total : 50.00
     * goods_weight_all : 22
     * store_shipping : 22
     * store_mansong_rule_list : null
     * store_voucher_list : []
     * freight : 1
     * store_name : 万能居生鲜超市
     */

    private String store_goods_total;
    private String goods_weight_all;
    private String store_shipping;
    private Object store_mansong_rule_list;
    private String freight;
    private String store_name;

    public String getArrive_time() {
        return arrive_time;
    }

    public void setArrive_time(String arrive_time) {
        this.arrive_time = arrive_time;
    }

    private String arrive_time;
    /**
     * cart_id : 11579
     * buyer_id : 14654
     * store_id : 126
     * store_name : 万能居生鲜超市
     * goods_id : 36142
     * goods_name : 新鲜海胆
     * goods_price : 45.00
     * goods_num : 1
     * goods_image : 126_05295545384398540.jpg
     * bl_id : 0
     * state : true
     * storage_state : true
     * goods_commonid : 106600
     * gc_id : 656
     * transport_id : 0
     * goods_freight : 0.00
     * goods_vat : 0
     * goods_storage : 7
     * goods_storage_alarm : 0
     * is_fcode : 0
     * have_gift : 0
     * groupbuy_info : null
     * xianshi_info : null
     * goods_weight : 20.00
     * goods_total : 45.00
     * goods_image_url : http://shop.wushi3.com/data/upload/shop/store/goods/126/126_05295545384398540_240.jpg
     */

    private List<GoodsListBean> goods_list;
    private List<?> store_voucher_list;

    public String getStore_goods_total() {
        return store_goods_total;
    }

    public void setStore_goods_total(String store_goods_total) {
        this.store_goods_total = store_goods_total;
    }

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

    public Object getStore_mansong_rule_list() {
        return store_mansong_rule_list;
    }

    public void setStore_mansong_rule_list(Object store_mansong_rule_list) {
        this.store_mansong_rule_list = store_mansong_rule_list;
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

    public List<GoodsListBean> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(List<GoodsListBean> goods_list) {
        this.goods_list = goods_list;
    }

    public List<?> getStore_voucher_list() {
        return store_voucher_list;
    }

    public void setStore_voucher_list(List<?> store_voucher_list) {
        this.store_voucher_list = store_voucher_list;
    }

    public static class GoodsListBean {
        private String cart_id;
        private String buyer_id;
        private String store_id;
        private String store_name;
        private String goods_id;
        private String goods_name;
        private String goods_price;
        private String goods_num;
        private String goods_image;
        private String bl_id;
        private boolean state;
        private boolean storage_state;
        private String goods_commonid;
        private String gc_id;
        private String transport_id;
        private String goods_freight;
        private String goods_vat;
        private String goods_storage;
        private String goods_storage_alarm;
        private String is_fcode;
        private String have_gift;
        private Object groupbuy_info;
        private Object xianshi_info;
        private String goods_weight;
        private String goods_total;
        private String goods_image_url;

        public String getCart_id() {
            return cart_id;
        }

        public void setCart_id(String cart_id) {
            this.cart_id = cart_id;
        }

        public String getBuyer_id() {
            return buyer_id;
        }

        public void setBuyer_id(String buyer_id) {
            this.buyer_id = buyer_id;
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

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getGoods_price() {
            return goods_price;
        }

        public void setGoods_price(String goods_price) {
            this.goods_price = goods_price;
        }

        public String getGoods_num() {
            return goods_num;
        }

        public void setGoods_num(String goods_num) {
            this.goods_num = goods_num;
        }

        public String getGoods_image() {
            return goods_image;
        }

        public void setGoods_image(String goods_image) {
            this.goods_image = goods_image;
        }

        public String getBl_id() {
            return bl_id;
        }

        public void setBl_id(String bl_id) {
            this.bl_id = bl_id;
        }

        public boolean isState() {
            return state;
        }

        public void setState(boolean state) {
            this.state = state;
        }

        public boolean isStorage_state() {
            return storage_state;
        }

        public void setStorage_state(boolean storage_state) {
            this.storage_state = storage_state;
        }

        public String getGoods_commonid() {
            return goods_commonid;
        }

        public void setGoods_commonid(String goods_commonid) {
            this.goods_commonid = goods_commonid;
        }

        public String getGc_id() {
            return gc_id;
        }

        public void setGc_id(String gc_id) {
            this.gc_id = gc_id;
        }

        public String getTransport_id() {
            return transport_id;
        }

        public void setTransport_id(String transport_id) {
            this.transport_id = transport_id;
        }

        public String getGoods_freight() {
            return goods_freight;
        }

        public void setGoods_freight(String goods_freight) {
            this.goods_freight = goods_freight;
        }

        public String getGoods_vat() {
            return goods_vat;
        }

        public void setGoods_vat(String goods_vat) {
            this.goods_vat = goods_vat;
        }

        public String getGoods_storage() {
            return goods_storage;
        }

        public void setGoods_storage(String goods_storage) {
            this.goods_storage = goods_storage;
        }

        public String getGoods_storage_alarm() {
            return goods_storage_alarm;
        }

        public void setGoods_storage_alarm(String goods_storage_alarm) {
            this.goods_storage_alarm = goods_storage_alarm;
        }

        public String getIs_fcode() {
            return is_fcode;
        }

        public void setIs_fcode(String is_fcode) {
            this.is_fcode = is_fcode;
        }

        public String getHave_gift() {
            return have_gift;
        }

        public void setHave_gift(String have_gift) {
            this.have_gift = have_gift;
        }

        public Object getGroupbuy_info() {
            return groupbuy_info;
        }

        public void setGroupbuy_info(Object groupbuy_info) {
            this.groupbuy_info = groupbuy_info;
        }

        public Object getXianshi_info() {
            return xianshi_info;
        }

        public void setXianshi_info(Object xianshi_info) {
            this.xianshi_info = xianshi_info;
        }

        public String getGoods_weight() {
            return goods_weight;
        }

        public void setGoods_weight(String goods_weight) {
            this.goods_weight = goods_weight;
        }

        public String getGoods_total() {
            return goods_total;
        }

        public void setGoods_total(String goods_total) {
            this.goods_total = goods_total;
        }

        public String getGoods_image_url() {
            return goods_image_url;
        }

        public void setGoods_image_url(String goods_image_url) {
            this.goods_image_url = goods_image_url;
        }
    }
}
