package net.mapthinks.web.rest;

import net.mapthinks.AtolyeApp;

import net.mapthinks.domain.CarBrand;
import net.mapthinks.repository.CarBrandRepository;
import net.mapthinks.repository.search.CarBrandSearchRepository;
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
 * Test class for the CarBrandResource REST controller.
 *
 * @see CarBrandResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AtolyeApp.class)
public class CarBrandResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private CarBrandRepository carBrandRepository;

    @Autowired
    private CarBrandSearchRepository carBrandSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCarBrandMockMvc;

    private CarBrand carBrand;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CarBrandResource carBrandResource = new CarBrandResource(carBrandRepository, carBrandSearchRepository);
        this.restCarBrandMockMvc = MockMvcBuilders.standaloneSetup(carBrandResource)
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
    public static CarBrand createEntity(EntityManager em) {
        CarBrand carBrand = new CarBrand()
            .name(DEFAULT_NAME);
        return carBrand;
    }

    @Before
    public void initTest() {
        carBrandSearchRepository.deleteAll();
        carBrand = createEntity(em);
    }

    @Test
    @Transactional
    public void createCarBrand() throws Exception {
        int databaseSizeBeforeCreate = carBrandRepository.findAll().size();

        // Create the CarBrand
        restCarBrandMockMvc.perform(post("/api/car-brands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(carBrand)))
            .andExpect(status().isCreated());

        // Validate the CarBrand in the database
        List<CarBrand> carBrandList = carBrandRepository.findAll();
        assertThat(carBrandList).hasSize(databaseSizeBeforeCreate + 1);
        CarBrand testCarBrand = carBrandList.get(carBrandList.size() - 1);
        assertThat(testCarBrand.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the CarBrand in Elasticsearch
        CarBrand carBrandEs = carBrandSearchRepository.findOne(testCarBrand.getId());
        assertThat(carBrandEs).isEqualToComparingFieldByField(testCarBrand);
    }

    @Test
    @Transactional
    public void createCarBrandWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = carBrandRepository.findAll().size();

        // Create the CarBrand with an existing ID
        carBrand.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarBrandMockMvc.perform(post("/api/car-brands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(carBrand)))
            .andExpect(status().isBadRequest());

        // Validate the CarBrand in the database
        List<CarBrand> carBrandList = carBrandRepository.findAll();
        assertThat(carBrandList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = carBrandRepository.findAll().size();
        // set the field null
        carBrand.setName(null);

        // Create the CarBrand, which fails.

        restCarBrandMockMvc.perform(post("/api/car-brands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(carBrand)))
            .andExpect(status().isBadRequest());

        List<CarBrand> carBrandList = carBrandRepository.findAll();
        assertThat(carBrandList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCarBrands() throws Exception {
        // Initialize the database
        carBrandRepository.saveAndFlush(carBrand);

        // Get all the carBrandList
        restCarBrandMockMvc.perform(get("/api/car-brands?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carBrand.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getCarBrand() throws Exception {
        // Initialize the database
        carBrandRepository.saveAndFlush(carBrand);

        // Get the carBrand
        restCarBrandMockMvc.perform(get("/api/car-brands/{id}", carBrand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(carBrand.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCarBrand() throws Exception {
        // Get the carBrand
        restCarBrandMockMvc.perform(get("/api/car-brands/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCarBrand() throws Exception {
        // Initialize the database
        carBrandRepository.saveAndFlush(carBrand);
        carBrandSearchRepository.save(carBrand);
        int databaseSizeBeforeUpdate = carBrandRepository.findAll().size();

        // Update the carBrand
        CarBrand updatedCarBrand = carBrandRepository.findOne(carBrand.getId());
        updatedCarBrand
            .name(UPDATED_NAME);

        restCarBrandMockMvc.perform(put("/api/car-brands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCarBrand)))
            .andExpect(status().isOk());

        // Validate the CarBrand in the database
        List<CarBrand> carBrandList = carBrandRepository.findAll();
        assertThat(carBrandList).hasSize(databaseSizeBeforeUpdate);
        CarBrand testCarBrand = carBrandList.get(carBrandList.size() - 1);
        assertThat(testCarBrand.getName()).isEqualTo(UPDATED_NAME);

        // Validate the CarBrand in Elasticsearch
        CarBrand carBrandEs = carBrandSearchRepository.findOne(testCarBrand.getId());
        assertThat(carBrandEs).isEqualToComparingFieldByField(testCarBrand);
    }

    @Test
    @Transactional
    public void updateNonExistingCarBrand() throws Exception {
        int databaseSizeBeforeUpdate = carBrandRepository.findAll().size();

        // Create the CarBrand

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCarBrandMockMvc.perform(put("/api/car-brands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(carBrand)))
            .andExpect(status().isCreated());

        // Validate the CarBrand in the database
        List<CarBrand> carBrandList = carBrandRepository.findAll();
        assertThat(carBrandList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCarBrand() throws Exception {
        // Initialize the database
        carBrandRepository.saveAndFlush(carBrand);
        carBrandSearchRepository.save(carBrand);
        int databaseSizeBeforeDelete = carBrandRepository.findAll().size();

        // Get the carBrand
        restCarBrandMockMvc.perform(delete("/api/car-brands/{id}", carBrand.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean carBrandExistsInEs = carBrandSearchRepository.exists(carBrand.getId());
        assertThat(carBrandExistsInEs).isFalse();

        // Validate the database is empty
        List<CarBrand> carBrandList = carBrandRepository.findAll();
        assertThat(carBrandList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCarBrand() throws Exception {
        // Initialize the database
        carBrandRepository.saveAndFlush(carBrand);
        carBrandSearchRepository.save(carBrand);

        // Search the carBrand
        restCarBrandMockMvc.perform(get("/api/_search/car-brands?query=id:" + carBrand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carBrand.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CarBrand.class);
        CarBrand carBrand1 = new CarBrand();
        carBrand1.setId(1L);
        CarBrand carBrand2 = new CarBrand();
        carBrand2.setId(carBrand1.getId());
        assertThat(carBrand1).isEqualTo(carBrand2);
        carBrand2.setId(2L);
        assertThat(carBrand1).isNotEqualTo(carBrand2);
        carBrand1.setId(null);
        assertThat(carBrand1).isNotEqualTo(carBrand2);
    }
}
