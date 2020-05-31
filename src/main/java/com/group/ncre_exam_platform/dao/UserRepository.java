package com.group.ncre_exam_platform.dao;

import com.group.ncre_exam_platform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("select s.user_id from User s where s.name = ?1")
    Integer getUserIdByName(String name);

    @Query("select s.password from User s where s.name = ?1")
    String getPasswordByName(String name);
}
