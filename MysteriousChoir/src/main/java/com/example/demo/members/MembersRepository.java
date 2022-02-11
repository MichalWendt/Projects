package com.example.demo.members;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MembersRepository extends JpaRepository<Member, Long> {

    List<Member> findByOrderByLastNameAscFirstNameAsc();

}
