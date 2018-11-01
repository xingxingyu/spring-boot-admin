package com.test.mysql.repository;

import com.test.mysql.entity.Authority;
import com.test.mysql.entity.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Page<Authority> findByAuthorityNameLike(String authorityName, Pageable pageable);

}
