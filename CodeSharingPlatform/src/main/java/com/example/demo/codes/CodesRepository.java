package com.example.demo.codes;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CodesRepository extends JpaRepository<Code, Long> {

    Code findCodeByUuid(UUID uuid);

    List<Code> findAll();

    List<Code> findAllByTimeEqualsAndViewsEquals(Long time, Long views);

}
