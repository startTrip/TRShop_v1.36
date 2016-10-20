package shop.trqq.com.bean;

/**
 * 资讯实体类
 *
 * @author 星河
 * @version 1.0
 * @created 2015-02-11
 */
public class InformationBean extends Entity {

    /**
     *
     */

    // 资讯分类ID
    private String informationTypeId;
    // 资讯名称
    private String informationName;
    // 资讯id
    private String informationId;
    // 资讯正文地址
    private String url;
    // 文章标题
    private String title;
    // 标题图片
    private String titleImg;
    // 文章摘要
    private String abstracts;
    // 评论条数
    private int commentCount;
    private String imageuri1;
    private String imageuri2;

    public String getImageuri1() {
        return imageuri1;
    }

    public void setImageuri1(String imageuri1) {
        this.imageuri1 = imageuri1;
    }

    public String getImageuri2() {
        return imageuri2;
    }

    public void setImageuri2(String imageuri2) {
        this.imageuri2 = imageuri2;
    }

    private String[] imageuris;

    public String[] getImageuris() {
        return imageuris;
    }

    public void setImageuris(String[] imageuris) {
        this.imageuris = imageuris;
    }

    public String getInformationTypeId() {
        return informationTypeId;
    }

    public void setInformationTypeId(String informationTypeId) {
        this.informationTypeId = informationTypeId;
    }

    public String getInformationName() {
        return informationName;
    }

    public void setInformationName(String informationName) {
        this.informationName = informationName;
    }

    public String getInformationId() {
        return informationId;
    }

    public void setInformationId(String informationId) {
        this.informationId = informationId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleImg() {
        return titleImg;
    }

    public void setTitleImg(String titleImg) {
        this.titleImg = titleImg;
    }

    public String getAbstracts() {
        return abstracts;
    }

    public void setAbstracts(String abstracts) {
        this.abstracts = abstracts;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

}
