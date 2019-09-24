export default class PageVO {
    /**
     * 总数量
     */
    public total: number;
    /**
     * 当前页,默认为1
     */
    public pageNum: number = 1;
    /**
     * 每页行数, 默认为10
     */
    public pageSize: number = 10;
    /**
     * 数据
     */
    public list: any[];
}
