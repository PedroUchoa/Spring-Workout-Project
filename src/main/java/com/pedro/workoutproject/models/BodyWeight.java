package com.pedro.workoutproject.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.pedro.workoutproject.dtos.bodyWeightDtos.UpdateBodyWeightDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity(name = "BodyWeight")
@Table(name = "body_weight")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class BodyWeight {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private Double value;
    @CreationTimestamp
    private LocalDateTime loggedOn;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    @JsonBackReference
    private User userId;

    public BodyWeight(Double  value, User user) {
        this.value = value;
        this.userId = user;
    }

    public void update(UpdateBodyWeightDto updateBodyWeightDto) {
        if(updateBodyWeightDto.value() != null){
            setValue(updateBodyWeightDto.value());
        }
    }
}
