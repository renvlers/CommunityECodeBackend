package cn.edu.cqu.communityecode.repository;

import cn.edu.cqu.communityecode.entity.GuestRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRecordRepository extends JpaRepository<GuestRecord, Integer> {
}
