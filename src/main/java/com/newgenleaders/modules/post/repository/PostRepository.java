package com.newgenleaders.modules.post.repository;

import com.newgenleaders.modules.post.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostRepository extends JpaRepository<PostEntity, UUID> {
}
