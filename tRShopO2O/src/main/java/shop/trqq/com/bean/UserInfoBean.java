package shop.trqq.com.bean;

/**
 * �û���Ϣʵ����
 *
 * @author
 * @version 1.0
 * @created 2015-02-16
 */
public class UserInfoBean extends Entity {

    /**
     *
     */
    // �û�id
    private String userId;
    // ����
    private String token;
    // KEY
    private String key;

    // ��½����
    private int loginType;
    // �ǳ�
    private String username;
    // ѧУ����
    private String schoolId;
    // ѧУ����
    private String schoolName;
    // רҵ
    private String professional;
    // �꼶
    private String schoolClass;
    // �û�ͷ��
    private String userIcon;
    // �Ա�
    private int sex;
    // �ֻ�����
    private String mobileBumber;
    // �Ƿ��½
    private boolean login;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }

    public String getNickname() {
        return username;
    }

    public void setNickname(String username) {
        this.username = username;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getProfessional() {
        return professional;
    }

    public void setProfessional(String professional) {
        this.professional = professional;
    }

    public String getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(String schoolClass) {
        this.schoolClass = schoolClass;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getMobileBumber() {
        return mobileBumber;
    }

    public void setMobileBumber(String mobileBumber) {
        this.mobileBumber = mobileBumber;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

}
