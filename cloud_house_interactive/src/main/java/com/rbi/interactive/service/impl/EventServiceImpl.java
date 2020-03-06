package com.rbi.interactive.service.impl;

import com.google.common.collect.Lists;
import com.rbi.interactive.dao.EventRecordDAO;
import com.rbi.interactive.dao.UserInfoDAO;
import com.rbi.interactive.entity.BillDO;
import com.rbi.interactive.entity.EventRecordDO;
import com.rbi.interactive.entity.UserInfoDO;
import com.rbi.interactive.entity.VillageDO;
import com.rbi.interactive.service.EventService;
import com.rbi.interactive.utils.PageData;
import com.rbi.interactive.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    UserInfoDAO userInfoDAO;

    @Autowired
    EventRecordDAO eventRecordDAO;

    @Override
    public PageData findByPage(String userId, Integer pageNo, Integer pageSize) {

        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);

        Pageable pageable = new PageRequest(pageNo - 1, pageSize, new Sort(Sort.Direction.DESC, "id"));
        Page<EventRecordDO> page = eventRecordDAO.findAll(new Specification<EventRecordDO>() {
            @Override
            public Predicate toPredicate(Root<EventRecordDO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                ArrayList<Predicate> predicateList = Lists.newArrayList();
                predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("organizationId"), userInfoDO.getOrganizationId())));
                return criteriaQuery.where(criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]))).getRestriction();
            }
        },pageable);

        return new PageData(page);
    }
}
