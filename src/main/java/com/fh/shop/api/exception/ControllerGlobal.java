package com.fh.shop.api.exception;

import com.fh.shop.api.common.ServerResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ControllerGlobal {
    @ExceptionHandler(GlobalException.class)
    @ResponseBody
    public ServerResponse handException(GlobalException glo){

        return ServerResponse.error(glo.getResponseEnum());
    }

}
