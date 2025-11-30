package com.pedro.workoutproject.models;


import com.pedro.workoutproject.dtos.userDtos.CreateUserDto;
import com.pedro.workoutproject.enumerated.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity(name = "User")
@Table(name = "user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@SQLRestriction(value = "is_active = true")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Boolean isActive = true;
    private LocalDateTime deleteOn;
    @CreationTimestamp
    private LocalDateTime createdOn;
    @UpdateTimestamp
    private LocalDateTime updateOn;
    @OneToMany(mappedBy = "userId")
    private List<BodyWeight> weightList = new ArrayList<>();
    @OneToMany(mappedBy = "userId")
    private List<Workout> workoutList = new ArrayList<>();

    public User(String email, String encryptedPassword, Role role) {
        this.email = email;
        this.password = encryptedPassword;
        this.role = role;
        this.isActive = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(role == Role.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void updateUser(CreateUserDto createUserDto) {

        if(!createUserDto.email().isBlank()){
            setEmail(createUserDto.email());
        }

        if(!createUserDto.password().isBlank()){
            setPassword(createUserDto.password());
        }

    }

    public void disable() {
        this.setIsActive(false);
        this.setDeleteOn(LocalDateTime.now());
        this.getWeightList().forEach(BodyWeight::disable);
        this.getWorkoutList().forEach(Workout::disable);
    }


}
