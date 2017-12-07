package net.mapthinks.web.rest;

import net.mapthinks.AtolyeApp;

import net.mapthinks.domain.MaintainType;
import net.mapthinks.repository.MaintainTypeRepository;
import net.mapthinks.repository.search.MaintainTypeSearchRepository;
import net.mapthinks.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MaintainTypeResource REST controller.
 *
 * @see MaintainTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AtolyeApp.class)
public class MaintainTypeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private MaintainTypeRepository maintainTypeRepository;

    @Autowired
    private MaintainTypeSearchRepository maintainTypeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMaintainTypeMockMvc;

    private MaintainType maintainType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MaintainTypeResource maintainTypeResource = new MaintainTypeResource(maintainTypeRepository, maintainTypeSearchRepository);
        this.restMaintainTypeMockMvc = MockMvcBuilders.standaloneSetup(maintainTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaintainType createEntity(EntityManager em) {
        MaintainType maintainType = new MaintainType()
            .name(DEFAULT_NAME);
        return maintainType;
    }

    @Before
    public void initTest() {
        maintainTypeSearchRepository.deleteAll();
        maintainType = createEntity(em);
    }

    @Test
    @Transactional
    public void createMaintainType() throws Exception {
        int databaseSizeBeforeCreate = maintainTypeRepository.findAll().size();

        // Create the MaintainType
        restMaintainTypeMockMvc.perform(post("/api/maintain-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(maintainType)))
            .andExpect(status().isCreated());

        // Validate the MaintainType in the database
        List<MaintainType> maintainTypeList = maintainTypeRepository.findAll();
        assertThat(maintainTypeList).hasSize(databaseSizeBeforeCreate + 1);
        MaintainType testMaintainType = maintainTypeList.get(maintainTypeList.size() - 1);
        assertThat(testMaintainType.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the MaintainType in Elasticsearch
        MaintainType maintainTypeEs = maintainTypeSearchRepository.findOne(testMaintainType.getId());
        assertThat(maintainTypeEs).isEqualToComparingFieldByField(testMaintainType);
    }

    @Test
    @Transactional
    public void createMaintainTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = maintainTypeRepository.findAll().size();

        // Create the MaintainType with an existing ID
        maintainType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaintainTypeMockMvc.perform(post("/api/maintain-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(maintainType)))
            .andExpect(status().isBadRequest());

        // Validate the MaintainType in the database
        List<MaintainType> maintainTypeList = maintainTypeRepository.findAll();
        assertThat(maintainTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = maintainTypeRepository.findAll().size();
        // set the field null
        maintainType.setName(null);

        // Create the MaintainType, which fails.

        restMaintainTypeMockMvc.perform(post("/api/maintain-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(maintainType)))
            .andExpect(status().isBadRequest());

        List<MaintainType> maintainTypeList = maintainTypeRepository.findAll();
        assertThat(maintainTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMaintainTypes() throws Exception {
        // Initialize the database
        maintainTypeRepository.saveAndFlush(maintainType);

        // Get all the maintainTypeList
        restMaintainTypeMockMvc.perform(get("/api/maintain-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(maintainType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getMaintainType() throws Exception {
        // Initialize the database
        maintainTypeRepository.saveAndFlush(maintainType);

        // Get the maintainType
        restMaintainTypeMockMvc.perform(get("/api/maintain-types/{id}", maintainType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(maintainType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMaintainType() throws Exception {
        // Get the maintainType
        restMaintainTypeMockMvc.perform(get("/api/maintain-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMaintainType() throws Exception {
        // Initialize the database
        maintainTypeRepository.saveAndFlush(maintainType);
        maintainTypeSearchRepository.save(maintainType);
        int databaseSizeBeforeUpdate = maintainTypeRepository.findAll().size();

        // Update the maintainType
        MaintainType updatedMaintainType = maintainTypeRepository.findOne(maintainType.getId());
        updatedMaintainType
            .name(UPDATED_NAME);

        restMaintainTypeMockMvc.perform(put("/api/maintain-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMaintainType)))
            .andExpect(status().isOk());

        // Validate the MaintainType in the database
        List<MaintainType> maintainTypeList = maintainTypeRepository.findAll();
        assertThat(maintainTypeList).hasSize(databaseSizeBeforeUpdate);
        MaintainType testMaintainType = maintainTypeList.get(maintainTypeList.size() - 1);
        assertThat(testMaintainType.getName()).isEqualTo(UPDATED_NAME);

        // Validate the MaintainType in Elasticsearch
        MaintainType maintainTypeEs = maintainTypeSearchRepository.findOne(testMaintainType.getId());
        assertThat(maintainTypeEs).isEqualToComparingFieldByField(testMaintainType);
    }

    @Test
    @Transactional
    public void updateNonExistingMaintainType() throws Exception {
        int databaseSizeBeforeUpdate = maintainTypeRepository.findAll().size();

        // Create the MaintainType

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMaintainTypeMockMvc.perform(put("/api/maintain-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(maintainType)))
            .andExpect(status().isCreated());

        // Validate the MaintainType in the database
        List<MaintainType> maintainTypeList = maintainTypeRepository.findAll();
        assertThat(maintainTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMaintainType() throws Exception {
        // Initialize the database
        maintainTypeRepository.saveAndFlush(maintainType);
        maintainTypeSearchRepository.save(maintainType);
        int databaseSizeBeforeDelete = maintainTypeRepository.findAll().size();

        // Get the maintainType
        restMaintainTypeMockMvc.perform(delete("/api/maintain-types/{id}", maintainType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean maintainTypeExistsInEs = maintainTypeSearchRepository.exists(maintainType.getId());
        assertThat(maintainTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<MaintainType> maintainTypeList = maintainTypeRepository.findAll();
        assertThat(maintainTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMaintainType() throws Exception {
        // Initialize the database
        maintainTypeRepository.saveAndFlush(maintainType);
        maintainTypeSearchRepository.save(maintainType);

        // Search the maintainType
        restMaintainTypeMockMvc.perform(get("/api/_search/maintain-types?query=id:" + maintainType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(maintainType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaintainType.class);
        MaintainType maintainType1 = new MaintainType();
        maintainType1.setId(1L);
        MaintainType maintainType2 = new MaintainType();
        maintainType2.setId(maintainType1.getId());
        assertThat(maintainType1).isEqualTo(maintainType2);
        maintainType2.setId(2L);
        assertThat(maintainType1).isNotEqualTo(maintainType2);
        maintainType1.setId(null);
        assertThat(maintainType1).isNotEqualTo(maintainType2);
    }
}
