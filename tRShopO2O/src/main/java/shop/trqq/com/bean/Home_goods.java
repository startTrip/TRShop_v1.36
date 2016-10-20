package shop.trqq.com.bean;

import java.util.List;

public class Home_goods extends Entity {
    /**
     *
     */
    private List<GoodsBean> item;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setItem(List<GoodsBean> item) {
        this.item = item;
    }

    public List<GoodsBean> getItem() {
        return this.item;
    }

}
