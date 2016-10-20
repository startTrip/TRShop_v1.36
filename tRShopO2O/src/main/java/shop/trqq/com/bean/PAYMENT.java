package shop.trqq.com.bean;

public class PAYMENT extends Entity {
    /**
     *
     */

    private String is_cod;

    private String pay_code;

    private String pay_fee;

    private String pay_id;

    private String formated_pay_fee;

    private String pay_name;

    public String getIs_cod() {
        return is_cod;
    }

    public void setIs_cod(String is_cod) {
        this.is_cod = is_cod;
    }

    public String getPay_code() {
        return pay_code;
    }

    public void setPay_code(String pay_code) {
        this.pay_code = pay_code;
    }

    public String getPay_fee() {
        return pay_fee;
    }

    public void setPay_fee(String pay_fee) {
        this.pay_fee = pay_fee;
    }

    public String getPay_id() {
        return pay_id;
    }

    public void setPay_id(String pay_id) {
        this.pay_id = pay_id;
    }

    public String getFormated_pay_fee() {
        return formated_pay_fee;
    }

    public void setFormated_pay_fee(String formated_pay_fee) {
        this.formated_pay_fee = formated_pay_fee;
    }

    public String getPay_name() {
        return pay_name;
    }

    public void setPay_name(String pay_name) {
        this.pay_name = pay_name;
    }

}
