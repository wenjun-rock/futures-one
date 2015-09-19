package fwj.futures.resource.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fwj.futures.resource.entity.Label;

public interface LabelRepository extends JpaRepository<Label, Integer> {

	Label findByName(String name);
}
