package fwj.futures.resource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import fwj.futures.resource.entity.Label;

@RepositoryRestResource(exported = false)
public interface LabelRepository extends JpaRepository<Label, Integer> {

	Label findByName(String name);
}
