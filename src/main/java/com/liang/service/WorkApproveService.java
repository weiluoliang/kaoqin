package com.liang.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.liang.convert.WorkApproveConvert;
import com.liang.entities.WorkApprovePO;
import com.liang.mapper.WorkApproveMapper;
import com.liang.vo.WorkApproveVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkApproveService {

    @Autowired
    private WorkApproveMapper workApproveMapper;

    public List<WorkApproveVo> list() {
        List<WorkApprovePO> poList = workApproveMapper.selectList(new LambdaQueryWrapper<>());
        List<WorkApproveVo> voList = WorkApproveConvert.INSTANCE.toVOs(poList);
        return voList;
    }

}
