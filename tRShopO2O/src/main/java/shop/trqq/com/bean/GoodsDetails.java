package shop.trqq.com.bean;

import org.myjson.JSONException;
import org.myjson.JSONObject;

//不是gson模型，myjson解析
public class GoodsDetails {
    private String appoint_satedate;
    private String areaid_1;
    private String areaid_2;
    private String color_id;
    private String evaluation_count;
    private String evaluation_good_star;
    private String goods_attr;
    private String goods_click;
    private String goods_collect;
    private String goods_costprice;
    private String goods_discount;
    private String goods_freight;
    private String goods_id;
    private String goods_jingle;
    private String goods_marketprice;
    private String goods_name;
    private String goods_price;
    private String goods_salenum;
    private String goods_serial;
    private String goods_spec;
    private String goods_specname;
    private String goods_stcids;
    private String goods_storage;
    private String goods_url;
    private String goods_vat;
    private String have_gift;
    private String is_appoint;
    private String is_fcode;
    private String is_presell;
    private String is_virtual;
    private String mobile_body;
    private String plateid_bottom;
    private String plateid_top;
    private String presell_deliverdate;
    private String promotion_price;
    private String promotion_type;
    private String spec_name;
    private String spec_value;
    private String transport_id;
    private String transport_title;
    private String upper_limit;
    private String virtual_indate;
    private String virtual_invalid_refundtiny;
    private String virtual_limit;

    public GoodsDetails() {
    }

    public GoodsDetails(String paramString1, String paramString2,
                        String paramString3, String paramString4, String paramString5,
                        String paramString6, String paramString7, String paramString8,
                        String paramString9, String paramString10, String paramString11,
                        String paramString12, String paramString13, String paramString14,
                        String paramString15, String paramString16, String paramString17,
                        String paramString18, String paramString19, String paramString20,
                        String paramString21, String paramString22, String paramString23,
                        String paramString24, String paramString25, String paramString26,
                        String paramString27, String paramString28, String paramString29,
                        String paramString30, String paramString31, String paramString32,
                        String paramString33, String paramString34, String paramString35,
                        String paramString36, String paramString37, String paramString38,
                        String paramString39, String paramString40, String paramString41,
                        String paramString42, String paramString43, String paramString44) {
        this.virtual_invalid_refundtiny = paramString1;
        this.appoint_satedate = paramString2;
        this.presell_deliverdate = paramString3;
        this.goods_name = paramString4;
        this.goods_jingle = paramString5;
        this.spec_name = paramString6;
        this.spec_value = paramString7;
        this.goods_attr = paramString8;
        this.goods_specname = paramString9;
        this.goods_price = paramString10;
        this.goods_marketprice = paramString11;
        this.goods_costprice = paramString12;
        this.goods_discount = paramString13;
        this.goods_serial = paramString14;
        this.transport_id = paramString15;
        this.transport_title = paramString16;
        this.goods_freight = paramString17;
        this.goods_vat = paramString18;
        this.areaid_1 = paramString19;
        this.areaid_2 = paramString20;
        this.goods_stcids = paramString21;
        this.plateid_top = paramString22;
        this.plateid_bottom = paramString23;
        this.goods_id = paramString24;
        this.goods_click = paramString25;
        this.goods_collect = paramString26;
        this.goods_salenum = paramString27;
        this.goods_spec = paramString28;
        this.goods_storage = paramString29;
        this.color_id = paramString30;
        this.evaluation_good_star = paramString31;
        this.evaluation_count = paramString32;
        this.promotion_type = paramString33;
        this.promotion_price = paramString34;
        this.upper_limit = paramString35;
        this.goods_url = paramString36;
        this.mobile_body = paramString37;
        this.is_virtual = paramString38;
        this.virtual_indate = paramString39;
        this.virtual_limit = paramString40;
        this.is_fcode = paramString41;
        this.is_appoint = paramString42;
        this.is_presell = paramString43;
        this.have_gift = paramString44;
    }

