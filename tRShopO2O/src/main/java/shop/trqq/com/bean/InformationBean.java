package shop.trqq.com.bean;

/**
 * ��Ѷʵ����
 *
 * @author �Ǻ�
 * @version 1.0
 * @created 2015-02-11
 */
public class InformationBean extends Entity {

    /**
     *
     */

    // ��Ѷ����ID
    private String informationTypeId;
    // ��Ѷ����
    private String informationName;
    // ��Ѷid
    private String informationId;
    // ��Ѷ���ĵ�ַ
    private String url;
    // ���±���
    private String title;
    // ����ͼƬ
    private String titleImg;
    // ����ժҪ
    private String abstracts;
    // ��������
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
