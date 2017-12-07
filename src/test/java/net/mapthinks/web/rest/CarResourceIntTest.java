package net.mapthinks.web.rest;

import net.mapthinks.AtolyeApp;

import net.mapthinks.domain.Car;
import net.mapthinks.repository.CarRepository;
import net.mapthinks.repository.search.CarSearchRepository;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static net.mapthinks.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CarResource REST controller.
 *
 * @see CarResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AtolyeApp.class)
public class CarResourceIntTest {

    private static final String DEFAULT_PLATE = "AAAAAAAAAA";
    private static final String UPDATED_PLATE = "BBBBBBBBBB";

    private static final String DEFAULT_CHASE_NO = "AAAAAAAAAA";
    private static final String UPDATED_CHASE_NO = "BBBBBBBBBB";

    private static final Double DEFAULT_KM = 1D;
    private static final Double UPDATED_KM = 2D;

    private static final Double DEFAULT_LATEST_OIL_CHANGE_KM = 1D;
    private static final Double UPDATED_LATEST_OIL_CHANGE_KM = 2D;

    private static final ZonedDateTime DEFAULT_LAST_VISIT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_VISIT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarSearchRepository carSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCarMockMvc;

    private Car car;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CarResource carResource = new CarResource(carRepository, carSearchRepository);
        this.restCarMockMvc = MockMvcBuilders.standaloneSetup(carResource)
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
    public static Car createEntity(EntityManager em) {
        Car car = new Car()
            .plate(DEFAULT_PLATE)
            .chaseNo(DEFAULT_CHASE_NO)
            .km(DEFAULT_KM)
            .latestOilChangeKm(DEFAULT_LATEST_OIL_CHANGE_KM)
            .lastVisit(DEFAULT_LAST_VISIT);
        return car;
    }

    @Before
    public void initTest() {
        carSearchRepository.deleteAll();
        car = createEntity(em);
    }

