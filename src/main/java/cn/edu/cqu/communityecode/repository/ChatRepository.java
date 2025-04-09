package cn.edu.cqu.communityecode.repository;

import cn.edu.cqu.communityecode.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Integer> {
}
