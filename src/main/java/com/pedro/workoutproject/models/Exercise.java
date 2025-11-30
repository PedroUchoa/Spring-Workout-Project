package com.pedro.workoutproject.models;

import com.pedro.workoutproject.dtos.exerciseDtos.CreateExerciseDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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
    private Boolean isActive = true;
    private LocalDateTime deleteOn;

    public Exercise(CreateExerciseDto createExerciseDto) {
        this.name = createExerciseDto.name();
        this.type = createExerciseDto.type();
        this.isActive = true;
    }

    public void update(CreateExerciseDto createExerciseDto) {
        if(!createExerciseDto.name().isBlank()){
            setName(createExerciseDto.name());
        }
        if(!createExerciseDto.type().isBlank()){
            setType(createExerciseDto.type());
        }
    }

    public void disable() {
        this.setIsActive(false);
        this.setDeleteOn(LocalDateTime.now());
    }

}
