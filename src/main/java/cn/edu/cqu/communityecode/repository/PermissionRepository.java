package cn.edu.cqu.communityecode.repository;

import cn.edu.cqu.communityecode.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {
}
