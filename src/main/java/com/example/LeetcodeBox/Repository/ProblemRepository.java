package com.example.LeetcodeBox.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.LeetcodeBox.Entity.ProblemEntity;
import java.util.Set;
import com.example.LeetcodeBox.Entity.TagEntity;




@Repository
public interface ProblemRepository extends JpaRepository<ProblemEntity,Long>{
    Optional<ProblemEntity> findByTitle(String title);

    Optional<ProblemEntity> findByUrl(String url);

    Set<ProblemEntity> findByTagsIn(Set<TagEntity> tags);
} 
