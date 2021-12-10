package com.tsccg.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tsccg.constant.MessageConstant;
import com.tsccg.dao.MenuDao;
import com.tsccg.dao.RoleDao;
import com.tsccg.entity.PageResult;
import com.tsccg.entity.QueryPageBean;
import com.tsccg.entity.Result;
import com.tsccg.pojo.Menu;
import com.tsccg.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Author: TSCCG
 * @Date: 2021/12/08 19:02
 */
@Service(interfaceClass = MenuService.class)
@Transactional
public class MenuServiceImpl implements MenuService{
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private RoleDao roleDao;

    /**
     * 获取所有顶级菜单
     * @return
     */
    @Override
    public List<Menu> findAllParentMenu() {
        return menuDao.findAllParentMenu();
    }

    /**
     * 添加菜单
     * @param menu
     * 请求参数：{name: "地址", linkUrl: "address.html", parentMenuId: 5, icon: "tt", attention: "无"}
     * id,name,linkUrl,priority,path,icon,description,parentMenuId,level
     * 待补全参数：priority,path,level
     */
    @Override
    public Result add(Menu menu) {
        /*
            1.完善参数信息：priority:优先级别,path:路径级别,level菜单等级
         */
        if (menu.getParentMenuId() == null) {
            //添加的是父菜单
            if (menu.getLinkUrl() != null && menu.getLinkUrl().length() > 0) {
                //一级菜单无访问路径
                return new Result(false, MessageConstant.PARENT_MENU_NO_LINK_URL);
            }
            //1.1菜单级别
            menu.setLevel(1);
            //1.2优先级别
            Integer parentCount = menuDao.findParentMenuCount();//查询所有父菜单的数量
            menu.setPriority(parentCount+1);//6
            //1.3路径级别
            menu.setPath(String.valueOf(parentCount+2));//7
        } else {
            //添加的是子菜单
            Integer parentMenuId = menu.getParentMenuId();//获取父菜单id
            //1.1菜单级别
            menu.setLevel(2);
            //1.2优先级别
            Integer childCount = menuDao.findChildCountByParentId(parentMenuId);//根据父菜单id查询已有的子菜单项数量
            menu.setPriority(childCount+1);//3
            //1.3路径级别
            Menu parentMenu = menuDao.findById(parentMenuId);//根据父菜单id查询对应的菜单信息
            //获取父菜单的路径级别
            String path = parentMenu.getPath();//1
            menu.setPath("/" + path + "-" + (childCount+1));//1-3
        }
        /*
            2.将参数信息添加到数据库
         */
        menuDao.add(menu);
        /*
            3.将刚插入的菜单id关联到角色菜单关联表的系统管理员id上
         */
        Map<String,Integer> map = new HashMap<>();
        map.put("role_id",1);
        map.put("menu_id",menu.getId());
        roleDao.addMenu(map);
        return new Result(true,MessageConstant.ADD_MENU_SUCCESS);
    }

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //取出参数
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        //调用分页助手
        PageHelper.startPage(currentPage,pageSize);
        //查询数据库
        Page<Menu> page = menuDao.findByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 根据id删除菜单
     * @param id
     */
    @Override
    public void deleteById(Integer id) {
        //1.删除当前菜单下的所有子菜单
        List<Menu> childrenMenus = menuDao.findChildrenMenuByParentId(id);
        if (childrenMenus != null && childrenMenus.size() > 0) {
            //清除所有子菜单的关联数据
            for (Menu childrenMenu : childrenMenus) {
                menuDao.deleteConnectionById(childrenMenu.getId());
            }
            //根据父菜单id删除其所有子菜单
            menuDao.deleteByParentMenuId(id);
        }
        //2.清除当前菜单在关联表中的数据
        menuDao.deleteConnectionById(id);
        //3.删除当前菜单
        menuDao.deleteById(id);
    }

    /**
     * 根据菜单id查询菜单数据
     * @param id
     * @return
     */
    @Override
    public Menu findById(Integer id) {
        return menuDao.findById(id);
    }

    /**
     * 编辑菜单
     * @param menu
     * @return 执行结果
     */
    @Override
    public Result edit(Menu menu) {
        /*
            1.判断是否为父菜单
         */
        Integer parentMenuId = menu.getParentMenuId();
        if (parentMenuId == null) {
            //如果为父菜单则不能有访问路径
            if (menu.getLinkUrl() != null && menu.getLinkUrl().length() > 0) {
                return new Result(false, MessageConstant.PARENT_MENU_NO_LINK_URL);
            }
            //父菜单的菜单等级为1
            menu.setLevel(1);
        } else {
            //如果为子菜单则不能有更下一级菜单
            Integer childCount = menuDao.findChildCountByParentId(menu.getId());
            if (childCount > 0) {
                return new Result(false,MessageConstant.CHILD_MENU_NO_CHILD);
            }
            //子菜单的菜单等级为2
            menu.setLevel(2);
        }
        /*
            2.更新数据库
         */
        menuDao.update(menu);
        return new Result(true,MessageConstant.EDIT_MENU_SUCCESS);
    }

}
