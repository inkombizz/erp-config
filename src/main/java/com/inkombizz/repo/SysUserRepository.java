package com.inkombizz.repo;

import com.inkombizz.entity.model.User;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysUserRepository extends CrudRepository<User, String> {

  @Query(value = "SELECT t.* FROM scr_user t WHERE t.Username = :username ", nativeQuery = true)
  User findByUsername(String username);
}
