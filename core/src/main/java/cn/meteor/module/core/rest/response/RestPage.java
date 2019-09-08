package cn.meteor.module.core.rest.response;

import java.util.Collections;
import java.util.List;

public class RestPage<T> {

    private static final long serialVersionUID = 8545996863226528798L;

    /**
     * 查询数据列表
     */
    private List<T> records = Collections.emptyList();
    
    /**
     * 总数
     */
    private long total = 0;
    
    /**
     * 每页显示条数，默认 10
     */
    private long size = 10;
    
    /**
     * 当前页
     */
    private long current = 1;
    
    /**
     * <p>
     * SQL 排序 ASC 数组
     * </p>
     */
    private String[] ascs;
    
    /**
     * <p>
     * SQL 排序 DESC 数组
     * </p>
     */
    private String[] descs;
    
//    /**
//     * <p>
//     * 自动优化 COUNT SQL
//     * </p>
//     */
//    private boolean optimizeCountSql = true;
    
    /**
     * <p>
     * 是否进行 count 查询
     * </p>
     */
    private boolean isSearchCount = true;
    
    public Long getPages() {
        if (getSize() == 0) {
            return 0L;
        }
        long pages = getTotal() / getSize();
        if (getTotal() % getSize() != 0) {
            pages++;
        }
        return pages;
    }

	public List<T> getRecords() {
		return records;
	}

	public void setRecords(List<T> records) {
		this.records = records;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getCurrent() {
		return current;
	}

	public void setCurrent(long current) {
		this.current = current;
	}

	public String[] getAscs() {
		return ascs;
	}

	public void setAscs(String[] ascs) {
		this.ascs = ascs;
	}

	public String[] getDescs() {
		return descs;
	}

	public void setDescs(String[] descs) {
		this.descs = descs;
	}

//	public boolean isOptimizeCountSql() {
//		return optimizeCountSql;
//	}
//
//	public void setOptimizeCountSql(boolean optimizeCountSql) {
//		this.optimizeCountSql = optimizeCountSql;
//	}

	public boolean isSearchCount() {
		return isSearchCount;
	}

	public void setSearchCount(boolean isSearchCount) {
		this.isSearchCount = isSearchCount;
	}
    

    
}
