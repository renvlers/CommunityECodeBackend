package cn.edu.cqu.communityecode.service;

import cn.edu.cqu.communityecode.entity.GuestRecord;
import cn.edu.cqu.communityecode.entity.GuestRequest;
import cn.edu.cqu.communityecode.repository.GuestRecordRepository;
import cn.edu.cqu.communityecode.repository.GuestRequestRepository;
import cn.edu.cqu.communityecode.util.HashUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class GuestService {
    @Autowired
    private GuestRequestRepository guestRequestRepository;

    @Autowired
    private GuestRecordRepository guestRecordRepository;

    public String generateRequestCode() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder codeBuilder = new StringBuilder();
        for(int i = 0; i < 6; i++) {
            codeBuilder.append(secureRandom.nextInt(10));
        }
        return codeBuilder.toString();
    }

    public String generateQrCode(String requestCode, LocalDateTime enterTime, LocalDateTime leaveTime, String guestName, String guestPhone, int ownerId) {
        String digest = requestCode +
                enterTime +
                leaveTime +
                guestName +
                guestPhone +
                ownerId;
        return HashUtil.sha256(digest);
    }

    public void createNewRequest(GuestRequest guestRequest) throws Exception {
        List<GuestRequest> requests = guestRequestRepository.findGuestRequestByGuestPhone(guestRequest.getGuestPhone());
        if(!requests.isEmpty()) {
            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime leaveTime = requests.getLast().getLeaveTime();
            if(leaveTime.isAfter(currentTime) || leaveTime.isEqual(currentTime)) throw new Exception("该访客有未使用的登记");
            else guestRequestRepository.delete(requests.getLast());
        }
        guestRequestRepository.save(guestRequest);
    }


    public void createNewRecord(GuestRecord guestRecord) throws Exception {
        guestRecordRepository.save(guestRecord);
    }

    public List<GuestRequest> getRequestsByOwnerId(int ownerId) throws Exception {
        return guestRequestRepository.findGuestRequestsByOwnerId(ownerId);
    }

    public List<GuestRecord> getRecordsByOwnerId(int ownerId) throws Exception {
        return guestRecordRepository.findGuestRecordsByOwnerId(ownerId);
    }

    public GuestRequest getRequestByRequestCode(String requestCode) throws Exception {
        return guestRequestRepository.findGuestRequestByRequestCode(requestCode).getLast();
    }

    public GuestRequest checkIfRequestValidByPhone(String phone) throws Exception {
        List<GuestRequest> requests = guestRequestRepository.findGuestRequestByGuestPhone(phone);
        if(requests.isEmpty()) throw new Exception("该访客没有进行登记");
        GuestRequest request = requests.getLast();
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime enterTime = request.getEnterTime();
        LocalDateTime leaveTime = request.getLeaveTime();

        boolean isWithinTimeRange =
                (currentTime.isAfter(enterTime) || currentTime.isEqual(enterTime)) &&
                        (currentTime.isBefore(leaveTime) || currentTime.isEqual(leaveTime));

        if(isWithinTimeRange) return request;
        else {
            deleteRequest(request);
            throw new Exception("该访客的登记已过期，已自动删除");
        }
    }

    public GuestRequest checkIfRequestValidByCode(String code) throws Exception {
        List<GuestRequest> requests = guestRequestRepository.findGuestRequestByRequestCode(code);
        if(requests.isEmpty()) throw new Exception("该访客没有进行登记");
        GuestRequest request = requests.getLast();
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime enterTime = request.getEnterTime();
        LocalDateTime leaveTime = request.getLeaveTime();

        boolean isWithinTimeRange =
                (currentTime.isAfter(enterTime) || currentTime.isEqual(enterTime)) &&
                        (currentTime.isBefore(leaveTime) || currentTime.isEqual(leaveTime));

        if(isWithinTimeRange) return request;
        else {
            deleteRequest(request);
            throw new Exception("该访客的登记已过期，已自动删除");
        }
    }

    public GuestRequest checkIfRequestValidByQrCode(String hash) throws Exception {
        List<GuestRequest> requests = guestRequestRepository.findGuestRequestByHash(hash);
        if(requests.isEmpty()) throw new Exception("该访客没有进行登记");
        GuestRequest request = requests.getLast();
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime enterTime = request.getEnterTime();
        LocalDateTime leaveTime = request.getLeaveTime();

        boolean isWithinTimeRange =
                (currentTime.isAfter(enterTime) || currentTime.isEqual(enterTime)) &&
                        (currentTime.isBefore(leaveTime) || currentTime.isEqual(leaveTime));

        if(isWithinTimeRange) return request;
        else {
            deleteRequest(request);
            throw new Exception("该访客的登记已过期，已自动删除");
        }
    }

    public void allowRequest(@NotNull GuestRequest request, int entrance) throws Exception {
        GuestRecord guestRecord = new GuestRecord();
        guestRecord.setEnterTime(request.getEnterTime());
        guestRecord.setLeaveTime(request.getLeaveTime());
        guestRecord.setGuestName(request.getGuestName());
        guestRecord.setGuestPhone(request.getGuestPhone());
        guestRecord.setEntrance(entrance);
        guestRecord.setOwnerId(request.getOwnerId());
        createNewRecord(guestRecord);
        guestRequestRepository.delete(request);
    }

    public void refuseRequest(GuestRequest request) throws Exception {
        guestRequestRepository.delete(request);
    }

    public void deleteRequest(GuestRequest request) throws Exception {
        guestRequestRepository.delete(request);
    }

    // 每隔1小时执行一次
    @Scheduled(fixedRate = 60 * 60 * 1000) // 单位是毫秒
    public void removeExpiredRequests() {
        LocalDateTime now = LocalDateTime.now();
        List<GuestRequest> expiredGuests = guestRequestRepository.findGuestRequestsByLeaveTimeBefore(now);
        if (!expiredGuests.isEmpty()) {
            guestRequestRepository.deleteAll(expiredGuests);
        }
    }
}
