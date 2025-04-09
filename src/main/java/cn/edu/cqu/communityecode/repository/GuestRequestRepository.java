package cn.edu.cqu.communityecode.repository;

import cn.edu.cqu.communityecode.entity.GuestRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRequestRepository extends JpaRepository<GuestRequest, String> {
}
