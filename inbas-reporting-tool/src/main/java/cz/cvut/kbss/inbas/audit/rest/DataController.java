package cz.cvut.kbss.inbas.audit.rest;

import cz.cvut.kbss.inbas.audit.persistence.sesame.DataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data")
public class DataController extends BaseController {

    @Autowired
    private DataDao dataDao;

    @RequestMapping(method = RequestMethod.GET)
    public String getRepositoryData() {
        return dataDao.getRepositoryData();
    }
}
