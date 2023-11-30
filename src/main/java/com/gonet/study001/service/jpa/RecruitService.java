package com.gonet.study001.service.jpa;

import com.gonet.study001.domain.Recruit;
import com.gonet.study001.domain.RecruitVO;
import com.gonet.study001.repository.jpa.RecruitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class RecruitService {
    private final RecruitRepository recruitRepository;

    public Page<Recruit> list(RecruitVO vo) {
        Pageable pageable = PageRequest.of(0,10, Sort.by("regDate").descending());

        Page<Recruit> resultList =recruitRepository.findAll(pageable);
        return resultList;
    }

    public Recruit view(Long id) {
        return recruitRepository.findById(id).get();
    }

    public void update(RecruitVO vo) {
        long id = vo.getId();
        Recruit bean = recruitRepository.findById(id).get();
        bean.setName(vo.getName());
        bean.setEmail(vo.getEmail());
        bean.setAddr(vo.getAddr());
    }

    public void delete(Long id) {
        recruitRepository.deleteById(id);
    }
}
