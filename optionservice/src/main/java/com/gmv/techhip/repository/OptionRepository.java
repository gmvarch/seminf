package com.gmv.techhip.repository;

import com.gmv.techhip.domain.Option;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Option entity.
 */
@SuppressWarnings("unused")
public interface OptionRepository extends JpaRepository<Option,Long> {

}
