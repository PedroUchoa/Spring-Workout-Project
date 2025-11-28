package com.pedro.workoutproject.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.pedro.workoutproject.dtos.workoutDtos.CreateWorkoutDto;
import com.pedro.workoutproject.dtos.workoutDtos.UpdateWorkoutDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
    @UpdateTimestamp
    private LocalDateTime updatedOn;
    private LocalDateTime startedOn;
    private LocalDateTime finishedOn;
    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonBackReference
    private User userId;
    @OneToMany(mappedBy = "workoutId")
    private List<WorkoutExercise> workoutExerciseList;

    public Workout(CreateWorkoutDto createWorkoutDto, User user) {
        this.notes = createWorkoutDto.notes();
        this.startedOn = createWorkoutDto.startedOn();
        this.finishedOn = createWorkoutDto.finishedOn();
        this.userId = user;
    }

    public void update(UpdateWorkoutDto updateWorkoutDto) {
        if (!updateWorkoutDto.notes().isBlank()){
            setNotes(updateWorkoutDto.notes());
        }
        if (updateWorkoutDto.startedOn() != null){
            setUpdatedOn(updateWorkoutDto.startedOn());
        }
        if (updateWorkoutDto.finishedOn() != null){
            setFinishedOn(updateWorkoutDto.finishedOn());
        }
    }
}
