package cn.edu.cqu.communityecode.repository;

import cn.edu.cqu.communityecode.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findByUid(int uid);

}
