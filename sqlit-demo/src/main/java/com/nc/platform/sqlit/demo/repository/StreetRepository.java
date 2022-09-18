package com.nc.platform.sqlit.demo.repository;

import com.nc.platform.sqlit.demo.model.Street;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface StreetRepository extends CrudRepository<Street, String>{

    Street findByCode(String code);

    List<Street> findByAreaCode(String areaCode);

    Page<Street> findAll(@PageableDefault(value = 15, sort = {"areaCode"}, direction = Sort.Direction.DESC) Pageable pageable);
}
