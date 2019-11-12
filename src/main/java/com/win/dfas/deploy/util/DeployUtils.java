package com.win.dfas.deploy.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.win.dfas.deploy.common.enumerate.DeployEnum;
import com.win.dfas.deploy.common.exception.BaseException;
import com.win.dfas.deploy.po.StrategyPO;
import com.win.dfas.deploy.po.TaskPO;
import com.win.dfas.deploy.schedule.bean.DeployEnvBean;
import com.win.dfas.deploy.service.AppModuleService;
import com.win.dfas.deploy.service.DeviceModuleService;
import com.win.dfas.deploy.service.StrategyService;
import com.win.dfas.deploy.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @包名 com.win.dfas.deploy.util
 * @类名 DeployUtils
 * @类描述 文件操作扩展工具类
 * @创建人 heshansen
 * @创建时间 2019/10/22 11:37
 */
@Slf4j
public class DeployUtils {

    /**
     * 保存文件到零时目录
     * @param file
     * @param deployEnv
     * @return
     * @throws IOException
     */
    public static File saveZipToTempFile(MultipartFile file, DeployEnvBean deployEnv){
        String tmpDir = FileUtil.getTmpDirPath()+File.separator+"deploy"+File.separator+file.getOriginalFilename();
        log.info("save file to "+tmpDir);
        File tempFile = new File(tmpDir);
        if(!tempFile.getParentFile().exists()) {
            tempFile.getParentFile().mkdirs();
        }
        try {
            tempFile.createNewFile();
            file.transferTo(tempFile);
        } catch (IOException e) {
            FileUtil.del(tempFile);
            log.error("保存文件异常！",e);
        }

        return tempFile;
    }

    /**
     * 解析部署包
     * @param file
     * @param deployEnv
     * @throws IOException
     */
    public static void parseDeployZipFile(MultipartFile file, DeployEnvBean deployEnv) throws IOException {
        log.info("pareZipFile开始解析zip文件....");
        if (!file.getOriginalFilename().endsWith(".zip")) {
            throw new IOException("不是zip格式文件:"+file.getOriginalFilename());
        }
        ZipInputStream zipInputStream = new ZipInputStream(file.getInputStream(), Charset.forName("UTF-8"));
        ZipEntry zipEntry;
//        是否含升级文件
        boolean hasReleaseDescFile =false;
//        是否含应用模块脚本文件
        boolean hasModuleDir = false;
//        是否含jar包目录
        boolean hasPackageDir = false;
//        是否含策略脚本文件
        boolean hasScriptDir = false;
//        是否含jar包文件
        boolean hasJarFile = false;
        String moduleDirName = getLastDirName(deployEnv.getModulesDir());
        String packageDirName = getLastDirName(deployEnv.getPackagesDir());
        String scriptDirName = getLastDirName(deployEnv.getScriptsDir());
        String subFileName = null;
        // 获取zip包中的每一个zip file entry
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            subFileName = FilePathUtils.getRealFilePath(zipEntry.getName());
            log.info("saveZipToTempFile*****zipFileName="+subFileName);
            Assert.notNull(subFileName, "压缩包中子文件的名字格式不正确");
            if (subFileName.equals(deployEnv.getReleaseDescFile()) && zipEntry.getSize()>0) {
                hasReleaseDescFile=true;
            }
            if (subFileName.endsWith(".jar") && subFileName.startsWith(packageDirName)){
                hasJarFile=true;
            }
//            判断必须文件夹是否存在
            if (zipEntry.isDirectory()){
                if (subFileName.equals(moduleDirName)){
                    hasModuleDir =true;
                }else if (subFileName.equals(packageDirName)){
                    hasPackageDir=true;
                }else if (subFileName.equals(scriptDirName)){
                    hasScriptDir = true;
                }
            }else {
//                如果策略已经部署，立即停止上传
                if (subFileName.startsWith(scriptDirName)){
                    String strategyPath = subFileName.substring(scriptDirName.length());
                    if(checkDeployed(strategyPath)){
                        throw new BaseException("策略["+strategyPath+"]已经部署，请卸载后再上传！");
                    }
                }
            }

        }
        if (!hasReleaseDescFile){
            throw new IOException("压缩包缺失升级文件："+deployEnv.getReleaseDescFile());
        }
        if (!hasPackageDir){
            throw new IOException("压缩包缺失根包目录："+packageDirName);
        }else if (!hasJarFile){
            throw new IOException("压缩包"+packageDirName+"中缺失jar包文件！");
        }
        if (!hasModuleDir){
            throw new IOException("压缩包缺失应用模块根目录："+moduleDirName);
        }
        if (!hasScriptDir){
            throw new IOException("压缩包缺失策略根目录："+moduleDirName);
        }
    }

    private static boolean checkDeployed(String strategyPath) {
        if (StrUtil.isNotEmpty(strategyPath)) {
            log.info("check deployed by strategy "+strategyPath);
            StrategyService strategyService = SpringContextUtils.getBean(StrategyService.class);
            QueryWrapper queryWrapper=new QueryWrapper();
            queryWrapper.eq("path",strategyPath);
            StrategyPO strategyPO = strategyService.getOne(queryWrapper);
            if (strategyPO!=null){
                TaskService taskService = SpringContextUtils.getBean(TaskService.class);
                QueryWrapper taskQuery=new QueryWrapper();
                taskQuery.eq("strategy_id",strategyPO.getId());
                TaskPO taskPO = taskService.getOne(taskQuery);
                if (taskPO!=null && DeployEnum.TaskStatus.isDeployd(taskPO.getStatus())){
                    return true;
                }
            }
        }
        return false;
    }

    private static String getLastDirName(String path) {
        String lastDir = "";
        if (StrUtil.isNotBlank(path)){
            lastDir = path.substring(path.lastIndexOf(File.separator)+1);
            if (!lastDir.endsWith(File.separator)){
                lastDir+=File.separator;
            }
        }
        return lastDir;
    }

    public static void main (String[] args){
        String parentDir = "modules/";
        String path = "modules/java_ms_registe_center.sh";
        System.out.println(path.startsWith(parentDir));
        System.out.println(parentDir.startsWith(parentDir));
        System.out.println(parentDir.substring(parentDir.length()));
        if (path.indexOf(parentDir)>0){
            String modulePath = path.substring(2,path.length());
            System.out.println(modulePath);
        }

    }
}
