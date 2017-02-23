package shop.trqq.com.supermarket.bean;

import java.util.List;

/**
 * Project: Market
 * Author: wm
 * Data:   2016/9/30
 */
public class GoCartGoods {


    /**
     * code : 200
     * datas : {"cart_list":[{"cart_id":"10843","buyer_id":"14507","store_id":"126","store_name":"万能居生鲜超市","goods_id":"28807","goods_name":"公仔四包面牛肉汤面 388g 4包实惠装 速食面快餐面即食面油炸方便面","goods_price":"8.30","goods_num":"1","goods_image":"126_05270745977349933.jpg","bl_id":"0","goods_image_url":"http://shop.trqq.com/data/upload/shop/store/goods/126/126_05270745977349933_240.jpg","goods_sum":"8.30"},{"cart_id":"10844","buyer_id":"14507","store_id":"1","store_name":"泰润自营","goods_id":"17911","goods_name":"泰润集团二周年纪念T恤  包邮！ 红色 M","goods_price":"28.00","goods_num":"1","goods_image":"1_05230344878836062.jpg","bl_id":"0","goods_image_url":"http://shop.trqq.com/data/upload/shop/store/goods/1/1_05230344878836062_240.jpg","goods_sum":"28.00"},{"cart_id":"11442","buyer_id":"14507","store_id":"126","store_name":"万能居生鲜超市","goods_id":"34750","goods_name":"巧渍柠檬洗洁精3L 洗涤灵无磷 不伤手 强效去油无残留","goods_price":"29.00","goods_num":"1","goods_image":"126_05283072966773795.jpg","bl_id":"0","goods_image_url":"http://shop.trqq.com/data/upload/shop/store/goods/126/126_05283072966773795_240.jpg","goods_sum":"29.00"},{"cart_id":"11496","buyer_id":"14507","store_id":"114","store_name":"手表时钟","goods_id":"15276","goods_name":"正品品牌 瑞士手表 男士手表钢带日历夜光男表爆款 8802 钢带蓝面","goods_price":"148.00","goods_num":"1","goods_image":"114_05218297103431322.jpg","bl_id":"0","goods_image_url":"http://shop.trqq.com/data/upload/shop/store/goods/114/114_05218297103431322_240.jpg","goods_sum":"148.00"}],"sum":"213.30"}
     */

    private int code;
    /**
     * cart_list : [{"cart_id":"10843","buyer_id":"14507","store_id":"126","store_namee":"万能居生鲜超市","goods_id":"28807","goods_name":"公仔四包面牛肉汤面 388g 4包实惠装 速食面快餐面即食面油炸方便面","goods_price":"8.30","goods_num":"1","goods_image":"126_05270745977349933.jpg","bl_id":"0","goods_image_url":"http://shop.trqq.com/data/upload/shop/store/goods/126/126_05270745977349933_240.jpg","goods_sum":"8.30"},{"cart_id":"10844","buyer_id":"14507","store_id":"1","store_name":"泰润自营","goods_id":"17911","goods_name":"泰润集团二周年纪念T恤  包邮！ 红色 M","goods_price":"28.00","goods_num":"1","goods_image":"1_05230344878836062.jpg","bl_id":"0","goods_image_url":"http://shop.trqq.com/data/upload/shop/store/goods/1/1_05230344878836062_240.jpg","goods_sum":"28.00"},{"cart_id":"11442","buyer_id":"14507","store_id":"126","store_name":"万能居生鲜超市","goods_id":"34750","goods_name":"巧渍柠檬洗洁精3L 洗涤灵无磷 不伤手 强效去油无残留","goods_price":"29.00","goods_num":"1","goods_image":"126_05283072966773795.jpg","bl_id":"0","goods_image_url":"http://shop.trqq.com/data/upload/shop/store/goods/126/126_05283072966773795_240.jpg","goods_sum":"29.00"},{"cart_id":"11496","buyer_id":"14507","store_id":"114","store_name":"手表时钟","goods_id":"15276","goods_name":"正品品牌 瑞士手表 男士手表钢带日历夜光男表爆款 8802 钢带蓝面","goods_price":"148.00","goods_num":"1","goods_image":"114_05218297103431322.jpg","bl_id":"0","goods_image_url":"http://shop.trqq.com/data/upload/shop/store/goods/114/114_05218297103431322_240.jpg","goods_sum":"148.00"}]
     * sum : 213.30
     */

    private DatasBean datas;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DatasBean getDatas() {
        return datas;
    }

    public void setDatas(DatasBean datas) {
        this.datas = datas;
    }

    public static class DatasBean {
        private String sum;
        /**
         * cart_id : 10843
         * buyer_id : 14507
         * store_id : 126
         * store_name : 万能居生鲜超市
         * goods_id : 28807
         * goods_name : 公仔四包面牛肉汤面 388g 4包实惠装 速食面快餐面即食面油炸方便面
         * goods_price : 8.30
         * goods_num : 1
         * goods_image : 126_05270745977349933.jpg
         * bl_id : 0
         * goods_image_url : http://shop.trqq.com/data/upload/shop/store/goods/126/126_05270745977349933_240.jpg
         * goods_sum : 8.30
         */

        private List<CartListBean> cart_list;

        public String getSum() {
            return sum;
        }

        public void setSum(String sum) {
            this.sum = sum;
        }

        public List<CartListBean> getCart_list() {
            return cart_list;
        }

        public void setCart_list(List<CartListBean> cart_list) {
            this.cart_list = cart_list;
        }

        public static class CartListBean {
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
            private String goods_image_url;
            private String goods_sum;
            private boolean mIsCheck;

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

            public String getGoods_image_url() {
                return goods_image_url;
            }

            public void setGoods_image_url(String goods_image_url) {
                this.goods_image_url = goods_image_url;
            }

            public String getGoods_sum() {
                return goods_sum;
            }

            public void setGoods_sum(String goods_sum) {
                this.goods_sum = goods_sum;
            }

            public void setIsCheck(boolean isCheck) {
                mIsCheck = isCheck;
            }

            public boolean getIsCheck() {
                return mIsCheck;
            }
        }
    }
}

