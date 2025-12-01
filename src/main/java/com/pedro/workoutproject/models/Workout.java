package com.pedro.workoutproject.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.pedro.workoutproject.dtos.workoutDtos.CreateWorkoutDto;
import com.pedro.workoutproject.dtos.workoutDtos.UpdateWorkoutDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Workout")
@Table(name = "workout")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@SQLRestriction(value = "is_active = true")
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String notes;
    private Boolean isActive = true;
    private LocalDateTime deleteOn;
    @CreationTimestamp
    private LocalDateTime createdOn;
    @UpdateTimestamp
    private LocalDateTime updateOn;
    private LocalDateTime startedOn;
    private LocalDateTime finishedOn;
    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonBackReference
    private User userId;
    @OneToMany(mappedBy = "workoutId")
    private List<WorkoutExercise> workoutExerciseList = new ArrayList<>();

    public Workout(CreateWorkoutDto createWorkoutDto, User user) {
        this.notes = createWorkoutDto.notes();
        this.startedOn = createWorkoutDto.startedOn();
        this.finishedOn = createWorkoutDto.finishedOn();
        this.userId = user;
        this.isActive = true;
    }

    public void update(UpdateWorkoutDto updateWorkoutDto) {
        if (!updateWorkoutDto.notes().isBlank()){
            setNotes(updateWorkoutDto.notes());
        }
        if (updateWorkoutDto.startedOn() != null){
            setUpdateOn(updateWorkoutDto.startedOn());
        }
        if (updateWorkoutDto.finishedOn() != null){
            setFinishedOn(updateWorkoutDto.finishedOn());
        }
    }

    public void disable() {
        this.setIsActive(false);
        this.setDeleteOn(LocalDateTime.now());
        this.getWorkoutExerciseList().forEach(WorkoutExercise::disable);
    }

}
