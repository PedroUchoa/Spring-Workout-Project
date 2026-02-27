package com.pedro.workoutproject.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.pedro.workoutproject.dtos.bodyWeightDtos.UpdateBodyWeightDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity(name = "BodyWeight")
@Table(name = "body_weight")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@SQLRestriction(value = "is_active = true")
public class BodyWeight {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private Double value;
    private LocalDateTime loggedOn;
    private Boolean isActive;
    private LocalDateTime deleteOn;
    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonBackReference
    private User userId;

    public BodyWeight(Double value, User user, LocalDateTime loggedOn) {
        this.value = value;
        this.userId = user;
        this.loggedOn = loggedOn;
        this.isActive = true;
    }

    public void update(UpdateBodyWeightDto updateBodyWeightDto) {
        setValue(updateBodyWeightDto.value());

    }

    public void disable() {
        this.setIsActive(false);
        this.setDeleteOn(LocalDateTime.now());
    }
}