    public static GoodsDetails newInstanceList(String paramString) {
        try {
            JSONObject localJSONObject = new JSONObject(paramString);
            if (localJSONObject.length() > 0) {
                String str1 = localJSONObject.optString("goods_name");
                String str2 = localJSONObject.optString("goods_jingle");
                String str3 = localJSONObject.optString("spec_name");
                String str4 = localJSONObject.optString("spec_value");
                String str5 = localJSONObject.optString("goods_attr");
                String str6 = localJSONObject.optString("goods_specname");
                String str7 = localJSONObject.optString("goods_price");
                String str8 = localJSONObject.optString("goods_marketprice");
                String str9 = localJSONObject.optString("goods_costprice");
                String str10 = localJSONObject.optString("goods_discount");
                String str11 = localJSONObject.optString("goods_serial");
                String str12 = localJSONObject.optString("transport_id");
                String str13 = localJSONObject.optString("transport_title");
                String str14 = localJSONObject.optString("goods_freight");
                String str15 = localJSONObject.optString("goods_vat");
                String str16 = localJSONObject.optString("areaid_1");
                String str17 = localJSONObject.optString("areaid_2");
                String str18 = localJSONObject.optString("goods_stcids");
                String str19 = localJSONObject.optString("plateid_top");
                String str20 = localJSONObject.optString("plateid_bottom");
                String str21 = localJSONObject.optString("goods_id");
                String str22 = localJSONObject.optString("goods_click");
                String str23 = localJSONObject.optString("goods_collect");
                String str24 = localJSONObject.optString("goods_salenum");
                String str25 = localJSONObject.optString("goods_spec");
                String str26 = localJSONObject.optString("goods_storage");
                String str27 = localJSONObject.optString("color_id");
                String str28 = localJSONObject
                        .optString("evaluation_good_star");
                String str29 = localJSONObject.optString("evaluation_count");
                String str30 = localJSONObject.optString("promotion_type");
                String str31 = localJSONObject
                        .optString("goods_promotion_price");
                String str32 = localJSONObject.optString("upper_limit");
                String str33 = localJSONObject.optString("goods_url");
                String str34 = localJSONObject.optString("mobile_body");
                String str35 = localJSONObject.optString("is_virtual");
                String str36 = localJSONObject.optString("virtual_indate");
                String str37 = localJSONObject.optString("virtual_limit");
                String str38 = localJSONObject.optString("is_fcode");
                String str39 = localJSONObject.optString("is_appoint");
                String str40 = localJSONObject.optString("is_presell");
                String str41 = localJSONObject.optString("have_gift");
                GoodsDetails localGoodsDetails = new GoodsDetails(
                        localJSONObject.optString("virtual_invalid_refund"),
                        localJSONObject.optString("appoint_satedate"),
                        localJSONObject.optString("presell_deliverdate"), str1,
                        str2, str3, str4, str5, str6, str7, str8, str9, str10,
                        str11, str12, str13, str14, str15, str16, str17, str18,
                        str19, str20, str21, str22, str23, str24, str25, str26,
                        str27, str28, str29, str30, str31, str32, str33, str34,
                        str35, str36, str37, str38, str39, str40, str41);
                return localGoodsDetails;
            }
        } catch (JSONException localJSONException) {
            localJSONException.printStackTrace();
        }
        return null;
    }

    public String getAppoint_satedate() {
        return this.appoint_satedate;
    }

    public String getAreaid_1() {
        return this.areaid_1;
    }

    public String getAreaid_2() {
        return this.areaid_2;
    }

    public String getColor_id() {
        return this.color_id;
    }

    public String getEvaluation_count() {
        return this.evaluation_count;
    }

    public String getEvaluation_good_star() {
        return this.evaluation_good_star;
    }

    public String getGoods_attr() {
        return this.goods_attr;
    }

    public String getGoods_click() {
        return this.goods_click;
    }

    public String getGoods_collect() {
        return this.goods_collect;
    }

    public String getGoods_costprice() {
        return this.goods_costprice;
    }

    public String getGoods_discount() {
        return this.goods_discount;
    }

    public String getGoods_freight() {
        return this.goods_freight;
    }

    public String getGoods_id() {
        return this.goods_id;
    }

    public String getGoods_jingle() {
        return this.goods_jingle;
    }

    public String getGoods_marketprice() {
        return this.goods_marketprice;
    }

    public String getGoods_name() {
        return this.goods_name;
    }

    public String getGoods_price() {
        return this.goods_price;
    }

    public String getGoods_salenum() {
        return this.goods_salenum;
    }

    public String getGoods_serial() {
        return this.goods_serial;
    }

    public String getGoods_spec() {
        return this.goods_spec;
    }

    public String getGoods_specname() {
        return this.goods_specname;
    }

    public String getGoods_stcids() {
        return this.goods_stcids;
    }

    public String getGoods_storage() {
        return this.goods_storage;
    }

    public String getGoods_url() {
        return this.goods_url;
    }

    public String getGoods_vat() {
        return this.goods_vat;
    }

    public String getHave_gift() {
        return this.have_gift;
    }

    public String getIs_appoint() {
        return this.is_appoint;
    }

    public String getIs_fcode() {
        return this.is_fcode;
    }

    public String getIs_presell() {
        return this.is_presell;
    }

    public String getIs_virtual() {
        return this.is_virtual;
    }

    public String getMobile_body() {
        return this.mobile_body;
    }

    public String getPlateid_bottom() {
        return this.plateid_bottom;
    }

    public String getPlateid_top() {
        return this.plateid_top;
    }

    public String getPresell_deliverdate() {
        return this.presell_deliverdate;
    }

