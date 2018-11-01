package com.test.mysql.repository;

import com.test.mysql.entity.Device;
import com.test.mysql.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    @Query("select t from device t where t.num like :num")
    Page<Device> findByNum(@Param("num") String num, Pageable pageRequest);


    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update device p set p.stat =?1 where p.id = ?2", nativeQuery = true)
    int updateStatById(String stat, Long id);



}
