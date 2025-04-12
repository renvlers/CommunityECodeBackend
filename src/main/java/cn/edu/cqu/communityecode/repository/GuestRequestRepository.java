package cn.edu.cqu.communityecode.repository;

import cn.edu.cqu.communityecode.entity.GuestRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GuestRequestRepository extends JpaRepository<GuestRequest, String> {
    public List<GuestRequest> findGuestRequestByRequestCode(String requestCode);
    public List<GuestRequest> findGuestRequestByGuestPhone(String guestPhone);
    public List<GuestRequest> findGuestRequestByHash(String hash);
    public List<GuestRequest> findGuestRequestsByOwnerId(Integer ownerId);
    public List<GuestRequest> findGuestRequestsByLeaveTimeBefore(LocalDateTime time);
}
