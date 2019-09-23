package com.win.dfas.deploy.common.aspect;

import com.win.dfas.deploy.common.annotation.SysLog;
import com.win.dfas.deploy.modules.sys.po.SysLogPO;
import com.win.dfas.deploy.modules.sys.po.SysUserPO;
import com.win.dfas.deploy.modules.sys.service.SysLogService;
import com.win.dfas.deploy.util.HttpContextUtils;
import com.win.dfas.deploy.util.IPUtils;
import com.win.dfas.deploy.util.JSONUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

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
        SysLog syslog = method.getAnnotation(SysLog.class);
        if(syslog != null){
            //注解上的描述
            sysLogPO.setOperation(syslog.value());
        }

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
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
        sysLogPO.setIp(IPUtils.getIpAddr(request));

        //用户名
//        String username = ((SysUserPO) SecurityUtils.getSubject().getPrincipal()).getUsername();
        sysLogPO.setUsername("admin");

        sysLogPO.setTime(time);
        //保存系统日志
        sysLogService.save(sysLogPO);
    }
}
