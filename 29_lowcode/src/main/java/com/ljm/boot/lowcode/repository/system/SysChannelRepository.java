package com.ljm.boot.lowcode.repository.system;

import com.ljm.boot.lowcode.model.system.SysChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface SysChannelRepository extends JpaRepository<SysChannel, Integer>, JpaSpecificationExecutor {

    List<SysChannel> findAllByParentIdOrderByOrderStr(Integer parentId);

    SysChannel findTopByChannelCode(String channelCode);

    SysChannel findTopByChannelName(String channelName);

}
