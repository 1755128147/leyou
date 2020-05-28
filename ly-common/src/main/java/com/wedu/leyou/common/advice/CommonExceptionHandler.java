package com.wedu.leyou.common.advice;

import com.wedu.leyou.common.exception.LyException;
import com.wedu.leyou.common.vo.ExceptionResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 通用异常处理机制
 */
@ControllerAdvice //@ControllerAdvice 实现全局异常处理
public class CommonExceptionHandler {

    @ExceptionHandler(LyException.class) //@ExceptionHandler 注解用来指明异常的处理类型
    public ResponseEntity<ExceptionResult> handleException(LyException e) {
        return ResponseEntity.status(e.getExceptionEnum().getCode())
                .body(new ExceptionResult(e.getExceptionEnum()));
    }
}
