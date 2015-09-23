package fwj.futures.resource.repository.com;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import fwj.futures.resource.entity.com.Holiday;

@RepositoryRestResource(exported = false)
public interface HolidayRepository extends JpaRepository<Holiday, Integer> {

}
