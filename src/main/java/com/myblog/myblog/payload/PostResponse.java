package com.myblog.myblog.payload;

import lombok.Data;

import java.util.List;

@Data
public class PostResponse {
    private List<PostDto> content;
    private int pageNo;
    private int pageSize;
    private int totalElements;
    private int totalPages;
    private boolean isLast;

}
