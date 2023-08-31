package com.milanuo.springboot2mybatisforum.web.admin.permission;

import com.alibaba.druid.support.json.JSONUtils;
import com.milanuo.springboot2mybatisforum.core.ajax.AjaxResult;
import com.milanuo.springboot2mybatisforum.module.web.pojo.SysPermission;
import com.milanuo.springboot2mybatisforum.module.web.service.RolePermissionService;
import com.milanuo.springboot2mybatisforum.module.web.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/admin/permission")
public class PermissionController {

    @Autowired
    private SysPermissionService sysPermissionService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @GetMapping("/list")
    public String list(Model model){
        List<SysPermission> sysPermissionList = sysPermissionService.getAll();
        model.addAttribute("sysPermissionList",sysPermissionList);
        return "admin/permission/list";
    }

    @GetMapping("/editPage")
    public String editPage(Integer id,Model model){
        SysPermission sysPermission = sysPermissionService.getPerById(id);
        model.addAttribute("sysPermission",sysPermission);
        return "admin/permission/edit";
    }

    @PostMapping("/editSave")
    @ResponseBody
    public AjaxResult editSave(Integer id,String name,String url){
        AjaxResult ajaxResult = new AjaxResult();

        try {
            SysPermission sysPermission = new SysPermission();
            sysPermission.setId(id);
            sysPermission.setName(name);
            sysPermission.setUrl(url);
            sysPermissionService.update(sysPermission);
            ajaxResult.setDescribe("编辑成功");
            ajaxResult.setSuccessful(true);
        }catch (Exception e){
            e.printStackTrace();
            ajaxResult.setSuccessful(false);
            ajaxResult.setDescribe("编辑失败，"+e.getMessage());
        }

        return ajaxResult;
    }

    @GetMapping("/addPage")
    public String addPage(){
        return "admin/permission/add";
    }

    @PostMapping("/add")
    @ResponseBody
    public AjaxResult add(String name,String url){
        AjaxResult ajaxResult = new AjaxResult();

        try {
            SysPermission sysPermission = new SysPermission();
            sysPermission.setName(name);
            sysPermission.setUrl(url);
            sysPermissionService.save(sysPermission);
            ajaxResult.setDescribe("添加成功");
            ajaxResult.setSuccessful(true);
        }catch (Exception e){
            e.printStackTrace();
            ajaxResult.setSuccessful(false);
            ajaxResult.setDescribe("添加失败，"+e.getMessage());
        }

        return ajaxResult;
    }


    @GetMapping("/delete")
    @ResponseBody
    public AjaxResult delete(Integer id){
        AjaxResult ajaxResult = new AjaxResult();

        try {
            //先删除联合表中的数据
            rolePermissionService.deleteByPermId(id);
            //再删除权限表中的数据
            sysPermissionService.delete(id);
            ajaxResult.setDescribe("删除成功");
            ajaxResult.setSuccessful(true);
        }catch (Exception e){
            e.printStackTrace();
            ajaxResult.setSuccessful(false);
            ajaxResult.setDescribe("删除失败，"+e.getMessage());
        }

        return ajaxResult;
    }
}
