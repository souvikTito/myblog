package com.myblog.myblog.service;

import com.myblog.myblog.payload.PostDto;
import com.myblog.myblog.payload.PostResponse;

public interface PostService {

   PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);

   PostDto createPost(PostDto postDto);

   PostDto getPostDto(long id);

   PostDto updatePost(PostDto postDto, long id);

   void deletePostById(long id);
}
