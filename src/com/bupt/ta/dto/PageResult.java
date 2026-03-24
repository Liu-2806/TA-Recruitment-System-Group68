package com.bupt.ta.dto;

import java.util.Collections;
import java.util.List;

/**
 * 分页结果通用对象。
 *
 * @param <T> 列表元素类型
 */
public class PageResult<T> {
    private List<T> records = Collections.emptyList();
    private int page;
    private int size;
    private long total;

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
