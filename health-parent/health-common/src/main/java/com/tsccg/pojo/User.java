package com.tsccg.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 用户
 */
public class User implements Serializable{
    private Integer id; // 主键
    private Date birthday; // 生日
    private String gender; // 性别
    private String username; // 用户名，唯一
    private String password; // 密码
    private String remark; // 备注
    private String station; // 状态
    private String telephone; // 联系电话
    private Set<Role> roles = new HashSet<Role>(0);//对应角色集合

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId()) &&
                Objects.equals(getBirthday(), user.getBirthday()) &&
                Objects.equals(getGender(), user.getGender()) &&
                Objects.equals(getUsername(), user.getUsername()) &&
                Objects.equals(getPassword(), user.getPassword()) &&
                Objects.equals(getRemark(), user.getRemark()) &&
                Objects.equals(getStation(), user.getStation()) &&
                Objects.equals(getTelephone(), user.getTelephone()) &&
                Objects.equals(getRoles(), user.getRoles());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getBirthday(), getGender(), getUsername(), getPassword(), getRemark(), getStation(), getTelephone(), getRoles());
    }
}