    public String getPromotion_price() {
        return this.promotion_price;
    }

    public String getPromotion_type() {
        return this.promotion_type;
    }

    public String getSpec_name() {
        return this.spec_name;
    }

    public String getSpec_value() {
        return this.spec_value;
    }

    public String getTransport_id() {
        return this.transport_id;
    }

    public String getTransport_title() {
        return this.transport_title;
    }

    public String getUpper_limit() {
        return this.upper_limit;
    }

    public String getVirtual_indate() {
        return this.virtual_indate;
    }

    public String getVirtual_invalid_refundtiny() {
        return this.virtual_invalid_refundtiny;
    }

    public String getVirtual_limit() {
        return this.virtual_limit;
    }

    public void setAppoint_satedate(String paramString) {
        this.appoint_satedate = paramString;
    }

    public void setAreaid_1(String paramString) {
        this.areaid_1 = paramString;
    }

    public void setAreaid_2(String paramString) {
        this.areaid_2 = paramString;
    }

    public void setColor_id(String paramString) {
        this.color_id = paramString;
    }

    public void setEvaluation_count(String paramString) {
        this.evaluation_count = paramString;
    }

    public void setEvaluation_good_star(String paramString) {
        this.evaluation_good_star = paramString;
    }

    public void setGoods_attr(String paramString) {
        this.goods_attr = paramString;
    }

    public void setGoods_click(String paramString) {
        this.goods_click = paramString;
    }

    public void setGoods_collect(String paramString) {
        this.goods_collect = paramString;
    }

    public void setGoods_costprice(String paramString) {
        this.goods_costprice = paramString;
    }

    public void setGoods_discount(String paramString) {
        this.goods_discount = paramString;
    }

    public void setGoods_freight(String paramString) {
        this.goods_freight = paramString;
    }

    public void setGoods_id(String paramString) {
        this.goods_id = paramString;
    }

    public void setGoods_jingle(String paramString) {
        this.goods_jingle = paramString;
    }

    public void setGoods_marketprice(String paramString) {
        this.goods_marketprice = paramString;
    }

    public void setGoods_name(String paramString) {
        this.goods_name = paramString;
    }

    public void setGoods_price(String paramString) {
        this.goods_price = paramString;
    }

    public void setGoods_salenum(String paramString) {
        this.goods_salenum = paramString;
    }

    public void setGoods_serial(String paramString) {
        this.goods_serial = paramString;
    }

    public void setGoods_spec(String paramString) {
        this.goods_spec = paramString;
    }

    public void setGoods_specname(String paramString) {
        this.goods_specname = paramString;
    }

    public void setGoods_stcids(String paramString) {
        this.goods_stcids = paramString;
    }

    public void setGoods_storage(String paramString) {
        this.goods_storage = paramString;
    }

    public void setGoods_url(String paramString) {
        this.goods_url = paramString;
    }

    public void setGoods_vat(String paramString) {
        this.goods_vat = paramString;
    }

    public void setHave_gift(String paramString) {
        this.have_gift = paramString;
    }

    public void setIs_appoint(String paramString) {
        this.is_appoint = paramString;
    }

    public void setIs_fcode(String paramString) {
        this.is_fcode = paramString;
    }

    public void setIs_presell(String paramString) {
        this.is_presell = paramString;
    }

    public void setIs_virtual(String paramString) {
        this.is_virtual = paramString;
    }

    public void setMobile_body(String paramString) {
        this.mobile_body = paramString;
    }

    public void setPlateid_bottom(String paramString) {
        this.plateid_bottom = paramString;
    }

    public void setPlateid_top(String paramString) {
        this.plateid_top = paramString;
    }

    public void setPresell_deliverdate(String paramString) {
        this.presell_deliverdate = paramString;
    }

    public void setPromotion_price(String paramString) {
        this.promotion_price = paramString;
    }

    public void setPromotion_type(String paramString) {
        this.promotion_type = paramString;
    }

    public void setSpec_name(String paramString) {
        this.spec_name = paramString;
    }

    public void setSpec_value(String paramString) {
        this.spec_value = paramString;
    }

    public void setTransport_id(String paramString) {
        this.transport_id = paramString;
    }

    public void setTransport_title(String paramString) {
        this.transport_title = paramString;
    }

    public void setUpper_limit(String paramString) {
        this.upper_limit = paramString;
    }

    public void setVirtual_indate(String paramString) {
        this.virtual_indate = paramString;
    }

    public void setVirtual_invalid_refundtiny(String paramString) {
        this.virtual_invalid_refundtiny = paramString;
    }

    public void setVirtual_limit(String paramString) {
        this.virtual_limit = paramString;
    }

