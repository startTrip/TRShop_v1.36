package shop.trqq.com.supermarket.bean;

import java.util.List;

/**
 * Project: TRShop_v1.36
 * Author: wm
 * Data:   2016/9/18
 */
public class HomeTopImage {

    /**
     * data : [{"id":42,"name":"热门影单","img_url":"http://7xqnv7.com1.z0.glb.clouddn.com/%E7%83%AD%E9%97%A8%E5%BD%B1%E5%8D%95%E5%89%AF%E6%9C%AC.jpg","type":2},{"id":479,"name":"电影卡片投稿","img_url":"http://7xqnv7.com1.z0.glb.clouddn.com/%E7%94%B5%E5%BD%B1%E5%8D%A1%E7%89%87%E6%94%B9.jpg","type":1}]
     * status : 1
     */

    private int status;
    /**
     * id : 42
     * name : 热门影单
     * img_url : http://7xqnv7.com1.z0.glb.clouddn.com/%E7%83%AD%E9%97%A8%E5%BD%B1%E5%8D%95%E5%89%AF%E6%9C%AC.jpg
     * type : 2
     */

    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private int id;
        private String name;
        private String img_url;
        private int type;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
