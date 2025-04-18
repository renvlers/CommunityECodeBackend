package cn.edu.cqu.communityecode.repository;

import cn.edu.cqu.communityecode.entity.Entrance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntranceRepository extends JpaRepository<Entrance, Integer> {
    public List<Entrance> findEntranceById(int id);
}
