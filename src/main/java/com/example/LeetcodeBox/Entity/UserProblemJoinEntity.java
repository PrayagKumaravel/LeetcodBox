package com.example.LeetcodeBox.Entity;

import com.example.LeetcodeBox.Enum.SolvingStatusEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    private Long user_id;

    private Long problem_id;

    private String notes;

    @Enumerated(EnumType.STRING)
    private SolvingStatusEnum status;

    private Long frequency;

    private Long average_time;
}
