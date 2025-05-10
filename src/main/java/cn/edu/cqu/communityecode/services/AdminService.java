package cn.edu.cqu.communityecode.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.cqu.communityecode.entities.Admin;
import cn.edu.cqu.communityecode.repositories.AdminRepository;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    public Admin getAdminByPhone(String phone) throws Exception {
        List<Admin> admins = adminRepository.findAdminByPhone(phone);
        if (admins.isEmpty())
            throw new Exception("此物管账号不存在");
        return admins.getLast();
    }

    public Admin getAdminByAid(int aid) throws Exception {
        List<Admin> admins = adminRepository.findAdminByAid(aid);
        if (admins.isEmpty())
            throw new Exception("此物管账号不存在");
        return admins.getLast();
    }

    public void changeAdminPassword(Admin admin, String newPassword) {
        admin.setPassword(newPassword);
        adminRepository.save(admin);
    }

}
