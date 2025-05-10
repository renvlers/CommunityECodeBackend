package cn.edu.cqu.communityecode.controllers;

import cn.edu.cqu.communityecode.dtos.EntranceDto;
import cn.edu.cqu.communityecode.entities.Entrance;
import cn.edu.cqu.communityecode.services.EntranceService;
import cn.edu.cqu.communityecode.utils.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Vector;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/entrance")
public class EntranceController {
    @Autowired
    EntranceService entranceService;

    @GetMapping("/get_all_entrances")
    ResponseEntity<Response<List<EntranceDto>>> getAllEntrances() {
        try {
            List<Entrance> entrances = entranceService.getAllEntrances();
            List<EntranceDto> dtos = new Vector<>();
            for (Entrance entrance : entrances) {
                dtos.add(new EntranceDto(entrance.getId(), entrance.getName()));
            }
            return ResponseEntity.ok(new Response<>("入口信息查询成功", dtos));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response<>(e.getMessage(), null));
        }
    }

    @GetMapping("/get_entrance_name_by_id")
    public ResponseEntity<Response<String>> getEntranceNameById(@RequestParam int id) {
        try {
            String name = entranceService.getEntranceById(id).getName();
            return ResponseEntity.ok(new Response<>("入口名称查询成功", name));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response<>(e.getMessage(), null));
        }
    }
}
