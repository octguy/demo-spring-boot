package octguy.demospringboot.service;

import octguy.demospringboot.model.User;
import octguy.demospringboot.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CustomUserDetailsService Tests")
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    private User adminUser;
    private User regularUser;
    private User disabledUser;

    @BeforeEach
    void setUp() {
        adminUser = User.builder()
                .id(1L)
                .email("admin@example.com")
                .password("admin123")
                .role("ADMIN")
                .enabled(true)
                .build();

        regularUser = User.builder()
                .id(2L)
                .email("user@example.com")
                .password("user123")
                .role("USER")
                .enabled(true)
                .build();

        disabledUser = User.builder()
                .id(3L)
                .email("disabled@example.com")
                .password("disabled123")
                .role("USER")
                .enabled(false)
                .build();
    }

    @Test
    @DisplayName("Should load admin user by email successfully")
    void shouldLoadAdminUserSuccessfully() {
        // Given
        when(userRepository.findByEmail("admin@example.com"))
                .thenReturn(Optional.of(adminUser));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("admin@example.com");

        // Then
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("admin@example.com");
        assertThat(userDetails.getPassword()).isEqualTo("admin123");
        assertThat(userDetails.getAuthorities())
                .extracting("authority")
                .contains("ROLE_ADMIN");
        assertThat(userDetails.isEnabled()).isTrue();
        verify(userRepository, times(1)).findByEmail("admin@example.com");
    }

    @Test
    @DisplayName("Should load regular user by email successfully")
    void shouldLoadRegularUserSuccessfully() {
        // Given
        when(userRepository.findByEmail("user@example.com"))
                .thenReturn(Optional.of(regularUser));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("user@example.com");

        // Then
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("user@example.com");
        assertThat(userDetails.getPassword()).isEqualTo("user123");
        assertThat(userDetails.getAuthorities())
                .extracting("authority")
                .contains("ROLE_USER");
        assertThat(userDetails.isEnabled()).isTrue();
        verify(userRepository, times(1)).findByEmail("user@example.com");
    }

    @Test
    @DisplayName("Should load disabled user correctly")
    void shouldLoadDisabledUserCorrectly() {
        // Given
        when(userRepository.findByEmail("disabled@example.com"))
                .thenReturn(Optional.of(disabledUser));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("disabled@example.com");

        // Then
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("disabled@example.com");
        assertThat(userDetails.isEnabled()).isFalse();
        verify(userRepository, times(1)).findByEmail("disabled@example.com");
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when user not found")
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        when(userRepository.findByEmail("nonexistent@example.com"))
                .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("nonexistent@example.com"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found with email: nonexistent@example.com");
        verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
    }

    @Test
    @DisplayName("Should handle null email gracefully")
    void shouldHandleNullEmailGracefully() {
        // Given
        when(userRepository.findByEmail(null))
                .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(null))
                .isInstanceOf(UsernameNotFoundException.class);
        verify(userRepository, times(1)).findByEmail(null);
    }

    @Test
    @DisplayName("Should handle empty email gracefully")
    void shouldHandleEmptyEmailGracefully() {
        // Given
        when(userRepository.findByEmail(""))
                .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(""))
                .isInstanceOf(UsernameNotFoundException.class);
        verify(userRepository, times(1)).findByEmail("");
    }

    @Test
    @DisplayName("Should correctly format role with ROLE_ prefix")
    void shouldCorrectlyFormatRolePrefix() {
        // Given
        when(userRepository.findByEmail("admin@example.com"))
                .thenReturn(Optional.of(adminUser));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("admin@example.com");

        // Then
        assertThat(userDetails.getAuthorities()).hasSize(1);
        assertThat(userDetails.getAuthorities().iterator().next().getAuthority())
                .isEqualTo("ROLE_ADMIN");
        verify(userRepository, times(1)).findByEmail("admin@example.com");
    }

    @Test
    @DisplayName("Should return user with exactly one authority")
    void shouldReturnUserWithOneAuthority() {
        // Given
        when(userRepository.findByEmail("user@example.com"))
                .thenReturn(Optional.of(regularUser));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("user@example.com");

        // Then
        assertThat(userDetails.getAuthorities()).hasSize(1);
        verify(userRepository, times(1)).findByEmail("user@example.com");
    }

    @Test
    @DisplayName("Should handle case-sensitive email lookup")
    void shouldHandleCaseSensitiveEmailLookup() {
        // Given
        when(userRepository.findByEmail("Admin@Example.com"))
                .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("Admin@Example.com"))
                .isInstanceOf(UsernameNotFoundException.class);
        verify(userRepository, times(1)).findByEmail("Admin@Example.com");
    }

    @Test
    @DisplayName("Should preserve password in UserDetails")
    void shouldPreservePasswordInUserDetails() {
        // Given
        when(userRepository.findByEmail("admin@example.com"))
                .thenReturn(Optional.of(adminUser));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("admin@example.com");

        // Then
        assertThat(userDetails.getPassword()).isEqualTo("admin123");
        verify(userRepository, times(1)).findByEmail("admin@example.com");
    }

    @Test
    @DisplayName("Should set account as non-expired, non-locked, and credentials non-expired")
    void shouldSetAccountStatusCorrectly() {
        // Given
        when(userRepository.findByEmail("user@example.com"))
                .thenReturn(Optional.of(regularUser));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("user@example.com");

        // Then
        assertThat(userDetails.isAccountNonExpired()).isTrue();
        assertThat(userDetails.isAccountNonLocked()).isTrue();
        assertThat(userDetails.isCredentialsNonExpired()).isTrue();
        verify(userRepository, times(1)).findByEmail("user@example.com");
    }
}
