package shop.trqq.com.bean;

public class StoreListBean extends Entity {

    /**
     *
     */
    private String store_name;
    private String store_id;
    private String store_address;
    private String store_area_info;

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getStore_address() {
        return store_address;
    }

    public void setStore_address(String store_address) {
        this.store_address = store_address;
    }

    public String getStore_area_info() {
        return store_area_info;
    }

    public void setStore_area_info(String store_area_info) {
        this.store_area_info = store_area_info;
    }

}
