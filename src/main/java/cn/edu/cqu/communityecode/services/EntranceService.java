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

    public Entrance getEntranceById(int id) throws Exception {
        List<Entrance> entrances = entranceRepository.findEntranceById(id);
        if (entrances.isEmpty())
            throw new Exception("入口信息不存在");
        return entrances.getLast();
    }

}
