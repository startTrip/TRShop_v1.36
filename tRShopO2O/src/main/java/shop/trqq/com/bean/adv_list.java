package shop.trqq.com.bean;

import java.util.List;

public class adv_list extends Entity {
    /**
     *
     */
    private List<Home1Bean> item;
    private String title;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setItem(List<Home1Bean> item) {
        this.item = item;
    }

    public List<Home1Bean> getItem() {
        return this.item;
    }

}
