package com.pedro.workoutproject.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "WorkoutExercise")
@Table(name = "workout_exercise")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class WorkoutExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private Integer weight;
    private Integer sets;
    private Integer reps;
    private String notes;
    @CreationTimestamp
    private LocalDateTime createdOn;
    private LocalDateTime updateOn;
    @ManyToOne
    @JoinColumn(name = "workoutId")
    @JsonBackReference
    private Workout workoutId;
    @ManyToOne
    @JoinColumn(name = "exerciseId")
    @JsonBackReference
    private Exercise exerciseId;

}
