package cn.edu.cqu.communityecode.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.edu.cqu.communityecode.entities.GuestRecord;

import java.util.List;

@Repository
public interface GuestRecordRepository extends JpaRepository<GuestRecord, Integer> {
    public List<GuestRecord> findGuestRecordsByOwnerId(int ownerId);

    public List<GuestRecord> findGuestRecordByRequestCode(String requestCode);

    public List<GuestRecord> findGuestRecordByHash(String hash);

    public List<GuestRecord> findGuestRecordsByGuestPhone(String phone);

    public List<GuestRecord> findGuestRecordsByGuestName(String name);
}
