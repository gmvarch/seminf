package com.gmv.techhip.repository;

import com.gmv.techhip.domain.Vote;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Vote entity.
 */
@SuppressWarnings("unused")
public interface VoteRepository extends JpaRepository<Vote,Long> {

}
