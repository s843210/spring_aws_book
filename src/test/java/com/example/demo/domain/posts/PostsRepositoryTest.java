package com.example.demo.domain.posts;

import com.example.demo.domain.user.Role;
import com.example.demo.domain.user.User;
import com.example.demo.domain.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class PostsRepositoryTest {

        @Autowired
        PostsRepository postsRepository;

        @Autowired
        UserRepository userRepository;

        @AfterEach
        public void cleanup() {
                postsRepository.deleteAll();
                userRepository.deleteAll();
        }

        @Test
        public void 게시글저장_불러오기() {
                // given
                User user = userRepository.save(User.builder()
                                .name("ssss")
                                .email("ssss@example.com")
                                .picture("picture")
                                .role(Role.USER)
                                .build());

                String title = "테스트 게시글";
                String content = "테스트 본문";

                postsRepository.save(Posts.builder()
                                .title(title)
                                .content(content)
                                .author("ssss")
                                .user(user)
                                .build());

                // when
                List<Posts> postsList = postsRepository.findAll();

                // then
                Posts posts = postsList.get(0);
                assertThat(posts.getTitle()).isEqualTo(title);
                assertThat(posts.getContent()).isEqualTo(content);
                assertThat(posts.getAuthor()).isEqualTo("ssss");
        }

        @Test
        public void BaseTimeEntity_등록() {
                // given
                LocalDateTime now = LocalDateTime.of(2019, 6, 4, 0, 0, 0);
                User user = userRepository.save(User.builder()
                                .name("author")
                                .email("author@example.com")
                                .picture("picture")
                                .role(Role.USER)
                                .build());

                postsRepository.save(Posts.builder()
                                .title("title")
                                .content("content")
                                .author("author")
                                .user(user)
                                .build());

                // when
                List<Posts> postsList = postsRepository.findAll();

                // then
                Posts posts = postsList.get(0);

                System.out.println(
                                ">>>>>>>>>>> createDate=" + posts.getCreatedDate() + " ,modifiedDate= "
                                                + posts.getModifiedDate());

                assertThat(posts.getCreatedDate()).isAfter(now);
                assertThat(posts.getModifiedDate()).isAfter(now);
        }

}