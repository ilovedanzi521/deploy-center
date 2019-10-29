package com.win.dfas.deploy.schedule.context;

import cn.hutool.core.io.file.FileWriter;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @包名 com.win.dfas.deploy.schedule.context
 * @类名 FileWriterTask
 * @类描述 异步文件写入任务
 * @创建人 heshansen
 * @创建时间 2019/10/28 17:54
 */
@Slf4j
public class FileWriterTask implements Runnable {

    /**
     * 文件写入操作：WRITE-覆盖；
     */
    public static final String WRITE="write";
    /**
     * 文件写入操作：APPEND-累加
     */
    public static final String APPEND = "append";
    /**
     * 文件路径
     */
    private String filePath;
    /**
     * 日志信息
     */
    private Collection<String> lines;
    /**
     * 操作 reset-重新开始；line-逐行；
     */
    private String operate;

    public FileWriterTask(String filePath, Collection<String> lines, String operate){
        this.filePath = filePath;
        this.lines = lines;
        this.operate = operate;
    }

    @Override
    public void run() {
        log.info("FileWriter ==> "+ Arrays.toString(lines.toArray()));
        FileWriter writer = new FileWriter(filePath);
        if (WRITE.equals(operate)){
            writer.writeLines(lines);
        }else if (APPEND.equals(operate)){
            writer.appendLines(lines);
        }
    }
}
