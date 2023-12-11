package com.myblog.myblog.service.impl;

import com.myblog.myblog.entity.Post;
import com.myblog.myblog.exception.ResorceNotFoundException;
import com.myblog.myblog.payload.PostDto;
import com.myblog.myblog.payload.PostResponse;
import com.myblog.myblog.repository.PostRepository;
import com.myblog.myblog.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
     Sort sort =  sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())?
                Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();
        PageRequest pageable = PageRequest.of(pageNo,pageSize,sort);
        Page<Post> content = postRepository.findAll(pageable);
        List<Post> posts = content.getContent();
       List<PostDto> dto= posts.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());

//--------------------------------------------------------------------------29/03/23-------------------------------------------------------------------------------
        PostResponse postResponse = new PostResponse();
        postResponse.setContent(dto);
        postResponse.setPageNo(content.getNumber());
        postResponse.setPageSize(content.getSize());
        postResponse.setTotalPages(content.getTotalPages());
        postResponse.setTotalElements((int)content.getTotalElements());
        postResponse.setLast(content.isLast());

//        return  posts.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());                  // This is Old Code 28/03/23

        return postResponse;
    }

    @Override
    public PostDto createPost(PostDto postDto) {


        // convert DTO to Entity
        Post post = mapToEntity(postDto);
        Post newPost = postRepository.save(post);

        // Convert Entity to DTO
       PostDto postResponse = mapToDTO(newPost);
       return postResponse;

    }

    @Override
    public PostDto getPostDto(long id) {
     Post post  =  postRepository.findById(id).orElseThrow(
                ()-> new ResorceNotFoundException("Post","Id", id)
        );
        return mapToDTO(post);

    }

    @Override
    public PostDto updatePost(PostDto postDto, long id) {
      Post post =  postRepository.findById(id).orElseThrow(
                ()-> new ResorceNotFoundException("Post","Id",id)
        );
      post.setTitle(postDto.getTitle());
      post.setDescription(postDto.getDescription());
      post.setContent(postDto.getContent());

        Post updatedPost = postRepository.save(post);
        return mapToDTO(updatedPost);
    }

    @Override
    public void deletePostById(long id) {
       Post post = postRepository.findById(id).orElseThrow(
                ()-> new ResorceNotFoundException("Post", "Id",id)
        );
       postRepository.deleteById(id);
    }

    private PostDto mapToDTO(Post newPost) {
        PostDto postDto = new PostDto();
        postDto.setId(newPost.getId());
        postDto.setTitle(newPost.getTitle());
        postDto.setDescription(newPost.getDescription());
        postDto.setContent(newPost.getContent());
        return postDto;
    }

    private Post mapToEntity(PostDto postDto) {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        return post;
    }
}
