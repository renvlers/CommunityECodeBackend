package cn.edu.cqu.communityecode.repositories;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.edu.cqu.communityecode.entities.Entrance;

import java.util.List;

@Repository
public interface EntranceRepository extends JpaRepository<Entrance, Integer> {
    public List<Entrance> findEntranceById(int id);

    @NotNull
    public List<Entrance> findEntrancesByIdGreaterThanEqual(int id);
}
