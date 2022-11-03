package com.inkombizz.repo;

import com.inkombizz.entity.model.RoleAuthorization;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysRoleAuthorizationRepository extends CrudRepository<RoleAuthorization, String> {
  
    RoleAuthorization findByHeaderCodeAndAuthorizationCode(String headeCode, String authCode);

}