    public String toString() {
        return "GoodsDetails [virtual_invalid_refundtiny="
                + this.virtual_invalid_refundtiny + ", appoint_satedate="
                + this.appoint_satedate + ", presell_deliverdate="
                + this.presell_deliverdate + ", goods_name=" + this.goods_name
                + ", goods_jingle=" + this.goods_jingle + ", spec_name="
                + this.spec_name + ", spec_value=" + this.spec_value
                + ", goods_attr=" + this.goods_attr + ", goods_specname="
                + this.goods_specname + ", goods_price=" + this.goods_price
                + ", goods_marketprice=" + this.goods_marketprice
                + ", goods_costprice=" + this.goods_costprice
                + ", goods_discount=" + this.goods_discount + ", goods_serial="
                + this.goods_serial + ", transport_id=" + this.transport_id
                + ", transport_title=" + this.transport_title
                + ", goods_freight=" + this.goods_freight + ", goods_vat="
                + this.goods_vat + ", areaid_1=" + this.areaid_1
                + ", areaid_2=" + this.areaid_2 + ", goods_stcids="
                + this.goods_stcids + ", plateid_top=" + this.plateid_top
                + ", plateid_bottom=" + this.plateid_bottom + ", goods_id="
                + this.goods_id + ", goods_click=" + this.goods_click
                + ", goods_collect=" + this.goods_collect + ", goods_salenum="
                + this.goods_salenum + ", goods_spec=" + this.goods_spec
                + ", goods_storage=" + this.goods_storage + ", color_id="
                + this.color_id + ", evaluation_good_star="
                + this.evaluation_good_star + ", evaluation_count="
                + this.evaluation_count + ", promotion_type="
                + this.promotion_type + ", promotion_price="
                + this.promotion_price + ", upper_limit=" + this.upper_limit
                + ", goods_url=" + this.goods_url + ", mobile_body="
                + this.mobile_body + ", is_virtual=" + this.is_virtual
                + ", virtual_indate=" + this.virtual_indate
                + ", virtual_limit=" + this.virtual_limit + ", is_fcode="
                + this.is_fcode + ", is_appoint=" + this.is_appoint
                + ", is_presell=" + this.is_presell + ", have_gift="
                + this.have_gift + "]";
    }

    public static class Attr {
        public static final String APPOINT_SATEDATE = "appoint_satedate";
        public static final String AREAID_1 = "areaid_1";
        public static final String AREAID_2 = "areaid_2";
        public static final String COLOR_ID = "color_id";
        public static final String EVALUATION_COUNT = "evaluation_count";
        public static final String EVALUATION_GOOD_STAR = "evaluation_good_star";
        public static final String GOODS_ATTR = "goods_attr";
        public static final String GOODS_CLICK = "goods_click";
        public static final String GOODS_COLLECT = "goods_collect";
        public static final String GOODS_COSTPRICE = "goods_costprice";
        public static final String GOODS_DISCOUNT = "goods_discount";
        public static final String GOODS_FREIGHT = "goods_freight";
        public static final String GOODS_ID = "goods_id";
        public static final String GOODS_JINGLE = "goods_jingle";
        public static final String GOODS_MARKETPRICE = "goods_marketprice";
        public static final String GOODS_NAME = "goods_name";
        public static final String GOODS_PRICE = "goods_price";
        public static final String GOODS_SALENUM = "goods_salenum";
        public static final String GOODS_SERIAL = "goods_serial";
        public static final String GOODS_SPEC = "goods_spec";
        public static final String GOODS_SPECNAME = "goods_specname";
        public static final String GOODS_STCIDS = "goods_stcids";
        public static final String GOODS_STORAGE = "goods_storage";
        public static final String GOODS_URL = "goods_url";
        public static final String GOODS_VAT = "goods_vat";
        public static final String HAVE_GIFT = "have_gift";
        public static final String IS_APPOINT = "is_appoint";
        public static final String IS_FCODE = "is_fcode";
        public static final String IS_PRESELL = "is_presell";
        public static final String IS_VIRTUAL = "is_virtual";
        public static final String MOBILE_BOBY = "mobile_body";
        public static final String PLATEID_BOTTOM = "plateid_bottom";
        public static final String PLATEID_TOP = "plateid_top";
        public static final String PRESELL_DELIVERDATE = "presell_deliverdate";
        public static final String PROMOTION_PRICE = "promotion_price";
        public static final String PROMOTION_TYPE = "promotion_type";
        public static final String SPEC_NAME = "spec_name";
        public static final String SPEC_VALUE = "spec_value";
        public static final String TRANSPORT_ID = "transport_id";
        public static final String TRANSPORT_TITLE = "transport_title";
        public static final String UPPER_LIMIT = "upper_limit";
        public static final String VIRTUAL_INDATE = "virtual_indate";
        public static final String VIRTUAL_INVALID_REFUNDTINY = "virtual_invalid_refund";
        public static final String VIRTUAL_LIMIT = "virtual_limit";
    }
}
