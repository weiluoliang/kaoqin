package com.liang.vo;

import lombok.Data;

@Data
public class ResultVo <T>{

    private int code;

    private String msg;

    private T data;

    public static <T> ResultVo<T> success(T data){
        ResultVo<T> resultVo = new ResultVo<>();
        resultVo.setCode(200);
        resultVo.setMsg("success");
        resultVo.setData(data);
        return resultVo;
    }
}
