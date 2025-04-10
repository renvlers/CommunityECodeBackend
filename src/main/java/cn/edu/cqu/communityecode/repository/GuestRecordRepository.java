package cn.edu.cqu.communityecode.repository;

import cn.edu.cqu.communityecode.entity.GuestRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestRecordRepository extends JpaRepository<GuestRecord, Integer> {
}
