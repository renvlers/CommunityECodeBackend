package cn.edu.cqu.communityecode.service;

import cn.edu.cqu.communityecode.entity.Entrance;
import cn.edu.cqu.communityecode.repository.EntranceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntranceService {
    @Autowired
    EntranceRepository entranceRepository;

    public List<Entrance> getAllEntrances() {
        return entranceRepository.findEntrancesByIdGreaterThanEqual(1);
    }

}
