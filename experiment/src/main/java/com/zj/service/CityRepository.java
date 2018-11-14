package com.zj.service;

import com.zj.model.City;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created with IntelliJ IDEA.
 * User: SuShaohua
 * Date: 2018/8/23
 * Time: 15:33
 * Description: MAIN
 */
@Repository
public class CityRepository {

    private ConcurrentMap<Long, City> repository = new ConcurrentHashMap<>();

    private static final AtomicLong idGenerator = new AtomicLong(0);

    public Long save(City city) {
        Long id = idGenerator.incrementAndGet();
        city.setId(id);
        repository.put(id, city);
        return id;
    }

    public Collection<City> findAll() {
        return repository.values();
    }


    public City findCityById(Long id) {
        return repository.get(id);
    }

    public Long updateCity(City city) {
        repository.put(city.getId(), city);
        return city.getId();
    }

    public Long deleteCity(Long id) {
        repository.remove(id);
        return id;
    }
}
