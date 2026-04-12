package com.example.LeetcodeBox.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.LeetcodeBox.Entity.UserProblemJoinEntity;

import java.util.List;

import com.example.LeetcodeBox.Entity.UserEntity;
import com.example.LeetcodeBox.Entity.ProblemEntity;


@Repository
public interface UserProblemJoinRepository extends JpaRepository<UserProblemJoinEntity,Long>{
    List<UserProblemJoinEntity> findByProblemAndUser(ProblemEntity problem, UserEntity user);

    List<UserProblemJoinEntity> findByUser(UserEntity user);
}
