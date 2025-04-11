package cn.edu.cqu.communityecode.service;

import cn.edu.cqu.communityecode.entity.GuestRecord;
import cn.edu.cqu.communityecode.entity.GuestRequest;
import cn.edu.cqu.communityecode.repository.GuestRecordRepository;
import cn.edu.cqu.communityecode.repository.GuestRequestRepository;
import cn.edu.cqu.communityecode.util.HashUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
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

    public void createNewRequest(GuestRequest guestRequest) {
        try {
            List<GuestRequest> requests = guestRequestRepository.findGuestRequestByGuestPhone(guestRequest.getGuestPhone());
            if(!requests.isEmpty()) throw new Exception("该访客有未使用的登记");
            guestRequestRepository.save(guestRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createNewRecord(GuestRecord guestRecord) {
        try {
            guestRecordRepository.save(guestRecord);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GuestRequest checkIfRequestValidByPhone(String phone) {
        List<GuestRequest> requests = guestRequestRepository.findGuestRequestByGuestPhone(phone);
        if(requests.isEmpty()) return null;
        GuestRequest request = requests.getLast();
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime enterTime = request.getEnterTime();
        LocalDateTime leaveTime = request.getLeaveTime();

        boolean isWithinTimeRange =
                (currentTime.isAfter(enterTime) || currentTime.isEqual(enterTime)) &&
                        (currentTime.isBefore(leaveTime) || currentTime.isEqual(leaveTime));

        if(isWithinTimeRange) return request;
        else {
            refuseRequest(request);
            return null;
        }
    }

    public GuestRequest checkIfRequestValidByCode(String code) {
        List<GuestRequest> requests = guestRequestRepository.findGuestRequestByRequestCode(code);
        if(requests.isEmpty()) return null;
        GuestRequest request = requests.getLast();
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime enterTime = request.getEnterTime();
        LocalDateTime leaveTime = request.getLeaveTime();

        boolean isWithinTimeRange =
                (currentTime.isAfter(enterTime) || currentTime.isEqual(enterTime)) &&
                        (currentTime.isBefore(leaveTime) || currentTime.isEqual(leaveTime));

        if(isWithinTimeRange) return request;
        else {
            refuseRequest(request);
            return null;
        }
    }

    public GuestRequest checkIfRequestValidByQrCode(String hash) {
        List<GuestRequest> requests = guestRequestRepository.findGuestRequestByHash(hash);
        if(requests.isEmpty()) return null;
        GuestRequest request = requests.getLast();
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime enterTime = request.getEnterTime();
        LocalDateTime leaveTime = request.getLeaveTime();

        boolean isWithinTimeRange =
                (currentTime.isAfter(enterTime) || currentTime.isEqual(enterTime)) &&
                        (currentTime.isBefore(leaveTime) || currentTime.isEqual(leaveTime));

        if(isWithinTimeRange) return request;
        else {
            refuseRequest(request);
            return null;
        }
    }

    public void allowRequest(@NotNull GuestRequest request, int entrance) {
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

    public void refuseRequest(GuestRequest request) {
        guestRequestRepository.delete(request);
    }
}
