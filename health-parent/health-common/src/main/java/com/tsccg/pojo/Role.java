package com.tsccg.pojo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 角色
 */
public class Role implements Serializable {
    private Integer id;
    private String name; // 角色名称
    private String keyword; // 角色关键字，用于权限控制
    private String description; // 描述
    private Set<User> users = new HashSet<User>(0);
    private Set<Permission> permissions = new HashSet<Permission>(0);
    private LinkedHashSet<Menu> menus = new LinkedHashSet<Menu>(0);

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public LinkedHashSet<Menu> getMenus() {
        return menus;
    }

    public void setMenus(LinkedHashSet<Menu> menus) {
        this.menus = menus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Role)) {
            return false;
        }
        Role role = (Role) o;
        return Objects.equals(getId(), role.getId()) &&
                Objects.equals(getName(), role.getName()) &&
                Objects.equals(getKeyword(), role.getKeyword()) &&
                Objects.equals(getDescription(), role.getDescription()) &&
                Objects.equals(getUsers(), role.getUsers()) &&
                Objects.equals(getPermissions(), role.getPermissions()) &&
                Objects.equals(getMenus(), role.getMenus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getKeyword(), getDescription(), getUsers(), getPermissions(), getMenus());
    }
}
