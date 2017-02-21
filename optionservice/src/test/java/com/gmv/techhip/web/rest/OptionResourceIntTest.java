package com.gmv.techhip.web.rest;

import com.gmv.techhip.OptionserviceApp;

import com.gmv.techhip.domain.Option;
import com.gmv.techhip.repository.OptionRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the OptionResource REST controller.
 *
 * @see OptionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OptionserviceApp.class)
public class OptionResourceIntTest {

    private static final String DEFAULT_OPTION = "AAAAAAAAAA";
    private static final String UPDATED_OPTION = "BBBBBBBBBB";

    @Inject
    private OptionRepository optionRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restOptionMockMvc;

    private Option option;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OptionResource optionResource = new OptionResource();
        ReflectionTestUtils.setField(optionResource, "optionRepository", optionRepository);
        this.restOptionMockMvc = MockMvcBuilders.standaloneSetup(optionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Option createEntity(EntityManager em) {
        Option option = new Option()
                .option(DEFAULT_OPTION);
        return option;
    }

    @Before
    public void initTest() {
        option = createEntity(em);
    }

    @Test
    @Transactional
    public void createOption() throws Exception {
        int databaseSizeBeforeCreate = optionRepository.findAll().size();

        // Create the Option

        restOptionMockMvc.perform(post("/api/options")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(option)))
            .andExpect(status().isCreated());

        // Validate the Option in the database
        List<Option> optionList = optionRepository.findAll();
        assertThat(optionList).hasSize(databaseSizeBeforeCreate + 1);
        Option testOption = optionList.get(optionList.size() - 1);
        assertThat(testOption.getOption()).isEqualTo(DEFAULT_OPTION);
    }

    @Test
    @Transactional
    public void createOptionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = optionRepository.findAll().size();

        // Create the Option with an existing ID
        Option existingOption = new Option();
        existingOption.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOptionMockMvc.perform(post("/api/options")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingOption)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Option> optionList = optionRepository.findAll();
        assertThat(optionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllOptions() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList
        restOptionMockMvc.perform(get("/api/options?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(option.getId().intValue())))
            .andExpect(jsonPath("$.[*].option").value(hasItem(DEFAULT_OPTION.toString())));
    }

    @Test
    @Transactional
    public void getOption() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get the option
        restOptionMockMvc.perform(get("/api/options/{id}", option.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(option.getId().intValue()))
            .andExpect(jsonPath("$.option").value(DEFAULT_OPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOption() throws Exception {
        // Get the option
        restOptionMockMvc.perform(get("/api/options/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOption() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);
        int databaseSizeBeforeUpdate = optionRepository.findAll().size();

        // Update the option
        Option updatedOption = optionRepository.findOne(option.getId());
        updatedOption
                .option(UPDATED_OPTION);

        restOptionMockMvc.perform(put("/api/options")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOption)))
            .andExpect(status().isOk());

        // Validate the Option in the database
        List<Option> optionList = optionRepository.findAll();
        assertThat(optionList).hasSize(databaseSizeBeforeUpdate);
        Option testOption = optionList.get(optionList.size() - 1);
        assertThat(testOption.getOption()).isEqualTo(UPDATED_OPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingOption() throws Exception {
        int databaseSizeBeforeUpdate = optionRepository.findAll().size();

        // Create the Option

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restOptionMockMvc.perform(put("/api/options")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(option)))
            .andExpect(status().isCreated());

        // Validate the Option in the database
        List<Option> optionList = optionRepository.findAll();
        assertThat(optionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteOption() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);
        int databaseSizeBeforeDelete = optionRepository.findAll().size();

        // Get the option
        restOptionMockMvc.perform(delete("/api/options/{id}", option.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Option> optionList = optionRepository.findAll();
        assertThat(optionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
