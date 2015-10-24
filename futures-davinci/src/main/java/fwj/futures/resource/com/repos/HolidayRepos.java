package fwj.futures.resource.com.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import fwj.futures.resource.com.entity.Holiday;

@RepositoryRestResource(exported = false)
public interface HolidayRepos extends JpaRepository<Holiday, Integer> {

}
