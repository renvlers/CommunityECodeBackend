package cn.edu.cqu.communityecode.services;

import cn.edu.cqu.communityecode.entities.GuestRecord;
import cn.edu.cqu.communityecode.entities.GuestRequest;
import cn.edu.cqu.communityecode.repositories.EntranceRepository;
import cn.edu.cqu.communityecode.repositories.GuestRecordRepository;
import cn.edu.cqu.communityecode.repositories.GuestRequestRepository;
import cn.edu.cqu.communityecode.utils.HashUtil;
import cn.edu.cqu.communityecode.utils.Status;

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

    @Autowired
    private EntranceRepository entranceRepository;

    public String generateRequestCode() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder codeBuilder = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            codeBuilder.append(secureRandom.nextInt(10));
        }
        return codeBuilder.toString();
    }

    public String generateQrCode(String requestCode, LocalDateTime enterTime, LocalDateTime leaveTime, String guestName,
            String guestPhone, int ownerId) {
        String digest = requestCode +
                enterTime +
                leaveTime +
                guestName +
                guestPhone +
                ownerId;
        return HashUtil.sha256(digest);
    }

    public GuestRecord generateRecordByRequest(GuestRequest request, int entrance, int status) {
        GuestRecord guestRecord = new GuestRecord();
        guestRecord.setEnterTime(request.getEnterTime());
        guestRecord.setLeaveTime(request.getLeaveTime());
        guestRecord.setGuestName(request.getGuestName());
        guestRecord.setGuestPhone(request.getGuestPhone());
        guestRecord.setEntrance(entrance);
        guestRecord.setOwnerId(request.getOwnerId());
        guestRecord.setRequestCode(request.getRequestCode());
        guestRecord.setHash(request.getHash());
        guestRecord.setStatus(status);
        return guestRecord;
    }

    public void createNewRequest(GuestRequest guestRequest) throws Exception {
        List<GuestRequest> requests = guestRequestRepository.findGuestRequestByGuestPhone(guestRequest.getGuestPhone());
        if (!requests.isEmpty()) {
            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime leaveTime = requests.getLast().getLeaveTime();
            if (leaveTime.isAfter(currentTime) || leaveTime.isEqual(currentTime))
                throw new Exception("该访客有未使用的登记");
            else
                guestRequestRepository.delete(requests.getLast());
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

    public List<GuestRecord> getRecordsByGuestPhone(String phone) throws Exception {
        return guestRecordRepository.findGuestRecordsByGuestPhone(phone);
    }

    public List<GuestRecord> getRecordsByGuestName(String name) throws Exception {
        return guestRecordRepository.findGuestRecordsByGuestName(name);
    }

    public List<GuestRecord> getAllRecords() {
        return guestRecordRepository.findAll();
    }

    public GuestRequest getRequestByRequestCode(String requestCode) throws Exception {
        return guestRequestRepository.findGuestRequestByRequestCode(requestCode).getLast();
    }

    public GuestRequest checkIfRequestValidByPhone(String phone) throws Exception {
        List<GuestRequest> requests = guestRequestRepository.findGuestRequestByGuestPhone(phone);
        if (requests.isEmpty())
            throw new Exception("该访客目前没有进行登记");
        return requests.getLast();
    }

    public GuestRequest checkIfRequestValidByCode(String code) throws Exception {
        List<GuestRecord> records = guestRecordRepository.findGuestRecordByRequestCode(code);
        List<GuestRequest> requests = guestRequestRepository.findGuestRequestByRequestCode(code);
        if (requests.isEmpty() && records.isEmpty())
            throw new Exception("访问代码无效");
        else if (requests.isEmpty())
            throw new Exception("访问代码过期或已被使用");
        else
            return requests.getLast();
    }

    public GuestRequest checkIfRequestValidByQrCode(String hash) throws Exception {
        List<GuestRecord> records = guestRecordRepository.findGuestRecordByHash(hash);
        List<GuestRequest> requests = guestRequestRepository.findGuestRequestByHash(hash);
        if (requests.isEmpty() && records.isEmpty())
            throw new Exception("二维码无效");
        else if (requests.isEmpty())
            throw new Exception("二维码过期或已被使用");
        else
            return requests.getLast();
    }

    public void allowRequest(@NotNull GuestRequest request, int entrance) throws Exception {
        GuestRecord guestRecord = generateRecordByRequest(request, entrance, Status.ALLOWED);
        createNewRecord(guestRecord);
        guestRequestRepository.delete(request);
    }

    public void refuseRequest(GuestRequest request, int entrance) throws Exception {
        GuestRecord guestRecord = generateRecordByRequest(request, entrance, Status.REFUSED);
        createNewRecord(guestRecord);
        guestRequestRepository.delete(request);
    }

    public void withdrawRequest(GuestRequest request) throws Exception {
        GuestRecord guestRecord = generateRecordByRequest(request, 0, Status.WITHDRAWN);
        createNewRecord(guestRecord);
        guestRequestRepository.delete(request);
    }

    // 每隔1分钟执行一次
    @Scheduled(fixedRate = 60 * 1000) // 单位是毫秒
    public void removeExpiredRequests() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        List<GuestRequest> expiredGuests = guestRequestRepository.findGuestRequestsByLeaveTimeBefore(now);
        if (!expiredGuests.isEmpty()) {
            for (GuestRequest request : expiredGuests) {
                GuestRecord record = generateRecordByRequest(request, 0, Status.EXPIRED);
                createNewRecord(record);
            }
            guestRequestRepository.deleteAll(expiredGuests);
        }
    }

    public String getEntranceById(int id) throws NullPointerException {
        return entranceRepository.findEntranceById(id).getLast().getName();
    }
}
