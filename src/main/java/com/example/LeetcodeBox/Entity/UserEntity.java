package com.example.LeetcodeBox.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="user")
@NoArgsConstructor// creates constructro with no arg
@AllArgsConstructor// creates constructor with args
@Getter
@Setter
@Builder
public class UserEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message="UserName can't be empty")
    @Column(unique=true)//since leetcode user id
    private String name;

    @NotBlank(message="Email can't be empty")
    @Column(unique=true)
    private String mailId;

    @NotBlank(message="Password can't be empty")
    private String password;
}