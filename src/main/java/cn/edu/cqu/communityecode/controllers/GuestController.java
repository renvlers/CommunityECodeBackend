package cn.edu.cqu.communityecode.controllers;

import cn.edu.cqu.communityecode.dtos.*;
import cn.edu.cqu.communityecode.entities.GuestRecord;
import cn.edu.cqu.communityecode.entities.GuestRequest;
import cn.edu.cqu.communityecode.services.GuestService;
import cn.edu.cqu.communityecode.utils.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Vector;

@RestController
@RequestMapping("/guest")
public class GuestController {

    @Autowired
    private GuestService guestService;

    /** 创建访客登记 */
    @PostMapping("/create_request")
    public ResponseEntity<Response<RequestDto>> createRequest(
            @RequestBody CreateRequestRequestDto dto) {

        try {
            String requestCode = guestService.generateRequestCode();
            LocalDateTime enterTime = dto.getEnterTime();
            LocalDateTime leaveTime = dto.getLeaveTime();
            String guestName = dto.getGuestName();
            String guestPhone = dto.getGuestPhone();
            int ownerId = dto.getOwnerId();
            String hash = guestService.generateQrCode(
                    requestCode, enterTime, leaveTime, guestName, guestPhone, ownerId);

            GuestRequest guestRequest = new GuestRequest();
            guestRequest.setRequestCode(requestCode);
            guestRequest.setEnterTime(enterTime);
            guestRequest.setLeaveTime(leaveTime);
            guestRequest.setGuestName(guestName);
            guestRequest.setGuestPhone(guestPhone);
            guestRequest.setOwnerId(ownerId);
            guestRequest.setHash(hash);
            guestService.createNewRequest(guestRequest);

            RequestDto data = new RequestDto(
                    requestCode, enterTime, leaveTime,
                    guestName, guestPhone, ownerId, hash);

            return ResponseEntity.ok(new Response<>("访客登记成功", data));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response<>(e.getMessage(), null));
        }
    }

    /** 通过登记码查询登记 */
    @GetMapping("/check_request_by_code")
    public ResponseEntity<Response<RequestDto>> checkRequestByCode(@RequestParam String code) {

        try {
            GuestRequest req = guestService.checkIfRequestValidByCode(code);

            RequestDto data = new RequestDto(
                    req.getRequestCode(), req.getEnterTime(), req.getLeaveTime(),
                    req.getGuestName(), req.getGuestPhone(), req.getOwnerId(), req.getHash());

            return ResponseEntity.ok(new Response<>("登记查找成功", data));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response<>(e.getMessage(), null));
        }
    }

    /** 通过访客手机号查询登记 */
    @GetMapping("/check_request_by_phone")
    public ResponseEntity<Response<RequestDto>> checkRequestByPhone(@RequestParam String phone) {

        try {
            GuestRequest req = guestService.checkIfRequestValidByPhone(phone);

            RequestDto data = new RequestDto(
                    req.getRequestCode(), req.getEnterTime(), req.getLeaveTime(),
                    req.getGuestName(), req.getGuestPhone(), req.getOwnerId(), req.getHash());

            return ResponseEntity.ok(new Response<>("登记查找成功", data));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response<>(e.getMessage(), null));
        }
    }

    /** 通过二维码内容查询登记 */
    @GetMapping("/check_request_by_qr_code")
    public ResponseEntity<Response<RequestDto>> checkRequestByQrCode(@RequestParam String qrCode) {

        try {
            GuestRequest req = guestService.checkIfRequestValidByQrCode(qrCode);

            RequestDto data = new RequestDto(
                    req.getRequestCode(), req.getEnterTime(), req.getLeaveTime(),
                    req.getGuestName(), req.getGuestPhone(), req.getOwnerId(), req.getHash());

            return ResponseEntity.ok(new Response<>("登记查找成功", data));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response<>(e.getMessage(), null));
        }
    }

    /** 允许访客进入 */
    @PostMapping("/allow_request")
    public ResponseEntity<Response<Object>> allowRequest(@RequestBody AllowRequestDto dto) {

        try {
            GuestRequest req = guestService.getRequestByRequestCode(dto.getRequestCode());
            guestService.allowRequest(req, dto.getEntrance());
            return ResponseEntity.ok(new Response<>("已允许访客进入", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response<>(e.getMessage(), null));
        }
    }

    /** 拒绝访客进入 */
    @PostMapping("/refuse_request")
    public ResponseEntity<Response<Object>> refuseRequest(@RequestBody RefuseRequestDto dto) {

        try {
            GuestRequest req = guestService.getRequestByRequestCode(dto.getRequestCode());
            guestService.refuseRequest(req, dto.getEntrance());
            return ResponseEntity.ok(new Response<>("已拒绝访客进入", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response<>(e.getMessage(), null));
        }
    }

    /** 某业主的全部登记 */
    @GetMapping("/get_requests_by_owner")
    public ResponseEntity<Response<List<RequestDto>>> getRequestsByOwner(@RequestParam int ownerId) {

        try {
            List<GuestRequest> list = guestService.getRequestsByOwnerId(ownerId);
            List<RequestDto> dtos = new Vector<>();
            for (GuestRequest r : list) {
                dtos.add(new RequestDto(
                        r.getRequestCode(), r.getEnterTime(), r.getLeaveTime(),
                        r.getGuestName(), r.getGuestPhone(), r.getOwnerId(), r.getHash()));
            }
            return ResponseEntity.ok(new Response<>("访客登记查询成功", dtos));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response<>(e.getMessage(), null));
        }
    }

    /** 某业主的全部记录 */
    @GetMapping("/get_records_by_owner")
    public ResponseEntity<Response<List<RecordDto>>> getRecordsByOwner(@RequestParam int ownerId) {

        try {
            List<GuestRecord> list = guestService.getRecordsByOwnerId(ownerId);
            List<RecordDto> dtos = new Vector<>();
            for (GuestRecord r : list) {
                dtos.add(new RecordDto(
                        r.getId(), r.getEnterTime(), r.getLeaveTime(),
                        r.getGuestName(), r.getGuestPhone(),
                        guestService.getEntranceById(r.getEntrance()),
                        r.getOwnerId(), r.getRequestCode(), r.getHash(), r.getStatus()));
            }
            return ResponseEntity.ok(new Response<>("访客记录查询成功", dtos));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response<>(e.getMessage(), null));
        }
    }

    /** 撤回登记 */
    @DeleteMapping("/delete_request")
    public ResponseEntity<Response<Object>> withdrawRequestByCode(@RequestParam String requestCode) {

        try {
            GuestRequest req = guestService.getRequestByRequestCode(requestCode);
            guestService.withdrawRequest(req);
            return ResponseEntity.ok(new Response<>("已撤回该访客登记", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response<>(e.getMessage(), null));
        }
    }

    /** 根据访客手机号获取所有记录 */
    @GetMapping("/get_records_by_guest_phone")
    public ResponseEntity<Response<List<RecordDto>>> getRecordsByGuestPhone(@RequestParam String phone) {

        try {
            List<GuestRecord> list = guestService.getRecordsByGuestPhone(phone);
            List<RecordDto> dtos = new Vector<>();
            for (GuestRecord r : list) {
                dtos.add(new RecordDto(
                        r.getId(), r.getEnterTime(), r.getLeaveTime(),
                        r.getGuestName(), r.getGuestPhone(),
                        guestService.getEntranceById(r.getEntrance()),
                        r.getOwnerId(), r.getRequestCode(), r.getHash(), r.getStatus()));
            }
            return ResponseEntity.ok(new Response<>("访客记录查询成功", dtos));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response<>(e.getMessage(), null));
        }
    }

    /** 根据访客姓名获取所有记录 */
    @GetMapping("/get_records_by_guest_name")
    public ResponseEntity<Response<List<RecordDto>>> getRecordsByGuestName(@RequestParam String name) {

        try {
            List<GuestRecord> list = guestService.getRecordsByGuestName(name);
            List<RecordDto> dtos = new Vector<>();
            for (GuestRecord r : list) {
                dtos.add(new RecordDto(
                        r.getId(), r.getEnterTime(), r.getLeaveTime(),
                        r.getGuestName(), r.getGuestPhone(),
                        guestService.getEntranceById(r.getEntrance()),
                        r.getOwnerId(), r.getRequestCode(), r.getHash(), r.getStatus()));
            }
            return ResponseEntity.ok(new Response<>("访客记录查询成功", dtos));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response<>(e.getMessage(), null));
        }
    }

    /** 获取全部访客记录 */
    @GetMapping("/get_all_records")
    public ResponseEntity<Response<List<RecordDto>>> getAllRecords() {

        try {
            List<GuestRecord> list = guestService.getAllRecords();
            List<RecordDto> dtos = new Vector<>();
            for (GuestRecord r : list) {
                dtos.add(new RecordDto(
                        r.getId(), r.getEnterTime(), r.getLeaveTime(),
                        r.getGuestName(), r.getGuestPhone(),
                        guestService.getEntranceById(r.getEntrance()),
                        r.getOwnerId(), r.getRequestCode(), r.getHash(), r.getStatus()));
            }
            return ResponseEntity.ok(new Response<>("访客记录查询成功", dtos));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response<>(e.getMessage(), null));
        }
    }
}
