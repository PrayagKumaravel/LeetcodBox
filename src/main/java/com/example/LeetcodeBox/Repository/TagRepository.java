package com.example.LeetcodeBox.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.LeetcodeBox.Entity.TagEntity;

@Repository
//syntax JpaRepository<entity,primary datatype>
public interface TagRepository extends JpaRepository<TagEntity,Long>{
}
