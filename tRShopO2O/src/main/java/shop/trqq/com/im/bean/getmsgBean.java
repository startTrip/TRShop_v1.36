package shop.trqq.com.im.bean;

import org.litepal.crud.DataSupport;

public class getmsgBean extends DataSupport {
    /**
     * LitePal������һ�ּ�Ϊ�����Ľ��������ֻ��������private���η����ֶβŻᱻӳ�䵽���ݿ���У�
     * �������ĳһ���ֶβ���ӳ��Ļ���ֻ��Ҫ�����ĳ�public��protected��default���η��Ϳ����ˡ�
     */

    private int id;

    // f_=from���� t_=to���͵� m_=msg��Ϣ
    private int f_id;

    private String f_name;

    private int t_id;

    private String t_name;

    private String t_msg;

    private String f_ip;

    private String add_time;

    private int m_id;

    private int goods_id;

    // private List<goods_info> goods_info ;

	/*
     * private User user;
	 * 
	 * public User getUser() { return user; }
	 * 
	 * public void setUser(User user) { this.user = user; }
	 */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getF_id() {
        return f_id;
    }

    public void setF_id(int f_id) {
        this.f_id = f_id;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public int getT_id() {
        return t_id;
    }

    public void setT_id(int t_id) {
        this.t_id = t_id;
    }

    public String getT_name() {
        return t_name;
    }

    public void setT_name(String t_name) {
        this.t_name = t_name;
    }

    public String getT_msg() {
        return t_msg;
    }

    public void setT_msg(String t_msg) {
        this.t_msg = t_msg;
    }

    public String getF_ip() {
        return f_ip;
    }

    public void setF_ip(String f_ip) {
        this.f_ip = f_ip;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public int getM_id() {
        return m_id;
    }

    public void setM_id(int m_id) {
        this.m_id = m_id;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

	/*
	 * public User getUser() { return user; }
	 * 
	 * public void setUser(User user) { this.user = user; }
	 */

}
