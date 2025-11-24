package com.example.demo.service.posts;

import com.example.demo.domain.posts.Posts;
import com.example.demo.domain.posts.PostsRepository;
import com.example.demo.domain.user.User;
import com.example.demo.domain.user.UserRepository;
import com.example.demo.web.dto.PostsListResponseDto;
import com.example.demo.web.dto.PostsResponseDto;
import com.example.demo.web.dto.PostsSaveRequestDto;
import com.example.demo.web.dto.PostsUpdateRequestDto;
import com.example.demo.config.auth.dto.SessionUser;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto, SessionUser user) {
        User userEntity = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. email=" + user.getEmail()));

        return postsRepository.save(requestDto.toEntity(userEntity)).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto, SessionUser user) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        if (!posts.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "작성자만 수정할 수 있습니다.");
        }

        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시슬이 없습니다. id=" + id));

        return new PostsResponseDto(entity);
    }

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream().map(PostsListResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id, SessionUser user) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        if (!posts.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "작성자만 삭제할 수 있습니다.");
        }

        postsRepository.delete(posts);
    }

}
