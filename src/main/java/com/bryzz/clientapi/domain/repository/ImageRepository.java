package com.bryzz.clientapi.domain.repository;

import com.bryzz.clientapi.domain.model.DockerImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<DockerImage, Long> {
}
