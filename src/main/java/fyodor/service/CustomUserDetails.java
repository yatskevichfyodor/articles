package fyodor.service;

import fyodor.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {
    private User user;
    private Set<GrantedAuthority> grantedAuthorities;

    public CustomUserDetails() {
    }

    public CustomUserDetails(User user, Set<GrantedAuthority> grantedAuthorities) {
        this.user = user;
        this.grantedAuthorities = grantedAuthorities;
    }

    public CustomUserDetails(User user) {
        this.user = user;
        this.grantedAuthorities = user.getRoles().stream().map(it -> new SimpleGrantedAuthority(it.getName())).collect(Collectors.toSet());
    }

    public User getUser() {
        return user;
    }

    public long getId() {
        return user.getId();
    }

    public String getPassword() {
        return user.getPassword();
    }

    public String getUsername() {
        return user.getUsername();
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public Set<GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    public boolean isAccountNonLocked() {
        return !user.isBlocked();
    }

    public boolean isEnabled() {
        return user.isConfirmed();
    }
}
