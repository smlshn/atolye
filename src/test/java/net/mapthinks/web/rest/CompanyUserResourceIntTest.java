package net.mapthinks.web.rest;

import net.mapthinks.AtolyeApp;

import net.mapthinks.domain.CompanyUser;
import net.mapthinks.repository.CompanyUserRepository;
import net.mapthinks.repository.search.CompanyUserSearchRepository;
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
 * Test class for the CompanyUserResource REST controller.
 *
 * @see CompanyUserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AtolyeApp.class)
public class CompanyUserResourceIntTest {

    @Autowired
    private CompanyUserRepository companyUserRepository;

    @Autowired
    private CompanyUserSearchRepository companyUserSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCompanyUserMockMvc;

    private CompanyUser companyUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CompanyUserResource companyUserResource = new CompanyUserResource(companyUserRepository, companyUserSearchRepository);
        this.restCompanyUserMockMvc = MockMvcBuilders.standaloneSetup(companyUserResource)
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
    public static CompanyUser createEntity(EntityManager em) {
        CompanyUser companyUser = new CompanyUser();
        return companyUser;
    }

    @Before
    public void initTest() {
        companyUserSearchRepository.deleteAll();
        companyUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createCompanyUser() throws Exception {
        int databaseSizeBeforeCreate = companyUserRepository.findAll().size();

        // Create the CompanyUser
        restCompanyUserMockMvc.perform(post("/api/company-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyUser)))
            .andExpect(status().isCreated());

        // Validate the CompanyUser in the database
        List<CompanyUser> companyUserList = companyUserRepository.findAll();
        assertThat(companyUserList).hasSize(databaseSizeBeforeCreate + 1);
        CompanyUser testCompanyUser = companyUserList.get(companyUserList.size() - 1);

        // Validate the CompanyUser in Elasticsearch
        CompanyUser companyUserEs = companyUserSearchRepository.findOne(testCompanyUser.getId());
        assertThat(companyUserEs).isEqualToComparingFieldByField(testCompanyUser);
    }

    @Test
    @Transactional
    public void createCompanyUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = companyUserRepository.findAll().size();

        // Create the CompanyUser with an existing ID
        companyUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanyUserMockMvc.perform(post("/api/company-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyUser)))
            .andExpect(status().isBadRequest());

        // Validate the CompanyUser in the database
        List<CompanyUser> companyUserList = companyUserRepository.findAll();
        assertThat(companyUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCompanyUsers() throws Exception {
        // Initialize the database
        companyUserRepository.saveAndFlush(companyUser);

        // Get all the companyUserList
        restCompanyUserMockMvc.perform(get("/api/company-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companyUser.getId().intValue())));
    }

    @Test
    @Transactional
    public void getCompanyUser() throws Exception {
        // Initialize the database
        companyUserRepository.saveAndFlush(companyUser);

        // Get the companyUser
        restCompanyUserMockMvc.perform(get("/api/company-users/{id}", companyUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(companyUser.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCompanyUser() throws Exception {
        // Get the companyUser
        restCompanyUserMockMvc.perform(get("/api/company-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompanyUser() throws Exception {
        // Initialize the database
        companyUserRepository.saveAndFlush(companyUser);
        companyUserSearchRepository.save(companyUser);
        int databaseSizeBeforeUpdate = companyUserRepository.findAll().size();

        // Update the companyUser
        CompanyUser updatedCompanyUser = companyUserRepository.findOne(companyUser.getId());

        restCompanyUserMockMvc.perform(put("/api/company-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCompanyUser)))
            .andExpect(status().isOk());

        // Validate the CompanyUser in the database
        List<CompanyUser> companyUserList = companyUserRepository.findAll();
        assertThat(companyUserList).hasSize(databaseSizeBeforeUpdate);
        CompanyUser testCompanyUser = companyUserList.get(companyUserList.size() - 1);

        // Validate the CompanyUser in Elasticsearch
        CompanyUser companyUserEs = companyUserSearchRepository.findOne(testCompanyUser.getId());
        assertThat(companyUserEs).isEqualToComparingFieldByField(testCompanyUser);
    }

    @Test
    @Transactional
    public void updateNonExistingCompanyUser() throws Exception {
        int databaseSizeBeforeUpdate = companyUserRepository.findAll().size();

        // Create the CompanyUser

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCompanyUserMockMvc.perform(put("/api/company-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyUser)))
            .andExpect(status().isCreated());

        // Validate the CompanyUser in the database
        List<CompanyUser> companyUserList = companyUserRepository.findAll();
        assertThat(companyUserList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCompanyUser() throws Exception {
        // Initialize the database
        companyUserRepository.saveAndFlush(companyUser);
        companyUserSearchRepository.save(companyUser);
        int databaseSizeBeforeDelete = companyUserRepository.findAll().size();

        // Get the companyUser
        restCompanyUserMockMvc.perform(delete("/api/company-users/{id}", companyUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean companyUserExistsInEs = companyUserSearchRepository.exists(companyUser.getId());
        assertThat(companyUserExistsInEs).isFalse();

        // Validate the database is empty
        List<CompanyUser> companyUserList = companyUserRepository.findAll();
        assertThat(companyUserList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCompanyUser() throws Exception {
        // Initialize the database
        companyUserRepository.saveAndFlush(companyUser);
        companyUserSearchRepository.save(companyUser);

        // Search the companyUser
        restCompanyUserMockMvc.perform(get("/api/_search/company-users?query=id:" + companyUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companyUser.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompanyUser.class);
        CompanyUser companyUser1 = new CompanyUser();
        companyUser1.setId(1L);
        CompanyUser companyUser2 = new CompanyUser();
        companyUser2.setId(companyUser1.getId());
        assertThat(companyUser1).isEqualTo(companyUser2);
        companyUser2.setId(2L);
        assertThat(companyUser1).isNotEqualTo(companyUser2);
        companyUser1.setId(null);
        assertThat(companyUser1).isNotEqualTo(companyUser2);
    }
}
