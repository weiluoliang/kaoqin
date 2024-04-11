package com.liang.third.work;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.liang.third.work.body.Data;
import com.liang.third.work.body.Result;
import com.liang.third.work.body.Xcxdata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class WorkWeixinHandler {

    private static final int size = 10;
    RestTemplate client = new RestTemplate();

    public List<Xcxdata> getList(Map<String, String> headerMaps, int page) {
        String url = "https://app.work.weixin.qq.com/wework_admin/approval/api/get_approval_list?lang=zh_CN&ajax=1&f=json&random=876422";
        String body = "limit="+size+"&filter=myapply&order_name=&template_id=&creator_vid=&start_time=&end_time=&begin_arrive_time=&end_arrive_time=&read_status=&apply_type=&keyword=&sp_status=2&app_order_direction=0&use_new_search=true";
        Map<String, List<String>> stringListMap = HttpUtil.decodeParams(body, Charset.defaultCharset());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headerMaps.forEach(headers::add);
        MultiValueMap<String, String> params= new LinkedMultiValueMap<>();
        params.putAll(stringListMap);
        params.set("offset",String.valueOf((page-1)*size));
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = client.exchange(url, HttpMethod.POST, requestEntity, String.class);
       // log.info("body = {}",response.getBody() );
        Result<Data> result = JSON.parseObject(response.getBody(),new TypeReference<Result<Data>>(){});
        return result.getData().getXcxdata();
    }


    public Integer getDuration(String spId,Map<String, String> headerMaps) {
        String url = "https://app.work.weixin.qq.com/wework_admin/approval/api/get_approval_detail?is_approval_v3=true&sp_id="+spId+"&sp_no=&is_from=creator&timezone_offset=-8&filter=&lang=zh_CN&ajax=1&f=json&random=574927&timeZoneInfo%5BzoneOffset%5D=8&timeZoneInfo%5BzoneId%5D=Asia%2FShanghai&timeZoneInfo%5BzoneDesc%5D=UTC%2B8";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headerMaps.forEach(headers::add);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = client.exchange(url, HttpMethod.GET, entity, String.class);
        String body = response.getBody();
       // System.out.println("body =  " + body );
        JSONObject data = JSON.parseObject(body);
        JSONArray jsonArray = data.getJSONObject("data")
                .getJSONObject("data")  //
                .getJSONObject("apply_data")//
                .getJSONArray("contents");

        Integer duration = 0 ;
        for (int i = 0; i < jsonArray.size() ; i++) {
            JSONObject o = jsonArray.getJSONObject(i);
            if("Attendance".equals(o.getString("control"))){
                duration = o.getJSONObject("value")//
                        .getJSONObject("attendance")//
                        .getJSONObject("date_range")//
                        .getInteger("duration");//
                break;
            } else if ("Vacation".equals(o.getString("control"))) {
                duration = o.getJSONObject("value")//
                        .getJSONObject("vacation")//
                        .getJSONObject("attendance")//
                        .getJSONObject("date_range")//
                        .getInteger("duration");//
            }
        }
        return duration;
    }

}
