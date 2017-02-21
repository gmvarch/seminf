package com.gmv.techhip.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gmv.techhip.domain.Option;

import com.gmv.techhip.repository.OptionRepository;
import com.gmv.techhip.web.rest.util.HeaderUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Option.
 */
@RestController
@RequestMapping("/api")
public class OptionResource {

    private final Logger log = LoggerFactory.getLogger(OptionResource.class);
        
    @Inject
    private OptionRepository optionRepository;

    /**
     * POST  /options : Create a new option.
     *
     * @param option the option to create
     * @return the ResponseEntity with status 201 (Created) and with body the new option, or with status 400 (Bad Request) if the option has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/options")
    @Timed
    public ResponseEntity<Option> createOption(@RequestBody Option option) throws URISyntaxException {
        log.debug("REST request to save Option : {}", option);
        if (option.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("option", "idexists", "A new option cannot already have an ID")).body(null);
        }
        Option result = optionRepository.save(option);
        return ResponseEntity.created(new URI("/api/options/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("option", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /options : Updates an existing option.
     *
     * @param option the option to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated option,
     * or with status 400 (Bad Request) if the option is not valid,
     * or with status 500 (Internal Server Error) if the option couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/options")
    @Timed
    public ResponseEntity<Option> updateOption(@RequestBody Option option) throws URISyntaxException {
        log.debug("REST request to update Option : {}", option);
        if (option.getId() == null) {
            return createOption(option);
        }
        Option result = optionRepository.save(option);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("option", option.getId().toString()))
            .body(result);
    }

    /**
     * GET  /options : get all the options.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of options in body
     */
    @GetMapping("/options")
    @Timed
    public List<Option> getAllOptions() {
        log.debug("REST request to get all Options");
        List<Option> options = optionRepository.findAll();
        return options;
    }

    /**
     * GET  /options/:id : get the "id" option.
     *
     * @param id the id of the option to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the option, or with status 404 (Not Found)
     */
    @GetMapping("/options/{id}")
    @Timed
    public ResponseEntity<Option> getOption(@PathVariable Long id) {
        log.debug("REST request to get Option : {}", id);
        Option option = optionRepository.findOne(id);
        return Optional.ofNullable(option)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /options/:id : delete the "id" option.
     *
     * @param id the id of the option to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/options/{id}")
    @Timed
    public ResponseEntity<Void> deleteOption(@PathVariable Long id) {
        log.debug("REST request to delete Option : {}", id);
        optionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("option", id.toString())).build();
    }

}
