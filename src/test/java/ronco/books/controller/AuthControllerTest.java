package ronco.books.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ronco.books.config.TestSecurityConfig;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ronco.books.model.User;
import ronco.books.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
@Import(TestSecurityConfig.class)
public class AuthControllerTest {

    // Create a test DTO class to avoid serialization issues with User's authorities
    @JsonIgnoreProperties({"authorities", "accountNonExpired", "accountNonLocked", "credentialsNonExpired", "enabled"})
    public static class UserDTO {
        private Long id;
        private String username;
        private String password;
        private String role;

        public UserDTO() {}

        public UserDTO(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public UserDTO(String username, String password, String role) {
            this.username = username;
            this.password = password;
            this.role = role;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Captor
    private ArgumentCaptor<UsernamePasswordAuthenticationToken> authTokenCaptor;

    @Test
    @DisplayName("POST /api/auth/register - When registering a new user, should return 201 Created")
    void registerUser_whenNewUser_shouldReturnCreated() throws Exception {
        // Arrange
        UserDTO userToRegister = new UserDTO("newuser", "password123");

        User savedUser = User.builder()
                .id(1L)
                .username("newuser")
                .password("encoded_password")
                .role("ROLE_USER")
                .build();

        when(userService.existsByUsername("newuser")).thenReturn(false);
        when(userService.save(any(User.class))).thenReturn(savedUser);

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToRegister)))
                .andExpect(status().isCreated())
                .andExpect(content().string("User registered successfully!"));

        verify(userService).existsByUsername("newuser");
        verify(userService).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();
        assertThat(capturedUser.getUsername()).isEqualTo("newuser");
        assertThat(capturedUser.getPassword()).isEqualTo("password123");
        assertThat(capturedUser.getRole()).isEqualTo("ROLE_USER"); // Default role should be set
    }

    @Test
    @DisplayName("POST /api/auth/register - When username is taken, should return 400 Bad Request")
    void registerUser_whenUsernameExists_shouldReturnBadRequest() throws Exception {
        // Arrange
        UserDTO userToRegister = new UserDTO("existinguser", "password123");

        when(userService.existsByUsername("existinguser")).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToRegister)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username is already taken!"));

        verify(userService).existsByUsername("existinguser");
        verify(userService, never()).save(any(User.class));
    }

    @Test
    @DisplayName("POST /api/auth/register - When role is provided, should use that role")
    void registerUser_whenRoleProvided_shouldUseProvidedRole() throws Exception {
        // Arrange
        UserDTO userToRegister = new UserDTO("adminuser", "password123", "ROLE_ADMIN");

        User savedUser = User.builder()
                .id(1L)
                .username("adminuser")
                .password("encoded_password")
                .role("ROLE_ADMIN")
                .build();

        when(userService.existsByUsername("adminuser")).thenReturn(false);
        when(userService.save(any(User.class))).thenReturn(savedUser);

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToRegister)))
                .andExpect(status().isCreated());

        verify(userService).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        assertThat(capturedUser.getRole()).isEqualTo("ROLE_ADMIN");
    }

    @Test
    @DisplayName("POST /api/auth/login - When credentials are valid, should return 200 OK")
    void login_whenCredentialsValid_shouldReturnOk() throws Exception {
        // Arrange
        UserDTO loginRequest = new UserDTO("testuser", "password123");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User logged in successfully!"));

        verify(authenticationManager).authenticate(authTokenCaptor.capture());
        UsernamePasswordAuthenticationToken capturedToken = authTokenCaptor.getValue();
        assertThat(capturedToken.getPrincipal()).isEqualTo("testuser");
        assertThat(capturedToken.getCredentials()).isEqualTo("password123");
    }
}
