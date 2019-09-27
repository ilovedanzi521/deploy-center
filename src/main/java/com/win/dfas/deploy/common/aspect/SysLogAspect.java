package com.win.dfas.deploy.common.aspect;

import com.win.dfas.deploy.common.annotation.SysLog;
import com.win.dfas.deploy.common.enumerate.LogEnum;
import com.win.dfas.deploy.po.SysLogPO;
import com.win.dfas.deploy.service.SysLogService;
import com.win.dfas.deploy.util.HttpContextUtils;
import com.win.dfas.deploy.util.IpUtils;
import com.win.dfas.deploy.util.JSONUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @包名 com.win.dfas.deploy.common.aspect
 * @类名 SysLogAspect
 * @类描述 系统日志切面
 * @创建人 heshansen
 * @创建时间 2019/09/23 16:54
 */
@Aspect
@Component
public class SysLogAspect {
    @Autowired
    private SysLogService sysLogService;

    @Pointcut("@annotation(com.win.dfas.deploy.common.annotation.SysLog)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;

        //保存日志
        saveSysLog(point, time);

        return result;
    }

    private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        SysLogPO sysLogPO = new SysLogPO();
        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        //注解上的描述
        SysLog syslog = method.getAnnotation(SysLog.class);
        if(syslog != null){
            String operate = syslog.value();
            if (StringUtils.isEmpty(operate)){
                operate = LogEnum.getOperationDesc(methodName,className.substring(className.lastIndexOf(".")+1));
            }
            sysLogPO.setOperation(operate);
        }
        sysLogPO.setMethod(className + "." + methodName + "()");
        //请求的参数
        Object[] args = joinPoint.getArgs();
        try{
            String params = JSONUtil.toJson(args);
            sysLogPO.setParams(params);
        }catch (Exception e){

        }

        //获取request
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        //设置IP地址
        sysLogPO.setIp(IpUtils.getIpAddr(request));

        //用户名
//        String username = ((SysUserPO) SecurityUtils.getSubject().getPrincipal()).getUsername();
        sysLogPO.setUsername("admin");

        sysLogPO.setTime(time);
        //保存系统日志
        sysLogService.save(sysLogPO);
    }
}
