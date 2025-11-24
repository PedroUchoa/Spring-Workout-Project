package com.pedro.workoutproject.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "Workout")
@Table(name = "workout")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String notes;
    @CreationTimestamp
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private LocalDateTime startedOn;
    private LocalDateTime finishedOn;
    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonBackReference
    private User userId;
    @OneToMany(mappedBy = "workoutId")
    private List<WorkoutExercise> workoutExerciseList;
}
