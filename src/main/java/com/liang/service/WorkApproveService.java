package com.liang.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.liang.convert.WorkApproveConvert;
import com.liang.entities.HeadersPO;
import com.liang.entities.WorkApprovePO;
import com.liang.mapper.HeadersMapper;
import com.liang.mapper.WorkApproveMapper;
import com.liang.third.work.WorkWeixinHandler;
import com.liang.third.work.body.Xcxdata;
import com.liang.vo.WorkApproveVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WorkApproveService {

    @Autowired
    private WorkApproveMapper workApproveMapper;
    @Autowired
    private HeadersMapper headersMapper;
    @Autowired
    private WorkWeixinHandler workWeixinHandler;
    static List<String> types = ListUtil.toList("请假", "加班");

    public List<WorkApproveVo> list() {
        List<WorkApprovePO> poList = workApproveMapper.selectList(new LambdaQueryWrapper<>());
        List<WorkApproveVo> voList = WorkApproveConvert.INSTANCE.toVOs(poList);
        return voList;
    }

    public void syncData() {
        Map<String,String> headerMaps = getHeaders();


        List<List<Object>> rows = new ArrayList<>();
        BigDecimal totalOvertime = new BigDecimal(0); // 加班
        BigDecimal totalLeave = new BigDecimal(0); // 调休
        BigDecimal totalAnnualLeave = new BigDecimal(0); // 年假

        for (int page = 1 ;; page++) {
            List<Xcxdata> list = workWeixinHandler.getList(headerMaps,page );
            if(CollUtil.isEmpty(list)){
                log.info("查询完毕 ！！！ rows size={}", rows.size() );
                break;
            }
            log.info("第{}页,size={}",page,list.size());
            for (Xcxdata item : list) {
                String spId = item.getSp_id();
                if (!types.contains(item.getTemplate_name())) {
                    log.info("过滤掉类型 ： {}", item.getTemplate_name() );
                    continue;
                }
                // 请假时常
                Integer duration= workWeixinHandler.getDuration(spId,headerMaps);
                // 计算小时
                BigDecimal hours = new BigDecimal(duration).divide(new BigDecimal(60*60),2, RoundingMode.HALF_UP);

                List<String> summaryList = item.getSummary_list();
                String type = summaryList.get(0).split("：")[1];
                String startTime = summaryList.get(1).split("：")[1];
                String endTime = summaryList.get(2).split("：")[1];

                if(hours.compareTo(new BigDecimal(9)) >= 0 && "加班".equals(type)){
                    hours = new BigDecimal("7.5");
                    log.info("如果是9小时改为7.5小时，type={}", type );
                } else if (hours.compareTo(new BigDecimal(9)) == 0 && "调休假".equals(type)) {
                    hours = new BigDecimal("7.5");
                    log.info("如果是9小时改为7.5小时，type={}", type );
                }

                ArrayList<Object> row = ListUtil.toList(type, startTime, endTime,hours);
                rows.add(row);

                if("调休假".equals(type)){
                    totalLeave = totalLeave.add(hours);
                } else if ("年假".equals(type)) {
                    totalAnnualLeave = totalAnnualLeave.add(hours);
                }else if ("加班".equals(type)) {
                    totalOvertime = totalOvertime.add(hours);
                }
            }
        }
        log.info("总记录数 = {} " , rows.size() );

        // xx天xx小时
        log.info("调休假： {}, hours={}", getDaysAndHours(totalLeave),totalLeave);
        // xx天
        log.info("年假： {},hours={}", totalAnnualLeave.divide(new BigDecimal(24)),totalAnnualLeave);
        // xx天xx小时
        log.info("加班： {},hours={}" , getDaysAndHours(totalOvertime),totalOvertime);
    }


    private Map<String, String> getHeaders() {
        List<HeadersPO> headersPOS = headersMapper.selectList(new LambdaQueryWrapper<>());
        return headersPOS.stream().collect(Collectors.toMap(HeadersPO::getHeaderName, HeadersPO::getHeaderValue));
    }

    private static String getDaysAndHours(BigDecimal data) {
        // 取余数
        BigDecimal[] results = data.divideAndRemainder(new BigDecimal("7.5"));
        return results[0]+"天"+ results[1]+"小时";
    }
}
