package com.group.auto_generating_exam.dao;

import com.group.auto_generating_exam.model.Exam;
import com.group.auto_generating_exam.model.Train;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface TrainRepository extends JpaRepository<Train, Integer> {
    @Query("select u.train_id from Train u where u.sub_id = ?1")
    List<Integer> getTrainIdBySubId(String sub_id);

    @Query("select max(u.train_id) from Train u")
    Integer getMaxTrainId();

    @Transactional
    @Modifying
    @Query("update Train s set s.last_time = :last_time, s.user_id = :user_id, s.sub_id = :sub_id where s.train_id = :train_id")
    void updateUserIdSubIdAndTrainTimeByTrainId(@Param("user_id") Integer user_id, @Param("sub_id") String sub_id, @Param("last_time") Long last_time, @Param("train_id") Integer train_id);

    @Query("select u from Train u where u.user_id = ?1")
    List<Train> getTrainByUserId(Integer user_id);

    @Query("select s.train_id from Train s where s.train_id = ?1")
    Integer isTrainExist(Integer train_Id);

    @Query("select s.sub_id from Train s where s.train_id = ?1")
    String getSubIdByTrainId(Integer train_id);

    @Query("select s.begin_time from Train s where s.train_id = ?1")
    Long getBeginTimeByTrainId(Integer train_id);

    @Query("select s.last_time from Train s where s.train_id = ?1")
    Long getLastTimeByTrainId(Integer train_id);

    @Query("select u from Train u where u.train_id = ?1")
    Train getTrainByTrainId(Integer train_id);
}
