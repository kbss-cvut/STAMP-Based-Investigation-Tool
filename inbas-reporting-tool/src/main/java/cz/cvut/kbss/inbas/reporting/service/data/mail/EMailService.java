package cz.cvut.kbss.inbas.reporting.service.data.mail;

import cz.cvut.kbss.inbas.reporting.model.com.EMail;
import cz.cvut.kbss.inbas.reporting.persistence.dao.EmailDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
@Service
public class EMailService {
    
    @Autowired
    protected EmailDao emailDao;
    
    public EMail getMailById(String id){
        return emailDao.findByMailId(id);
    }
    
    public void persist(EMail email){
        // I assume that the transanction is rollbacked when something goes wrong
        emailDao.persist(email);
    }
    
    public void remove(EMail email){
        emailDao.remove(email);
    }
}