    @Test
    @Transactional
    public void createCar() throws Exception {
        int databaseSizeBeforeCreate = carRepository.findAll().size();

        // Create the Car
        restCarMockMvc.perform(post("/api/cars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(car)))
            .andExpect(status().isCreated());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeCreate + 1);
        Car testCar = carList.get(carList.size() - 1);
        assertThat(testCar.getPlate()).isEqualTo(DEFAULT_PLATE);
        assertThat(testCar.getChaseNo()).isEqualTo(DEFAULT_CHASE_NO);
        assertThat(testCar.getKm()).isEqualTo(DEFAULT_KM);
        assertThat(testCar.getLatestOilChangeKm()).isEqualTo(DEFAULT_LATEST_OIL_CHANGE_KM);
        assertThat(testCar.getLastVisit()).isEqualTo(DEFAULT_LAST_VISIT);

        // Validate the Car in Elasticsearch
        Car carEs = carSearchRepository.findOne(testCar.getId());
        assertThat(carEs).isEqualToComparingFieldByField(testCar);
    }

    @Test
    @Transactional
    public void createCarWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = carRepository.findAll().size();

        // Create the Car with an existing ID
        car.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarMockMvc.perform(post("/api/cars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(car)))
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkPlateIsRequired() throws Exception {
        int databaseSizeBeforeTest = carRepository.findAll().size();
        // set the field null
        car.setPlate(null);

        // Create the Car, which fails.

        restCarMockMvc.perform(post("/api/cars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(car)))
            .andExpect(status().isBadRequest());

        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCars() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList
        restCarMockMvc.perform(get("/api/cars?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(car.getId().intValue())))
            .andExpect(jsonPath("$.[*].plate").value(hasItem(DEFAULT_PLATE.toString())))
            .andExpect(jsonPath("$.[*].chaseNo").value(hasItem(DEFAULT_CHASE_NO.toString())))
            .andExpect(jsonPath("$.[*].km").value(hasItem(DEFAULT_KM.doubleValue())))
            .andExpect(jsonPath("$.[*].latestOilChangeKm").value(hasItem(DEFAULT_LATEST_OIL_CHANGE_KM.doubleValue())))
            .andExpect(jsonPath("$.[*].lastVisit").value(hasItem(sameInstant(DEFAULT_LAST_VISIT))));
    }

    @Test
    @Transactional
    public void getCar() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get the car
        restCarMockMvc.perform(get("/api/cars/{id}", car.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(car.getId().intValue()))
            .andExpect(jsonPath("$.plate").value(DEFAULT_PLATE.toString()))
            .andExpect(jsonPath("$.chaseNo").value(DEFAULT_CHASE_NO.toString()))
            .andExpect(jsonPath("$.km").value(DEFAULT_KM.doubleValue()))
            .andExpect(jsonPath("$.latestOilChangeKm").value(DEFAULT_LATEST_OIL_CHANGE_KM.doubleValue()))
            .andExpect(jsonPath("$.lastVisit").value(sameInstant(DEFAULT_LAST_VISIT)));
    }

    @Test
    @Transactional
    public void getNonExistingCar() throws Exception {
        // Get the car
        restCarMockMvc.perform(get("/api/cars/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCar() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);
        carSearchRepository.save(car);
        int databaseSizeBeforeUpdate = carRepository.findAll().size();

        // Update the car
        Car updatedCar = carRepository.findOne(car.getId());
        updatedCar
            .plate(UPDATED_PLATE)
            .chaseNo(UPDATED_CHASE_NO)
            .km(UPDATED_KM)
            .latestOilChangeKm(UPDATED_LATEST_OIL_CHANGE_KM)
            .lastVisit(UPDATED_LAST_VISIT);

        restCarMockMvc.perform(put("/api/cars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCar)))
            .andExpect(status().isOk());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
        Car testCar = carList.get(carList.size() - 1);
        assertThat(testCar.getPlate()).isEqualTo(UPDATED_PLATE);
        assertThat(testCar.getChaseNo()).isEqualTo(UPDATED_CHASE_NO);
        assertThat(testCar.getKm()).isEqualTo(UPDATED_KM);
        assertThat(testCar.getLatestOilChangeKm()).isEqualTo(UPDATED_LATEST_OIL_CHANGE_KM);
        assertThat(testCar.getLastVisit()).isEqualTo(UPDATED_LAST_VISIT);

        // Validate the Car in Elasticsearch
        Car carEs = carSearchRepository.findOne(testCar.getId());
        assertThat(carEs).isEqualToComparingFieldByField(testCar);
    }

    @Test
    @Transactional
    public void updateNonExistingCar() throws Exception {
        int databaseSizeBeforeUpdate = carRepository.findAll().size();

        // Create the Car

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCarMockMvc.perform(put("/api/cars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(car)))
            .andExpect(status().isCreated());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCar() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);
        carSearchRepository.save(car);
        int databaseSizeBeforeDelete = carRepository.findAll().size();

        // Get the car
        restCarMockMvc.perform(delete("/api/cars/{id}", car.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean carExistsInEs = carSearchRepository.exists(car.getId());
        assertThat(carExistsInEs).isFalse();

        // Validate the database is empty
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCar() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);
        carSearchRepository.save(car);

        // Search the car
        restCarMockMvc.perform(get("/api/_search/cars?query=id:" + car.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(car.getId().intValue())))
            .andExpect(jsonPath("$.[*].plate").value(hasItem(DEFAULT_PLATE.toString())))
            .andExpect(jsonPath("$.[*].chaseNo").value(hasItem(DEFAULT_CHASE_NO.toString())))
            .andExpect(jsonPath("$.[*].km").value(hasItem(DEFAULT_KM.doubleValue())))
            .andExpect(jsonPath("$.[*].latestOilChangeKm").value(hasItem(DEFAULT_LATEST_OIL_CHANGE_KM.doubleValue())))
            .andExpect(jsonPath("$.[*].lastVisit").value(hasItem(sameInstant(DEFAULT_LAST_VISIT))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Car.class);
        Car car1 = new Car();
        car1.setId(1L);
        Car car2 = new Car();
        car2.setId(car1.getId());
        assertThat(car1).isEqualTo(car2);
        car2.setId(2L);
        assertThat(car1).isNotEqualTo(car2);
        car1.setId(null);
        assertThat(car1).isNotEqualTo(car2);
    }
}
