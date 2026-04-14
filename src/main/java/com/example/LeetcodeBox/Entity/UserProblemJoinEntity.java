package com.example.LeetcodeBox.Entity;

import com.example.LeetcodeBox.Enum.SolvingStatusEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="userproblemjoin")
@Builder
public class UserProblemJoinEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    //many rows of userproblem can have one user
    @ManyToOne
    private UserEntity user;

    //many rows of userproblem can have one problem
    @ManyToOne
    private ProblemEntity problem;


    @Column(columnDefinition = "TEXT",length = 5000) //says db to treat it as text(larger content)
    private String notes;

    @Enumerated(EnumType.STRING)
    private SolvingStatusEnum status;

    private Long frequency;

    private Long solved_frequency;

    private Long average_time;
}
