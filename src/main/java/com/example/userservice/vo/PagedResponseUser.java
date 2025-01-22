package com.example.userservice.vo;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PagedResponseUser {
    private List<ResponseUser> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;

    public PagedResponseUser(Page<ResponseUser> page) {
        this.content = page.getContent();
        this.pageNo = page.getNumber();
        this.pageSize = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.last = page.isLast();
    }
}
