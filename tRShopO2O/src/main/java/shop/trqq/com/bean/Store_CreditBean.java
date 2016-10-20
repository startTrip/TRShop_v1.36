package shop.trqq.com.bean;

import java.io.Serializable;

public class Store_CreditBean implements Serializable {
    /**
     *
     */
    private String text;
    private String credit;
    private String percent;
    private String percent_class;
    private String percent_text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getPercent_class() {
        return percent_class;
    }

    public void setPercent_class(String percent_class) {
        this.percent_class = percent_class;
    }

    public String getPercent_text() {
        return percent_text;
    }

    public void setPercent_text(String percent_text) {
        this.percent_text = percent_text;
    }

}
