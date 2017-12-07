package net.mapthinks.web.rest;

import net.mapthinks.AtolyeApp;

import net.mapthinks.domain.CarModel;
import net.mapthinks.repository.CarModelRepository;
import net.mapthinks.repository.search.CarModelSearchRepository;
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
 * Test class for the CarModelResource REST controller.
 *
 * @see CarModelResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AtolyeApp.class)
public class CarModelResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private CarModelRepository carModelRepository;

    @Autowired
    private CarModelSearchRepository carModelSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCarModelMockMvc;

    private CarModel carModel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CarModelResource carModelResource = new CarModelResource(carModelRepository, carModelSearchRepository);
        this.restCarModelMockMvc = MockMvcBuilders.standaloneSetup(carModelResource)
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
    public static CarModel createEntity(EntityManager em) {
        CarModel carModel = new CarModel()
            .name(DEFAULT_NAME);
        return carModel;
    }

    @Before
    public void initTest() {
        carModelSearchRepository.deleteAll();
        carModel = createEntity(em);
    }

    @Test
    @Transactional
    public void createCarModel() throws Exception {
        int databaseSizeBeforeCreate = carModelRepository.findAll().size();

        // Create the CarModel
        restCarModelMockMvc.perform(post("/api/car-models")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(carModel)))
            .andExpect(status().isCreated());

        // Validate the CarModel in the database
        List<CarModel> carModelList = carModelRepository.findAll();
        assertThat(carModelList).hasSize(databaseSizeBeforeCreate + 1);
        CarModel testCarModel = carModelList.get(carModelList.size() - 1);
        assertThat(testCarModel.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the CarModel in Elasticsearch
        CarModel carModelEs = carModelSearchRepository.findOne(testCarModel.getId());
        assertThat(carModelEs).isEqualToComparingFieldByField(testCarModel);
    }

    @Test
    @Transactional
    public void createCarModelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = carModelRepository.findAll().size();

        // Create the CarModel with an existing ID
        carModel.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarModelMockMvc.perform(post("/api/car-models")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(carModel)))
            .andExpect(status().isBadRequest());

        // Validate the CarModel in the database
        List<CarModel> carModelList = carModelRepository.findAll();
        assertThat(carModelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = carModelRepository.findAll().size();
        // set the field null
        carModel.setName(null);

        // Create the CarModel, which fails.

        restCarModelMockMvc.perform(post("/api/car-models")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(carModel)))
            .andExpect(status().isBadRequest());

        List<CarModel> carModelList = carModelRepository.findAll();
        assertThat(carModelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCarModels() throws Exception {
        // Initialize the database
        carModelRepository.saveAndFlush(carModel);

        // Get all the carModelList
        restCarModelMockMvc.perform(get("/api/car-models?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carModel.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getCarModel() throws Exception {
        // Initialize the database
        carModelRepository.saveAndFlush(carModel);

        // Get the carModel
        restCarModelMockMvc.perform(get("/api/car-models/{id}", carModel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(carModel.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCarModel() throws Exception {
        // Get the carModel
        restCarModelMockMvc.perform(get("/api/car-models/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCarModel() throws Exception {
        // Initialize the database
        carModelRepository.saveAndFlush(carModel);
        carModelSearchRepository.save(carModel);
        int databaseSizeBeforeUpdate = carModelRepository.findAll().size();

        // Update the carModel
        CarModel updatedCarModel = carModelRepository.findOne(carModel.getId());
        updatedCarModel
            .name(UPDATED_NAME);

        restCarModelMockMvc.perform(put("/api/car-models")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCarModel)))
            .andExpect(status().isOk());

        // Validate the CarModel in the database
        List<CarModel> carModelList = carModelRepository.findAll();
        assertThat(carModelList).hasSize(databaseSizeBeforeUpdate);
        CarModel testCarModel = carModelList.get(carModelList.size() - 1);
        assertThat(testCarModel.getName()).isEqualTo(UPDATED_NAME);

        // Validate the CarModel in Elasticsearch
        CarModel carModelEs = carModelSearchRepository.findOne(testCarModel.getId());
        assertThat(carModelEs).isEqualToComparingFieldByField(testCarModel);
    }

    @Test
    @Transactional
    public void updateNonExistingCarModel() throws Exception {
        int databaseSizeBeforeUpdate = carModelRepository.findAll().size();

        // Create the CarModel

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCarModelMockMvc.perform(put("/api/car-models")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(carModel)))
            .andExpect(status().isCreated());

        // Validate the CarModel in the database
        List<CarModel> carModelList = carModelRepository.findAll();
        assertThat(carModelList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCarModel() throws Exception {
        // Initialize the database
        carModelRepository.saveAndFlush(carModel);
        carModelSearchRepository.save(carModel);
        int databaseSizeBeforeDelete = carModelRepository.findAll().size();

        // Get the carModel
        restCarModelMockMvc.perform(delete("/api/car-models/{id}", carModel.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean carModelExistsInEs = carModelSearchRepository.exists(carModel.getId());
        assertThat(carModelExistsInEs).isFalse();

        // Validate the database is empty
        List<CarModel> carModelList = carModelRepository.findAll();
        assertThat(carModelList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCarModel() throws Exception {
        // Initialize the database
        carModelRepository.saveAndFlush(carModel);
        carModelSearchRepository.save(carModel);

        // Search the carModel
        restCarModelMockMvc.perform(get("/api/_search/car-models?query=id:" + carModel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carModel.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CarModel.class);
        CarModel carModel1 = new CarModel();
        carModel1.setId(1L);
        CarModel carModel2 = new CarModel();
        carModel2.setId(carModel1.getId());
        assertThat(carModel1).isEqualTo(carModel2);
        carModel2.setId(2L);
        assertThat(carModel1).isNotEqualTo(carModel2);
        carModel1.setId(null);
        assertThat(carModel1).isNotEqualTo(carModel2);
    }
}
