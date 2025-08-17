package com.jup.jupging.domain.plogging.repository;

import com.jup.jupging.domain.plogging.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
