package com.milanuo.springboot2mybatisforum.web.admin.comment;

import com.milanuo.springboot2mybatisforum.core.PageResult.BasePageResult;
import com.milanuo.springboot2mybatisforum.core.Query4Object.Query4Topics;
import com.milanuo.springboot2mybatisforum.core.ajax.AjaxResult;
import com.milanuo.springboot2mybatisforum.module.reply.pojo.Reply;
import com.milanuo.springboot2mybatisforum.module.reply.pojo.ReplyAdmin;
import com.milanuo.springboot2mybatisforum.module.reply.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/admin/comment")
public class AdminCommentController {

    @Autowired
    private ReplyService replyService;

    @GetMapping("/list")
    public String list(Integer pageNo,Model model){
        Query4Topics query4Topics = new Query4Topics();
        if(pageNo!=null){
            query4Topics.setPageNum(pageNo);
        }else{
            query4Topics.setPageNum(1);
        }
        query4Topics.setPageSize(20);
        List<ReplyAdmin> replyList = replyService.getAllReply(query4Topics);
        BasePageResult basePageResult = new BasePageResult();
        basePageResult.setPageNum(query4Topics.getPageNum());
        basePageResult.setPageSize(query4Topics.getPageSize());
        basePageResult.setTotalCount(replyService.getAllReplyCount(query4Topics));
        model.addAttribute("replyList",replyList);
        model.addAttribute("basePageResult",basePageResult);

        return "admin/comment/list";
    }

    @GetMapping("/editPage")
    public String editPage(Integer id,Model model){

        ReplyAdmin replyAdmin = replyService.getReplyAdmin(id);
        model.addAttribute("replyAdmin",replyAdmin);

        return "admin/comment/edit";
    }

    @PostMapping("/editSave")
    @ResponseBody
    public AjaxResult editSave(Integer id,String content){
        AjaxResult ajaxResult = new AjaxResult();
        Reply reply = new Reply();
        try{
            reply.setId(id);
            reply.setContent(content);
            replyService.update(reply);
            ajaxResult.setSuccessful(true);
            ajaxResult.setDescribe("修改成功");
        }catch (Exception e){
            e.printStackTrace();
            ajaxResult.setSuccessful(false);
            ajaxResult.setDescribe("修改失败，请重试");
        }

        return ajaxResult;
    }

    @GetMapping("/delete")
    @ResponseBody
    public AjaxResult delete(Integer id){
        AjaxResult ajaxResult = new AjaxResult();

        try{
            replyService.deleteById(id);
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
