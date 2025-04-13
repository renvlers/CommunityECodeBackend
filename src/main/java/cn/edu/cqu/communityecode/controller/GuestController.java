package cn.edu.cqu.communityecode.controller;

import cn.edu.cqu.communityecode.dto.*;
import cn.edu.cqu.communityecode.entity.GuestRecord;
import cn.edu.cqu.communityecode.entity.GuestRequest;
import cn.edu.cqu.communityecode.service.GuestService;
import cn.edu.cqu.communityecode.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Vector;

@RestController
@RequestMapping("/guest")
public class GuestController {
    @Autowired
    private GuestService guestService;
    @PostMapping("/create_request")
    public Response<RequestDto> createRequest(@RequestBody CreateRequestRequestDto createRequestRequestDto) {
        try {
            String requestCode = guestService.generateRequestCode();
            LocalDateTime enterTime = createRequestRequestDto.getEnterTime();
            LocalDateTime leaveTime = createRequestRequestDto.getLeaveTime();
            String guestName = createRequestRequestDto.getGuestName();
            String guestPhone = createRequestRequestDto.getGuestPhone();
            int ownerId = createRequestRequestDto.getOwnerId();
            String hash = guestService.generateQrCode(requestCode, enterTime, leaveTime, guestName, guestPhone, ownerId);
            GuestRequest guestRequest = new GuestRequest();
            guestRequest.setRequestCode(requestCode);
            guestRequest.setEnterTime(enterTime);
            guestRequest.setLeaveTime(leaveTime);
            guestRequest.setGuestName(guestName);
            guestRequest.setGuestPhone(guestPhone);
            guestRequest.setOwnerId(ownerId);
            guestRequest.setHash(hash);
            guestService.createNewRequest(guestRequest);
            return new Response<>("访客登记成功", new RequestDto(
                    requestCode,
                    enterTime,
                    leaveTime,
                    guestName,
                    guestPhone,
                    ownerId,
                    hash
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(e.getMessage(), null);
        }
    }

    @GetMapping("/check_request_by_code")
    public Response<RequestDto> checkRequestByCode(@RequestParam String code) {
        try {
            GuestRequest guestRequest = guestService.checkIfRequestValidByCode(code);
            String requestCode = guestRequest.getRequestCode();
            LocalDateTime enterTime = guestRequest.getEnterTime();
            LocalDateTime leaveTime = guestRequest.getLeaveTime();
            String guestName = guestRequest.getGuestName();
            String guestPhone = guestRequest.getGuestPhone();
            int ownerId = guestRequest.getOwnerId();
            String hash = guestRequest.getHash();
            return new Response<>("登记查找成功", new RequestDto(
                    requestCode,
                    enterTime,
                    leaveTime,
                    guestName,
                    guestPhone,
                    ownerId,
                    hash
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(e.getMessage(), null);
        }
    }

    @GetMapping("/check_request_by_phone")
    public Response<RequestDto> checkRequestByPhone(@RequestParam String phone) {
        try {
            GuestRequest guestRequest = guestService.checkIfRequestValidByPhone(phone);
            String requestCode = guestRequest.getRequestCode();
            LocalDateTime enterTime = guestRequest.getEnterTime();
            LocalDateTime leaveTime = guestRequest.getLeaveTime();
            String guestName = guestRequest.getGuestName();
            String guestPhone = guestRequest.getGuestPhone();
            int ownerId = guestRequest.getOwnerId();
            String hash = guestRequest.getHash();
            return new Response<>("登记查找成功", new RequestDto(
                    requestCode,
                    enterTime,
                    leaveTime,
                    guestName,
                    guestPhone,
                    ownerId,
                    hash
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(e.getMessage(), null);
        }
    }

    @GetMapping("/check_request_by_qr_code")
    public Response<RequestDto> checkRequestByQrCode(@RequestParam String qrCode) {
        try {
            GuestRequest guestRequest = guestService.checkIfRequestValidByQrCode(qrCode);
            String requestCode = guestRequest.getRequestCode();
            LocalDateTime enterTime = guestRequest.getEnterTime();
            LocalDateTime leaveTime = guestRequest.getLeaveTime();
            String guestName = guestRequest.getGuestName();
            String guestPhone = guestRequest.getGuestPhone();
            int ownerId = guestRequest.getOwnerId();
            String hash = guestRequest.getHash();
            return new Response<>("登记查找成功", new RequestDto(
                    requestCode,
                    enterTime,
                    leaveTime,
                    guestName,
                    guestPhone,
                    ownerId,
                    hash
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(e.getMessage(), null);
        }
    }

    @PostMapping("/allow_request")
    public Response<Object> allowRequest(@RequestBody AllowRequestDto allowRequestDto) {
        try {
            GuestRequest guestRequest = guestService.getRequestByRequestCode(allowRequestDto.getRequestCode());
            guestService.allowRequest(guestRequest, allowRequestDto.getEntrance());
            return new Response<>("已允许访客进入", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(e.getMessage(), null);
        }
    }

    @PostMapping("/refuse_request")
    public Response<Object> refuseRequest(@RequestBody RefuseRequestDto refuseRequestDto) {
        try {
            GuestRequest guestRequest = guestService.getRequestByRequestCode(refuseRequestDto.getRequestCode());
            guestService.refuseRequest(guestRequest);
            return new Response<>("已拒绝访客进入", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(e.getMessage(), null);
        }
    }

    @GetMapping("/get_requests_by_owner")
    public Response<List<RequestDto>> getRequestsByOwner(@RequestParam int ownerId) {
        try {
            List<GuestRequest> guestRequests = guestService.getRequestsByOwnerId(ownerId);
            List<RequestDto> requestDtos = new Vector<>();
            for(GuestRequest guestRequest: guestRequests) {
                requestDtos.add(new RequestDto(
                        guestRequest.getRequestCode(),
                        guestRequest.getEnterTime(),
                        guestRequest.getLeaveTime(),
                        guestRequest.getGuestName(),
                        guestRequest.getGuestPhone(),
                        guestRequest.getOwnerId(),
                        guestRequest.getHash()
                ));
            }
            return new Response<>("访客登记查询成功", requestDtos);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(e.getMessage(), null);
        }
    }

    @GetMapping("/get_records_by_owner")
    public Response<List<RecordDto>> getRecordsByOwner(@RequestParam int ownerId) {
        try {
            List<GuestRecord> guestRecords = guestService.getRecordsByOwnerId(ownerId);
            List<RecordDto> recordDtos = new Vector<>();
            for(GuestRecord guestRecord: guestRecords) {
                recordDtos.add(new RecordDto(
                        guestRecord.getId(),
                        guestRecord.getEnterTime(),
                        guestRecord.getLeaveTime(),
                        guestRecord.getGuestName(),
                        guestRecord.getGuestPhone(),
                        guestService.getEntranceById(guestRecord.getId()),
                        guestRecord.getOwnerId()
                ));
            }
            return new Response<>("访客记录查询成功", recordDtos);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(e.getMessage(), null);
        }
    }

    @DeleteMapping("/delete_request?code={requestCode}")
    public Response<Object> deleteRequestByCode(@PathVariable String requestCode) {
        try {
            GuestRequest guestRequest = guestService.getRequestByRequestCode(requestCode);
            guestService.deleteRequest(guestRequest);
            return new Response<>("删除成功，已取消该访客登记", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(e.getMessage(), null);
        }
    }
}
