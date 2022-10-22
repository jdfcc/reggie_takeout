package com.jdfcc.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler{

    /**
     * 异常处理方法
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R exceptionhandler(SQLIntegrityConstraintViolationException exception){
        log.error("Error: {}",exception.getMessage());

        if(exception.getMessage().contains("Duplicate")){
            String[] msgs=exception.getMessage().split(" ");
            String msg=msgs[2]+"already exists";
            return R.error(msg);
        }
        return R.error("Unknow error");
    }
}
