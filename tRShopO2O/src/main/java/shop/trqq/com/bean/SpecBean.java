package shop.trqq.com.bean;

/**
 * 规格实体类
 *
 * @author 星河
 * @version 1.0
 * @created 2015-02-11
 */
public class SpecBean extends Entity {
    /**
     *
     */
    private String SpecID;
    private String SpecName;

    public String getSpecID() {
        return SpecID;
    }

    public void setSpecID(String specID) {
        SpecID = specID;
    }

    public String getSpecName() {
        return SpecName;
    }

    public void setSpecName(String specName) {
        SpecName = specName;
    }

}
