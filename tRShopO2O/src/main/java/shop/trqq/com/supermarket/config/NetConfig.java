package shop.trqq.com.supermarket.config;

/**
 * Project: TRShop_v1.36
 * Author: wm
 * Data:   2016/9/18
 */
public interface NetConfig {

    // ������ҳģ��������ַ

    // ������ҳ banner ҳ�� get ����
    String DISCOVERBANNER ="http://api.markapp.cn/v131/singles/banner";


    // ������ҳ���� post ���� baseUrl ������� agent_id = 101
    String BASEHOMECLASSIFY ="http://123.57.81.236/sixmarket/sixmarket/index.php/Webservice/v410/index_show";


    //-------------------------------------------------------
    // ����ģ���ַ   post ���� baseUrl ������� agent_id = 101
    String CLASSIFYDATA = "http://123.57.81.236/sixmarket/sixmarket/index.php/Webservice/v410/goods_type_list";


    //-------------------------------------------------------
    // ��Ʒ�б�baseUrl   ����ұߵķ���ģ��(�������Id)
    String GOODSLISTFROMCLASSIFY = "http://123.57.81.236/sixmarket/sixmarket/index.php/Webservice/v410/get_index_goodslist";

    // ��ҳ�з���ͼ��   �������
    String GOODSLISTFROMHOME= "http://123.57.81.236/sixmarket/sixmarket/index.php/Webservice/v410/index_type_goods_list";
}

