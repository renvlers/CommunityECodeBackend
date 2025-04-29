package cn.edu.cqu.communityecode.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.edu.cqu.communityecode.entities.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findUserByPhone(String phone);

    List<User> findUserByUid(Integer uid);

}
