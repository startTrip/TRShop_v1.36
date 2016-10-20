package com.alipay.pay;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Project: TRShop_v1.36
 * Author: wm
 * Data:   2016/10/12
 */
public class AlipayHelper {

    // �̻�PID  ������ 2088 ��ͷ
    public static final String PARTNER = "2088111278561763";
    // �տ��ˣ�֧�����˺ţ��˺��ǹ̶��ģ�һ���̻�һ���˺�
    public static final String SELLER = "gaoyandingzhi@126.com";
    // �ͻ���ʹ�õ�˽Կ�����������������ǩ�����������������ʹ�ù�Կ������֤
    public static final String RSA_PRIVATE = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBANEClM9ja39OuhbiFcPYG8nUt19TIGvnBjC2CGMV3BKY2pTolVuicMfM0yyxvwtewe7Wkk+06Zl8fjgIWZS8SsfOeznQZbJq236CbcFYIhDsorDllDwQ0Uk409WSjaOCDJamOjGeQjYqy3D7v+z+Z48ZvCOPleX2h415mHQeHWVdAgMBAAECgYB6FrHqOr7uTIRzHXltPu1shi7fJeWIYhjBl3NqvbghvNvho8KrFkYez8yDDQj1kVJjOz+YA6t4lrn77RS2xw4+fRJgBy/LD9ILectaThysuFt84yKooSuFAv1AQKMeVXkpnFuzzBFtxyuRPtPUYXftSvEm/9BapFHGEVCuT7RvAQJBAP9yq18VFhPQAfngld9n0NwmCO33kdbFYqVIWBNKZdvVZIqwIvnmTqsgQacrvWutsWauukKT7VzySkht/uE63j0CQQDRdjgqx4H7SfMjkaZK5nJ6ptuFgR19HkakOJZSIM78Ot3PzfHcnfYuCRjs8lIEWmhYqj2FE+BcZ9cejphGuTWhAkB0XimBXBq9ldGAonXD2whDcbQ5q8EtJKgmgUlWKFs0hQaTQ1/7lZYa0Mv3uq5EwlCBZXGGaNsFr351dl5Y/jdFAkA6D2DmSsL22rqwo1DK9jHJWbMDwJRh+CBwqNbSERIOzGprjZR7KLXycMcd9tVRK5Y87YN7/dR1CLuSVshS4kfBAkAW6ls9/RlBA6gOpDuq+Qn4CZUng3h7OJsDgzCY95RtuMISJNuVFcGC/XVKB+urkyfhR/H7I8HIPXQtNJenH9f2";

    // ֧������Կ
    public static final String RSA_PUBLIC = "";

    /**
     * create the order info. ����������Ϣ
     *
     */
    public static String getOrderInfo(String subject, String body, String price,String pay_sn) {

        // ǩԼ���������ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // ǩԼ����֧�����˺�
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // �̻���վΨһ������
        // TODO ��������ţ��������Լ��������ṩ�ģ�
        orderInfo += "&out_trade_no=" + "\"" + pay_sn + "\"";

        Log.d("getOrder", "getOrderInfo: "+ pay_sn);
        // ��Ʒ���
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // ��Ʒ����
        orderInfo += "&body=" + "\"" + body + "\"";

        // ��Ʒ���
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // �������첽֪ͨҳ��·��
        // ��֧����֧���ɹ���֧�������������Զ����������ַ�������ǵ��̳�֧���Ƿ�ɹ�
        orderInfo += "&notify_url=" + "\"" + "http://shop.wushi3.com/mobile/index.php?act=wnj_payment&op=notify_url" + "\"";

        // ����ӿ���ƣ� �̶�ֵ
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // ֧�����ͣ� �̶�ֵ
        orderInfo += "&payment_type=\"1\"";

        // ������룬 �̶�ֵ
        orderInfo += "&_input_charset=\"utf-8\"";

        // ����δ����׵ĳ�ʱʱ��
        // Ĭ��30���ӣ�һ����ʱ���ñʽ��׾ͻ��Զ����رա�
        // ȡֵ��Χ��1m��15d��
        // m-���ӣ�h-Сʱ��d-�죬1c-���죨���۽��׺�ʱ����������0��رգ���
        // �ò�����ֵ������С��㣬��1.5h����ת��Ϊ90m��
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_tokenΪ��������Ȩ��ȡ����alipay_open_id,���ϴ˲����û���ʹ����Ȩ���˻�����֧��
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // ֧��������������󣬵�ǰҳ����ת���̻�ָ��ҳ���·�����ɿ�
        orderInfo += "&return_url=\"m.alipay.com\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. ����̻������ţ���ֵ���̻���Ӧ����Ψһ�����Զ����ʽ�淶��
     *
     */
    public static String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info. �Զ�����Ϣ����ǩ��
     *
     * @param content
     *            ��ǩ����Ϣ
     */
    public static String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. ��ȡǩ��ʽ
     *
     */
    public static String getSignType() {
        return "sign_type=\"RSA\"";
    }

}
