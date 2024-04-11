package com.liang.controller;

import com.liang.service.WorkApproveService;
import com.liang.vo.ResultVo;
import com.liang.vo.WorkApproveVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/workApprove")
public class WorkApproveController {

    @Autowired
    private WorkApproveService workApproveService;


    @RequestMapping("/list")
    public ResultVo<List<WorkApproveVo>> list(){
        List<WorkApproveVo> list = workApproveService.list();
        return ResultVo.success(list);
    }

    @RequestMapping("/syncData")
    public ResultVo<Void> syncData(){
        workApproveService.syncData();
        return ResultVo.ok();
    }
}
