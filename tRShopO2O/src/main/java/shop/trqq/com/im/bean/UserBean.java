package shop.trqq.com.im.bean;

import org.litepal.crud.DataSupport;


public class UserBean extends DataSupport {


    private int u_id;

    private String u_name;

    private int s_id;

    private String update_time;

    private String connected;

    private String s_name;

    private String avatar;

    private String s_avatar;

    private String disconnect_time;

    private String online;

    private String time;

    private String recent;

    private String friend;

    private int unreadnum;

    public int getUnreadnum() {
        return unreadnum;
    }

    public void setUnreadnum(int unreadnum) {
        this.unreadnum = unreadnum;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRecent() {
        return recent;
    }

    public void setRecent(String recent) {
        this.recent = recent;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }


    public String getU_name() {
        return u_name;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }


    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getConnected() {
        return connected;
    }

    public void setConnected(String connected) {
        this.connected = connected;
    }

    public String getS_name() {
        return s_name;
    }

    public void setS_name(String s_name) {
        this.s_name = s_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getS_avatar() {
        return s_avatar;
    }

    public void setS_avatar(String s_avatar) {
        this.s_avatar = s_avatar;
    }

    public String getDisconnect_time() {
        return disconnect_time;
    }

    public void setDisconnect_time(String disconnect_time) {
        this.disconnect_time = disconnect_time;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public int getU_id() {
        return u_id;
    }

    public void setU_id(int u_id) {
        this.u_id = u_id;
    }

    public int getS_id() {
        return s_id;
    }

    public void setS_id(int s_id) {
        this.s_id = s_id;
    }


}
