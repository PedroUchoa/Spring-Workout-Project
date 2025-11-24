package com.pedro.workoutproject.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "Exercises")
@Table(name = "exercises")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String type;
    @OneToMany(mappedBy = "exerciseId")
    List<WorkoutExercise> workoutExerciseList;
}
