package com.example.musicStore.model;

import com.example.musicStore.config.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Public.class)
    private Long id;

    @JsonView(Views.Public.class)
    private String username;

    @JsonView(Views.Internal.class)
    private String password;

    @JsonView(Views.Public.class)
    private String email;

    @JsonView(Views.Public.class)
    private String role;

    @Override
    @JsonView(Views.Internal.class)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> role);
    }

    @Override
    @JsonView(Views.Internal.class)
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonView(Views.Internal.class)
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonView(Views.Internal.class)
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonView(Views.Internal.class)
    public boolean isEnabled() {
        return true;
    }
}