package shop.trqq.com.supermarket.config;

/**
 * Project: TRShop_v1.36
 * Author: wm
 * Data:   2016/9/18
 */
public interface NetConfig {

    // 超市首页模块的请求地址

    // 超市首页 banner 页面 get 请求
    String DISCOVERBANNER ="http://api.markapp.cn/v131/singles/banner";


    // 超市首页分类 post 请求 baseUrl 请求参数 agent_id = 101
    String BASEHOMECLASSIFY ="http://123.57.81.236/sixmarket/sixmarket/index.php/Webservice/v410/index_show";


    //-------------------------------------------------------
    // 分类模块地址   post 请求 baseUrl 请求参数 agent_id = 101
    String CLASSIFYDATA = "http://123.57.81.236/sixmarket/sixmarket/index.php/Webservice/v410/goods_type_list";


    //-------------------------------------------------------
    // 商品列表baseUrl   点击右边的分类模块(传点击的Id)
    String GOODSLISTFROMCLASSIFY = "http://123.57.81.236/sixmarket/sixmarket/index.php/Webservice/v410/get_index_goodslist";

    // 首页中分类图标   点击详情
    String GOODSLISTFROMHOME= "http://123.57.81.236/sixmarket/sixmarket/index.php/Webservice/v410/index_type_goods_list";
}

