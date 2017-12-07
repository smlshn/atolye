package net.mapthinks.web.rest;

import net.mapthinks.AtolyeApp;

import net.mapthinks.domain.SystemUser;
import net.mapthinks.repository.SystemUserRepository;
import net.mapthinks.repository.search.SystemUserSearchRepository;
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
 * Test class for the SystemUserResource REST controller.
 *
 * @see SystemUserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AtolyeApp.class)
public class SystemUserResourceIntTest {

    @Autowired
    private SystemUserRepository systemUserRepository;

    @Autowired
    private SystemUserSearchRepository systemUserSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSystemUserMockMvc;

    private SystemUser systemUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SystemUserResource systemUserResource = new SystemUserResource(systemUserRepository, systemUserSearchRepository);
        this.restSystemUserMockMvc = MockMvcBuilders.standaloneSetup(systemUserResource)
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
    public static SystemUser createEntity(EntityManager em) {
        SystemUser systemUser = new SystemUser();
        return systemUser;
    }

    @Before
    public void initTest() {
        systemUserSearchRepository.deleteAll();
        systemUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createSystemUser() throws Exception {
        int databaseSizeBeforeCreate = systemUserRepository.findAll().size();

        // Create the SystemUser
        restSystemUserMockMvc.perform(post("/api/system-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemUser)))
            .andExpect(status().isCreated());

        // Validate the SystemUser in the database
        List<SystemUser> systemUserList = systemUserRepository.findAll();
        assertThat(systemUserList).hasSize(databaseSizeBeforeCreate + 1);
        SystemUser testSystemUser = systemUserList.get(systemUserList.size() - 1);

        // Validate the SystemUser in Elasticsearch
        SystemUser systemUserEs = systemUserSearchRepository.findOne(testSystemUser.getId());
        assertThat(systemUserEs).isEqualToComparingFieldByField(testSystemUser);
    }

    @Test
    @Transactional
    public void createSystemUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = systemUserRepository.findAll().size();

        // Create the SystemUser with an existing ID
        systemUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSystemUserMockMvc.perform(post("/api/system-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemUser)))
            .andExpect(status().isBadRequest());

        // Validate the SystemUser in the database
        List<SystemUser> systemUserList = systemUserRepository.findAll();
        assertThat(systemUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSystemUsers() throws Exception {
        // Initialize the database
        systemUserRepository.saveAndFlush(systemUser);

        // Get all the systemUserList
        restSystemUserMockMvc.perform(get("/api/system-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemUser.getId().intValue())));
    }

    @Test
    @Transactional
    public void getSystemUser() throws Exception {
        // Initialize the database
        systemUserRepository.saveAndFlush(systemUser);

        // Get the systemUser
        restSystemUserMockMvc.perform(get("/api/system-users/{id}", systemUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(systemUser.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSystemUser() throws Exception {
        // Get the systemUser
        restSystemUserMockMvc.perform(get("/api/system-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSystemUser() throws Exception {
        // Initialize the database
        systemUserRepository.saveAndFlush(systemUser);
        systemUserSearchRepository.save(systemUser);
        int databaseSizeBeforeUpdate = systemUserRepository.findAll().size();

        // Update the systemUser
        SystemUser updatedSystemUser = systemUserRepository.findOne(systemUser.getId());

        restSystemUserMockMvc.perform(put("/api/system-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSystemUser)))
            .andExpect(status().isOk());

        // Validate the SystemUser in the database
        List<SystemUser> systemUserList = systemUserRepository.findAll();
        assertThat(systemUserList).hasSize(databaseSizeBeforeUpdate);
        SystemUser testSystemUser = systemUserList.get(systemUserList.size() - 1);

        // Validate the SystemUser in Elasticsearch
        SystemUser systemUserEs = systemUserSearchRepository.findOne(testSystemUser.getId());
        assertThat(systemUserEs).isEqualToComparingFieldByField(testSystemUser);
    }

    @Test
    @Transactional
    public void updateNonExistingSystemUser() throws Exception {
        int databaseSizeBeforeUpdate = systemUserRepository.findAll().size();

        // Create the SystemUser

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSystemUserMockMvc.perform(put("/api/system-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemUser)))
            .andExpect(status().isCreated());

        // Validate the SystemUser in the database
        List<SystemUser> systemUserList = systemUserRepository.findAll();
        assertThat(systemUserList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSystemUser() throws Exception {
        // Initialize the database
        systemUserRepository.saveAndFlush(systemUser);
        systemUserSearchRepository.save(systemUser);
        int databaseSizeBeforeDelete = systemUserRepository.findAll().size();

        // Get the systemUser
        restSystemUserMockMvc.perform(delete("/api/system-users/{id}", systemUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean systemUserExistsInEs = systemUserSearchRepository.exists(systemUser.getId());
        assertThat(systemUserExistsInEs).isFalse();

        // Validate the database is empty
        List<SystemUser> systemUserList = systemUserRepository.findAll();
        assertThat(systemUserList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSystemUser() throws Exception {
        // Initialize the database
        systemUserRepository.saveAndFlush(systemUser);
        systemUserSearchRepository.save(systemUser);

        // Search the systemUser
        restSystemUserMockMvc.perform(get("/api/_search/system-users?query=id:" + systemUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemUser.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemUser.class);
        SystemUser systemUser1 = new SystemUser();
        systemUser1.setId(1L);
        SystemUser systemUser2 = new SystemUser();
        systemUser2.setId(systemUser1.getId());
        assertThat(systemUser1).isEqualTo(systemUser2);
        systemUser2.setId(2L);
        assertThat(systemUser1).isNotEqualTo(systemUser2);
        systemUser1.setId(null);
        assertThat(systemUser1).isNotEqualTo(systemUser2);
    }
}
