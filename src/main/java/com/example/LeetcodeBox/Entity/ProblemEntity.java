package com.example.LeetcodeBox.Entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="problem")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProblemEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title cant be left empty")
    private String title;

    @Column(unique=true)
    @NotBlank(message = "Url can't left empty")
    private String url;

    @ManyToMany
    @JoinTable(name="problemtagjoin",
    joinColumns=@JoinColumn(name="problem_id"),
    inverseJoinColumns=@JoinColumn(name="tag_id"))
    private Set<TagEntity> tags;
}
