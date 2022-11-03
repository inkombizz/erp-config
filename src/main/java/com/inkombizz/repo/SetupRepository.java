package com.inkombizz.repo;


import com.inkombizz.entity.model.Setup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SetupRepository extends CrudRepository<Setup, String> {

  Setup findByCode(String code);
}
