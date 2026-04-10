package com.example.LeetcodeBox.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.LeetcodeBox.Entity.TagEntity;
import java.util.Optional;
import java.util.Set;


@Repository
//syntax JpaRepository<entity,primary datatype>
public interface TagRepository extends JpaRepository<TagEntity,Long>{
    Optional<TagEntity> findByName(String name);

    void deleteByName(String name);

    Set<TagEntity> findByNameIn(Set<String> names);
}
