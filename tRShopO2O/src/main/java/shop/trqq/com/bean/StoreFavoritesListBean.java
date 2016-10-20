package shop.trqq.com.bean;

public class StoreFavoritesListBean extends Entity {

    /**
     *
     */

    private String store_id;

    private String store_name;

    private String fav_time;

    private String fav_time_text;

    private String goods_count;

    private String store_collect;

    private String store_avatar_url;

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

    public String getFav_time() {
        return fav_time;
    }

    public void setFav_time(String fav_time) {
        this.fav_time = fav_time;
    }

    public String getFav_time_text() {
        return fav_time_text;
    }

    public void setFav_time_text(String fav_time_text) {
        this.fav_time_text = fav_time_text;
    }

    public String getGoods_count() {
        return goods_count;
    }

    public void setGoods_count(String goods_count) {
        this.goods_count = goods_count;
    }

    public String getStore_collect() {
        return store_collect;
    }

    public void setStore_collect(String store_collect) {
        this.store_collect = store_collect;
    }

    public String getStore_avatar_url() {
        return store_avatar_url;
    }

    public void setStore_avatar_url(String store_avatar_url) {
        this.store_avatar_url = store_avatar_url;
    }

}
