package com.example.demo.web;

import com.example.demo.service.posts.PostsService;
import com.example.demo.web.dto.PostsResponseDto;
import com.example.demo.web.dto.PostsSaveRequestDto;
import com.example.demo.web.dto.PostsUpdateRequestDto;
import com.example.demo.config.auth.LoginUser;
import com.example.demo.config.auth.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostsApiController {
    private final PostsService postsService;

    @PostMapping("/api/v1/posts")
    public Long save(@RequestBody PostsSaveRequestDto requestDto, @LoginUser SessionUser user) {
        return postsService.save(requestDto, user);
    }

    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable("id") Long id, @RequestBody PostsUpdateRequestDto requestDto,
            @LoginUser SessionUser user) {
        return postsService.update(id, requestDto, user);
    }

    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDto findByid(@PathVariable("id") Long id) {
        return postsService.findById(id);
    }

    @DeleteMapping("/api/v1/posts/{id}")
    public Long delete(@PathVariable("id") Long id, @LoginUser SessionUser user) {
        postsService.delete(id, user);
        return id;
    }

    @GetMapping("/api/v1/posts/list")
    public java.util.List<com.example.demo.web.dto.PostsListResponseDto> findAll() {
        return postsService.findAllDesc();
    }

}
