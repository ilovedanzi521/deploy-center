package com.win.dfas.deploy.vo.response;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @包名 com.win.dfas.deploy.vo.response
 * @类名 PageVO
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/10/09 20:16
 */
@Data
public class PageVO<T> implements Serializable {
    private static final long serialVersionUID = 7152867562738107787L;
    /**
     * 总记录数
     */
    private long total;
    /**
     * 每页记录数
     */
    private int pageSize;
    /**
     * 总页数
     */
    private int pages;
    /**
     * 当前页数
     */
    private int pageNum;
    /**
     * 列表数据
     */
    private List<T> list;
    /**
     * 分页
     * @param list        列表数据
     * @param total  总记录数
     * @param pageSize    每页记录数
     * @param pageNum    当前页数
     */
    public PageVO(List<T> list, long total, int pageSize, int pageNum) {
        this.list = list;
        this.total = total;
        this.pageSize = pageSize;
        this.pageNum = pageNum;
        this.pages = (int)Math.ceil((double)total/pageSize);
    }

    /**
     * 分页（将分页参数转成平台统一分页参数）
     */
    public PageVO(Page<T> page) {
        this.list = page.getRecords();
        this.total = page.getTotal();
        this.pageSize = (int)page.getSize();
        this.pageNum = (int)page.getCurrent();
        this.pages = (int)page.getPages();
    }

    /**
     * 分页（用于列表原数转换）
     */
    public PageVO(Page page, List<T> list) {
        this.total = page.getTotal();
        this.pageSize = (int)page.getSize();
        this.pageNum = (int)page.getCurrent();
        this.pages = (int)page.getPages();
        this.list = list;
    }
}
