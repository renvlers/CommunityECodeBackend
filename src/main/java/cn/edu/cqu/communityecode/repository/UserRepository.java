package cn.edu.cqu.communityecode.repository;

import cn.edu.cqu.communityecode.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

}
