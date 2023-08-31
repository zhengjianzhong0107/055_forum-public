package com.milanuo.springboot2mybatisforum.web.admin.admin_role;

import com.milanuo.springboot2mybatisforum.core.ajax.AjaxResult;
import com.milanuo.springboot2mybatisforum.module.web.pojo.RolePermission;
import com.milanuo.springboot2mybatisforum.module.web.pojo.SysPermission;
import com.milanuo.springboot2mybatisforum.module.web.pojo.SysRole;
import com.milanuo.springboot2mybatisforum.module.web.service.RolePermissionService;
import com.milanuo.springboot2mybatisforum.module.web.service.SysPermissionService;
import com.milanuo.springboot2mybatisforum.module.web.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/admin/role")
public class AdminRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private SysPermissionService sysPermissionService;

    @GetMapping("/list")
    public String list(Model model) {

        List<SysRole> sysRoleList = sysRoleService.getAllRole();
        model.addAttribute("sysRoleList", sysRoleList);

        return "admin/role/list";
    }

    @GetMapping("/editPage")
    public String editPage(Integer id, Model model) {
        //根据角色ID获取角色信息
        SysRole sysRole = sysRoleService.getSysRoleById(id);
        model.addAttribute("sysRole", sysRole);

        //根据角色ID获取已有的权限信息
        List<Integer> perIds = rolePermissionService.getPerIdByRoleId(id);
        model.addAttribute("perIds", perIds);

        //获取所有的权限ID和Name信息
        List<SysPermission> sysPermissionIdNameList = sysPermissionService.getAllIdName();
        model.addAttribute("sysPermissionIdNameList", sysPermissionIdNameList);

        return "admin/role/edit";
    }

    @GetMapping("/block")
    @ResponseBody
    public AjaxResult block(Integer id) {

        AjaxResult ajaxResult = new AjaxResult();
        SysRole sysRole = new SysRole();

        try {
            sysRole.setId(id);
            sysRole.setAvailable((byte) 2);
            sysRoleService.update(sysRole);
            ajaxResult.setSuccessful(true);
            ajaxResult.setDescribe("锁定成功");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult.setDescribe("锁定失败，请重试");
            ajaxResult.setSuccessful(false);
        }

        return ajaxResult;

    }

    @GetMapping("/usering")
    @ResponseBody
    public AjaxResult usering(Integer id) {

        AjaxResult ajaxResult = new AjaxResult();
        SysRole sysRole = new SysRole();

        try {
            sysRole.setId(id);
            sysRole.setAvailable((byte) 1);
            sysRoleService.update(sysRole);
            ajaxResult.setSuccessful(true);
            ajaxResult.setDescribe("启用成功");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult.setDescribe("启用失败，请重试");
            ajaxResult.setSuccessful(false);
        }

        return ajaxResult;

    }

    @GetMapping("/delete")
    @ResponseBody
    public AjaxResult delete(Integer id) {

        AjaxResult ajaxResult = new AjaxResult();

        try {
            //删除角色与权限关联表中相对应的数据
            rolePermissionService.deleteByRoleId(id);

            //删除角色
            sysRoleService.delete(id);


            ajaxResult.setSuccessful(true);
            ajaxResult.setDescribe("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult.setDescribe("删除失败，请重试");
            ajaxResult.setSuccessful(false);
        }

        return ajaxResult;
    }

    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult edit(Integer id, String role, String description, String permIds) {
        AjaxResult ajaxResult = new AjaxResult();

        try {
            //更新SysRole
            SysRole sysRole = new SysRole();
            sysRole.setId(id);
            sysRole.setRole(role);
            sysRole.setDescription(description);
            sysRoleService.update(sysRole);

            //删除role_perm联合表中所有该角色的数据
            rolePermissionService.deleteByRoleId(id);

            //重新存入新的数据
            if (permIds != null && !"".equals(permIds.trim())) {
                String[] perIds = permIds.split(",");
                for (String perId : perIds) {
                    RolePermission rolePermission = new RolePermission();
                    rolePermission.setRoleId(id);
                    rolePermission.setPerId(Integer.valueOf(perId));
                    rolePermissionService.save(rolePermission);
                }
            }
            ajaxResult.setDescribe("编辑成功");
            ajaxResult.setSuccessful(true);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult.setSuccessful(false);
            ajaxResult.setDescribe("编辑失败" + e.getMessage());
        }

        return ajaxResult;
    }

    @GetMapping("/addPage")
    public String addPage(Model model) {

        //获取所有的权限ID和Name信息
        List<SysPermission> sysPermissionIdNameList = sysPermissionService.getAllIdName();
        model.addAttribute("sysPermissionIdNameList", sysPermissionIdNameList);
        return "admin/role/add";
    }

    @PostMapping("/add")
    @ResponseBody
    public AjaxResult add(String role, String description, String permIds) {
        AjaxResult ajaxResult = new AjaxResult();

        try {
            //添加SysRole
            SysRole sysRole = new SysRole();
            sysRole.setRole(role);
            sysRole.setDescription(description);
            sysRole.setAvailable((byte)1);
            sysRoleService.save(sysRole);

            //存入相关联的权限
            if (permIds != null && !"".equals(permIds.trim())) {
                String[] perIds = permIds.split(",");
                for (String perId : perIds) {
                    RolePermission rolePermission = new RolePermission();
                    rolePermission.setRoleId(sysRole.getId());
                    rolePermission.setPerId(Integer.valueOf(perId));
                    rolePermissionService.save(rolePermission);
                }
            }
            ajaxResult.setDescribe("保存成功");
            ajaxResult.setSuccessful(true);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult.setSuccessful(false);
            ajaxResult.setDescribe("保存失败" + e.getMessage());
        }

        return ajaxResult;
    }

}
