package com.pedro.workoutproject.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.pedro.workoutproject.dtos.workoutExerciseDtos.CreateWorkoutExerciseDto;
import com.pedro.workoutproject.dtos.workoutExerciseDtos.UpdateWorkoutExerciseDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

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

    public WorkoutExercise(CreateWorkoutExerciseDto createWorkoutExerciseDto, Workout workout, Exercise exercise) {
        this.weight = createWorkoutExerciseDto.weight();
        this.sets = createWorkoutExerciseDto.sets();
        this.reps = createWorkoutExerciseDto.reps();
        this.notes = createWorkoutExerciseDto.notes();
        this.workoutId=workout;
        this.exerciseId = exercise;
    }

    public void update(UpdateWorkoutExerciseDto updateWorkoutExerciseDto) {

        if(updateWorkoutExerciseDto.reps() != null){
            setReps(updateWorkoutExerciseDto.reps());
        }


        if(updateWorkoutExerciseDto.sets() != null){
            setSets(updateWorkoutExerciseDto.sets());
        }

        if(updateWorkoutExerciseDto.weight() != null){
            setWeight(updateWorkoutExerciseDto.weight());
        }

        if(!updateWorkoutExerciseDto.notes().isBlank()){
            setNotes(updateWorkoutExerciseDto.notes());
        }

    }
}
