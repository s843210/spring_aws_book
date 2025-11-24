package com.example.demo.web;

import com.example.demo.domain.user.UserRepository;
import com.example.demo.domain.posts.Posts;
import com.example.demo.domain.posts.PostsRepository;
import com.example.demo.web.dto.PostsSaveRequestDto;
import com.example.demo.web.dto.PostsUpdateRequestDto;
import com.example.demo.config.auth.dto.SessionUser;
import com.example.demo.domain.user.User;
import com.example.demo.domain.user.Role;
import org.springframework.mock.web.MockHttpSession;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@org.springframework.transaction.annotation.Transactional
class PostsApiControllerTest {

        @Autowired
        private WebApplicationContext context;

        private MockMvc mvc;

        @BeforeEach
        public void setup() {
                mvc = MockMvcBuilders
                                .webAppContextSetup(context)
                                .apply(springSecurity())
                                .build();
        }

        @Autowired
        private PostsRepository postsRepository;

        @AfterEach
        public void tearDown() throws Exception {
                postsRepository.deleteAll();
                userRepository.deleteAll();
        }

        @Autowired
        private UserRepository userRepository;

        @Test
        @WithMockUser(roles = "USER")
        public void Posts_등록된다() throws Exception {
                // given
                User user = userRepository.save(User.builder()
                                .name("author")
                                .email("author@example.com")
                                .picture("picture")
                                .role(Role.USER)
                                .build());

                String title = "title";
                String content = "content";
                PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                                .title(title)
                                .content(content)
                                .author("author")
                                .build();

                String url = "http://localhost:8080/api/v1/posts";

                MockHttpSession session = new MockHttpSession();
                session.setAttribute("user", new SessionUser(user));

                // when
                mvc.perform(post(url)
                                .session(session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(requestDto)))
                                .andExpect(status().isOk());

                // then
                List<Posts> all = postsRepository.findAll();
                assertThat(all.get(0).getTitle()).isEqualTo(title);
                assertThat(all.get(0).getContent()).isEqualTo(content);
                assertThat(all.get(0).getAuthor()).isEqualTo("author");
        }

        @Test
        @WithMockUser(roles = "USER")
        public void Posts_수정된다() throws Exception {
                // given
                User user = userRepository.save(User.builder()
                                .name("author")
                                .email("author@example.com")
                                .picture("picture")
                                .role(Role.USER)
                                .build());

                Posts savedPosts = postsRepository.save(Posts.builder()
                                .title("title")
                                .content("content")
                                .author("author")
                                .user(user)
                                .build());

                Long updateId = savedPosts.getId();
                String expectedTitle = "title2";
                String expectedContent = "content2";

                PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                                .title(expectedTitle)
                                .content(expectedContent)
                                .build();

                String url = "http://localhost:8080/api/v1/posts/" + updateId;

                MockHttpSession session = new MockHttpSession();
                session.setAttribute("user", new SessionUser(user));

                // when
                mvc.perform(put(url)
                                .session(session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(requestDto)))
                                .andExpect(status().isOk());

                // then
                List<Posts> all = postsRepository.findAll();
                assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
                assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
        }
}