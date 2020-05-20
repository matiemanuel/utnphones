package edu.utn.utnphones.service;

import edu.utn.utnphones.exceptions.ProvinceNotExistsException;
import edu.utn.utnphones.model.Province;
import edu.utn.utnphones.repository.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class ProvinceService {

    private final ProvinceRepository provinceRepository;

    @Autowired
    public ProvinceService(ProvinceRepository provinceRepository) {
        this.provinceRepository = provinceRepository;
    }

    public void addProvince(Province newProvince) {
        provinceRepository.save(newProvince);
    }

    public List<Province> getAll(String name) {
        if (isNull(name)){
            return provinceRepository.findAll();
        }
        return provinceRepository.findbyName(name);
    }

    public Province findById(Integer id) throws ProvinceNotExistsException {
        return provinceRepository.findById(id).orElseThrow(ProvinceNotExistsException::new);
    }

}
