package com.example.LeetcodeBox.Entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass // says this class is upper class and field in this class will be extends to its sub classes
//this is not an entity, its just a class (springboot)
public class BaseEntity {
    private final LocalDateTime createdAt=LocalDateTime.now();

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
