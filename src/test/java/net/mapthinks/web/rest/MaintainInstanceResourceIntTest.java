package net.mapthinks.web.rest;

import net.mapthinks.AtolyeApp;

import net.mapthinks.domain.MaintainInstance;
import net.mapthinks.repository.MaintainInstanceRepository;
import net.mapthinks.repository.search.MaintainInstanceSearchRepository;
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
 * Test class for the MaintainInstanceResource REST controller.
 *
 * @see MaintainInstanceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AtolyeApp.class)
public class MaintainInstanceResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final Double DEFAULT_PRICE_SECOND = 1D;
    private static final Double UPDATED_PRICE_SECOND = 2D;

    @Autowired
    private MaintainInstanceRepository maintainInstanceRepository;

    @Autowired
    private MaintainInstanceSearchRepository maintainInstanceSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMaintainInstanceMockMvc;

    private MaintainInstance maintainInstance;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MaintainInstanceResource maintainInstanceResource = new MaintainInstanceResource(maintainInstanceRepository, maintainInstanceSearchRepository);
        this.restMaintainInstanceMockMvc = MockMvcBuilders.standaloneSetup(maintainInstanceResource)
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
    public static MaintainInstance createEntity(EntityManager em) {
        MaintainInstance maintainInstance = new MaintainInstance()
            .name(DEFAULT_NAME)
            .comment(DEFAULT_COMMENT)
            .price(DEFAULT_PRICE)
            .priceSecond(DEFAULT_PRICE_SECOND);
        return maintainInstance;
    }

    @Before
    public void initTest() {
        maintainInstanceSearchRepository.deleteAll();
        maintainInstance = createEntity(em);
    }

    @Test
    @Transactional
    public void createMaintainInstance() throws Exception {
        int databaseSizeBeforeCreate = maintainInstanceRepository.findAll().size();

        // Create the MaintainInstance
        restMaintainInstanceMockMvc.perform(post("/api/maintain-instances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(maintainInstance)))
            .andExpect(status().isCreated());

        // Validate the MaintainInstance in the database
        List<MaintainInstance> maintainInstanceList = maintainInstanceRepository.findAll();
        assertThat(maintainInstanceList).hasSize(databaseSizeBeforeCreate + 1);
        MaintainInstance testMaintainInstance = maintainInstanceList.get(maintainInstanceList.size() - 1);
        assertThat(testMaintainInstance.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMaintainInstance.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testMaintainInstance.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testMaintainInstance.getPriceSecond()).isEqualTo(DEFAULT_PRICE_SECOND);

        // Validate the MaintainInstance in Elasticsearch
        MaintainInstance maintainInstanceEs = maintainInstanceSearchRepository.findOne(testMaintainInstance.getId());
        assertThat(maintainInstanceEs).isEqualToComparingFieldByField(testMaintainInstance);
    }

    @Test
    @Transactional
    public void createMaintainInstanceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = maintainInstanceRepository.findAll().size();

        // Create the MaintainInstance with an existing ID
        maintainInstance.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaintainInstanceMockMvc.perform(post("/api/maintain-instances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(maintainInstance)))
            .andExpect(status().isBadRequest());

        // Validate the MaintainInstance in the database
        List<MaintainInstance> maintainInstanceList = maintainInstanceRepository.findAll();
        assertThat(maintainInstanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = maintainInstanceRepository.findAll().size();
        // set the field null
        maintainInstance.setName(null);

        // Create the MaintainInstance, which fails.

        restMaintainInstanceMockMvc.perform(post("/api/maintain-instances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(maintainInstance)))
            .andExpect(status().isBadRequest());

        List<MaintainInstance> maintainInstanceList = maintainInstanceRepository.findAll();
        assertThat(maintainInstanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMaintainInstances() throws Exception {
        // Initialize the database
        maintainInstanceRepository.saveAndFlush(maintainInstance);

        // Get all the maintainInstanceList
        restMaintainInstanceMockMvc.perform(get("/api/maintain-instances?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(maintainInstance.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].priceSecond").value(hasItem(DEFAULT_PRICE_SECOND.doubleValue())));
    }

    @Test
    @Transactional
    public void getMaintainInstance() throws Exception {
        // Initialize the database
        maintainInstanceRepository.saveAndFlush(maintainInstance);

        // Get the maintainInstance
        restMaintainInstanceMockMvc.perform(get("/api/maintain-instances/{id}", maintainInstance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(maintainInstance.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.priceSecond").value(DEFAULT_PRICE_SECOND.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMaintainInstance() throws Exception {
        // Get the maintainInstance
        restMaintainInstanceMockMvc.perform(get("/api/maintain-instances/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMaintainInstance() throws Exception {
        // Initialize the database
        maintainInstanceRepository.saveAndFlush(maintainInstance);
        maintainInstanceSearchRepository.save(maintainInstance);
        int databaseSizeBeforeUpdate = maintainInstanceRepository.findAll().size();

        // Update the maintainInstance
        MaintainInstance updatedMaintainInstance = maintainInstanceRepository.findOne(maintainInstance.getId());
        updatedMaintainInstance
            .name(UPDATED_NAME)
            .comment(UPDATED_COMMENT)
            .price(UPDATED_PRICE)
            .priceSecond(UPDATED_PRICE_SECOND);

        restMaintainInstanceMockMvc.perform(put("/api/maintain-instances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMaintainInstance)))
            .andExpect(status().isOk());

        // Validate the MaintainInstance in the database
        List<MaintainInstance> maintainInstanceList = maintainInstanceRepository.findAll();
        assertThat(maintainInstanceList).hasSize(databaseSizeBeforeUpdate);
        MaintainInstance testMaintainInstance = maintainInstanceList.get(maintainInstanceList.size() - 1);
        assertThat(testMaintainInstance.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMaintainInstance.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testMaintainInstance.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testMaintainInstance.getPriceSecond()).isEqualTo(UPDATED_PRICE_SECOND);

        // Validate the MaintainInstance in Elasticsearch
        MaintainInstance maintainInstanceEs = maintainInstanceSearchRepository.findOne(testMaintainInstance.getId());
        assertThat(maintainInstanceEs).isEqualToComparingFieldByField(testMaintainInstance);
    }

    @Test
    @Transactional
    public void updateNonExistingMaintainInstance() throws Exception {
        int databaseSizeBeforeUpdate = maintainInstanceRepository.findAll().size();

        // Create the MaintainInstance

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMaintainInstanceMockMvc.perform(put("/api/maintain-instances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(maintainInstance)))
            .andExpect(status().isCreated());

        // Validate the MaintainInstance in the database
        List<MaintainInstance> maintainInstanceList = maintainInstanceRepository.findAll();
        assertThat(maintainInstanceList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMaintainInstance() throws Exception {
        // Initialize the database
        maintainInstanceRepository.saveAndFlush(maintainInstance);
        maintainInstanceSearchRepository.save(maintainInstance);
        int databaseSizeBeforeDelete = maintainInstanceRepository.findAll().size();

        // Get the maintainInstance
        restMaintainInstanceMockMvc.perform(delete("/api/maintain-instances/{id}", maintainInstance.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean maintainInstanceExistsInEs = maintainInstanceSearchRepository.exists(maintainInstance.getId());
        assertThat(maintainInstanceExistsInEs).isFalse();

        // Validate the database is empty
        List<MaintainInstance> maintainInstanceList = maintainInstanceRepository.findAll();
        assertThat(maintainInstanceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMaintainInstance() throws Exception {
        // Initialize the database
        maintainInstanceRepository.saveAndFlush(maintainInstance);
        maintainInstanceSearchRepository.save(maintainInstance);

        // Search the maintainInstance
        restMaintainInstanceMockMvc.perform(get("/api/_search/maintain-instances?query=id:" + maintainInstance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(maintainInstance.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].priceSecond").value(hasItem(DEFAULT_PRICE_SECOND.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaintainInstance.class);
        MaintainInstance maintainInstance1 = new MaintainInstance();
        maintainInstance1.setId(1L);
        MaintainInstance maintainInstance2 = new MaintainInstance();
        maintainInstance2.setId(maintainInstance1.getId());
        assertThat(maintainInstance1).isEqualTo(maintainInstance2);
        maintainInstance2.setId(2L);
        assertThat(maintainInstance1).isNotEqualTo(maintainInstance2);
        maintainInstance1.setId(null);
        assertThat(maintainInstance1).isNotEqualTo(maintainInstance2);
    }
}
