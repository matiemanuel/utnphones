package edu.utn.utnphones.controller;



import edu.utn.utnphones.exceptions.ProvinceNotExistsException;
import edu.utn.utnphones.model.Province;
import edu.utn.utnphones.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/provinces")
public class ProvinceController {

    private final ProvinceService provinceService;

    @Autowired
    public ProvinceController(ProvinceService provinceService) {
        this.provinceService = provinceService;
    }


    @GetMapping("/{provinceId}")

    public Province getProvincebyId(@PathVariable Integer provinceId) throws ProvinceNotExistsException {
        return this.provinceService.findById(provinceId);
    }

    @PostMapping("/")
    public void addProvince(@RequestBody Province newProvince){// transforma el json en una entidad
        provinceService.addProvince(newProvince);
    }

    @GetMapping("/")
    public List<Province> getAll(@RequestParam(required = false) String name){
        return provinceService.getAll(name);
    }

}
