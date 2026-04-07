package com.example.LeetcodeBox.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.LeetcodeBox.Entity.UserEntity;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long>{
    Optional<UserEntity> findByName(String name);
}
