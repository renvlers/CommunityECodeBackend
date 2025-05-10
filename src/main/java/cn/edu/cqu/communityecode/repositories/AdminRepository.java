package cn.edu.cqu.communityecode.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.edu.cqu.communityecode.entities.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    List<Admin> findAdminByAid(int aid);

    List<Admin> findAdminByPhone(String phone);
}
