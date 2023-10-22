package com.myblogapp.myblogapp.service.impl;

import com.myblogapp.myblogapp.entity.Comment;
import com.myblogapp.myblogapp.entity.Post;
import com.myblogapp.myblogapp.exception.ResourceNotFoundException;
import com.myblogapp.myblogapp.payload.CommentDto;
import com.myblogapp.myblogapp.payload.PostDto;
import com.myblogapp.myblogapp.payload.PostResponse;
import com.myblogapp.myblogapp.repository.CommentRepository;
import com.myblogapp.myblogapp.repository.PostRepository;
import com.myblogapp.myblogapp.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;
    private CommentRepository commentRepository;
    private ModelMapper mapper;
    public PostServiceImpl(PostRepository postRepository, ModelMapper mapper,CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository=commentRepository;
        this.mapper = mapper;
    }
    @Override
    public PostDto createPost(PostDto postDto) {
        Post post = mapToPost(postDto);
        Post newPost = postRepository.save(post);
        PostDto newPostDto = mapToDto(newPost);
        return newPostDto;
    }

    @Override
    public PostResponse getAllPosts(int pageN, int pageS, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageN,pageS,sort);
        Page<Post> posts = postRepository.findAll(pageable);
        List<Post> content = posts.getContent();

        List<PostDto> postDtos = content.stream().map(post -> mapToDto(post)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(postDtos);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getNumberOfElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }

    @Override
    public PostDto getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", id));
        return mapToDto(post);
    }

    @Override
    public PostDto updateOnePostById(long id,PostDto postDto) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        Post updatedPost = postRepository.save(post);
        return mapToDto(updatedPost);

    }

    @Override
    public void deleteOnePostById(long id) {
        Post deletingPost = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        postRepository.delete(deletingPost);
    }

    @Override
    public PostDto getAllCommentsWithPost(long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "postId", postId));
        List<Comment> byPostId = commentRepository.findByPostId(post.getId());
        List<CommentDto> comments = byPostId.stream().map(x -> mapToCommentDto(x)).collect(Collectors.toList());

        PostDto postDto = new PostDto();
        postDto.setCommentDtos(comments);
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setDescription(post.getDescription());

        return postDto;
    }

    Post mapToPost(PostDto postDto){
        Post post = mapper.map(postDto, Post.class);
//        Post post = new  Post();
//        post.setId(post.getId());
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
        return post;
    }
    PostDto mapToDto(Post post){
        PostDto postDto = mapper.map(post, PostDto.class);
//        PostDto postDto = new PostDto();
//        postDto.setId(post.getId());
//        postDto.setTitle(post.getTitle());
//        postDto.setDescription(post.getDescription());
//        postDto.setContent(post.getContent());
        return postDto;
    }
    private CommentDto mapToCommentDto(Comment comment){
        CommentDto map = mapper.map(comment, CommentDto.class);
        return map;
    }
}
