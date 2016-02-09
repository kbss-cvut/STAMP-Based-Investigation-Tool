package cz.cvut.kbss.inbas.audit.rest;

import cz.cvut.kbss.inbas.audit.service.options.OptionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("permitAll()")
@RestController
@RequestMapping("/options")
public class OptionsController extends BaseController {

    @Autowired
    private OptionsService optionsService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getOptions(@RequestParam(value = "type", required = true) String type) {
        try {
            return optionsService.getOptions(type);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
