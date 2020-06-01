package com.group.ncre_exam_platform.dao;

import com.group.ncre_exam_platform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("select s.user_id from User s where s.name = ?1")
    Integer getUserIdByName(String name);

    @Query("select s.password from User s where s.name = ?1")
    String getPasswordByName(String name);

    @Query("select s.user_id from User s where s.email = ?1")
    Integer getUserIdByEmail(String email);

    @Transactional
    @Modifying
    @Query("update User u set u.password = :password where u.email = :email")
    void savePassword(@Param("email") String email, @Param("password") String password);
}
