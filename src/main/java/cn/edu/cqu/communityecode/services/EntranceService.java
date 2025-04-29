package cn.edu.cqu.communityecode.services;

import cn.edu.cqu.communityecode.entities.Entrance;
import cn.edu.cqu.communityecode.repositories.EntranceRepository;

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
