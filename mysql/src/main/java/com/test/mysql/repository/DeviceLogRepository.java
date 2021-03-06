package com.test.mysql.repository;

import com.test.mysql.entity.Department;
import com.test.mysql.entity.Devicelog;
import com.test.mysql.entity.User;
import com.test.mysql.entity.Userlog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface DeviceLogRepository extends JpaRepository<Devicelog, Long> {
    @Query("select t from Devicelog t where  t.createdate >= ?1 and  t.createdate <= ?2  ")
    Page<Devicelog> findByDeviceIdAndDate(Date start, Date end, Pageable pageRequest);

}
