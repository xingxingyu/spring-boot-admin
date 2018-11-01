package com.test.mysql.repository;

import com.test.mysql.entity.Resource;
import com.test.mysql.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    Page<Resource> findResourceByResourceNameLike(String resourceName, Pageable pageable);
    Resource findById(Long id);

}
