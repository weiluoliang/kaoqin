package com.liang.convert;

import com.liang.entities.WorkApprovePO;
import com.liang.vo.WorkApproveVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface WorkApproveConvert {

    WorkApproveConvert INSTANCE = Mappers.getMapper(WorkApproveConvert.class);

    List<WorkApproveVo> toVOs(List<WorkApprovePO> poList);

    WorkApproveVo map(WorkApprovePO value);
}
