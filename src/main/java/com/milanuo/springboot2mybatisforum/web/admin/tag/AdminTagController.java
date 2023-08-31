package com.milanuo.springboot2mybatisforum.web.admin.tag;

import com.milanuo.springboot2mybatisforum.core.PageResult.BasePageResult;
import com.milanuo.springboot2mybatisforum.core.Query4Object.Query4Object;
import com.milanuo.springboot2mybatisforum.core.Query4Object.Query4Topics;
import com.milanuo.springboot2mybatisforum.core.ajax.AjaxResult;
import com.milanuo.springboot2mybatisforum.module.tags.pojo.Tags;
import com.milanuo.springboot2mybatisforum.module.tags.pojo.TagsWithNum;
import com.milanuo.springboot2mybatisforum.module.tags.service.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/admin/tag")
public class AdminTagController {

    @Autowired
    private TagsService tagsService;

    @GetMapping("/list")
    public String list(Integer pageNum,Model model){

        Query4Topics query4Topics = new Query4Topics();
        if(pageNum==null){
            query4Topics.setPageNum(1);
        }else{
            query4Topics.setPageNum(pageNum);
        }
        query4Topics.setPageSize(20);

        List<TagsWithNum> tagsWithNums = tagsService.getAllTags(query4Topics);
        BasePageResult basePageResult = new BasePageResult();
        basePageResult.setPageNum(query4Topics.getPageNum());
        basePageResult.setPageSize(query4Topics.getPageSize());
        basePageResult.setTotalCount(tagsService.getAllTagsCount(query4Topics));
        model.addAttribute("tagsWithNums",tagsWithNums);
        model.addAttribute("basePageResult",basePageResult);

        return "admin/tag/list";
    }

    @GetMapping("/editPage")
    public String editPage(Integer id,Model model){

        if(id!=null){
            Tags tag = tagsService.getTagById(id);
            model.addAttribute("tag",tag);
        }

        return "admin/tag/edit";
    }

    @PostMapping("/edit")
    public String edit(Integer id,String tag,String describ){
        Tags tags = new Tags();
        tags.setId(id);
        tags.setTag(tag);
        tags.setDescrib(describ);
        tagsService.updateTag(tags);

        return "redirect:/admin/tag/list";
    }

    @GetMapping("/delete")
    @ResponseBody
    public AjaxResult delete(Integer id){

        AjaxResult ajaxResult = new AjaxResult();

        try{
            if(id!=null){
                tagsService.deleteTagById(id);
            }
            ajaxResult.setSuccessful(true);
            ajaxResult.setDescribe("删除成功");
        }catch (Exception e){
            e.printStackTrace();
            ajaxResult.setSuccessful(false);
            ajaxResult.setDescribe("删除失败，请重试");
        }

        return ajaxResult;
    }

}
