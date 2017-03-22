package info.kapable.utils.configurationmanager.reporting.web.rest;

import info.kapable.utils.configurationmanager.reporting.ConfigurationManagerReportingApp;

import info.kapable.utils.configurationmanager.reporting.domain.RuleType;
import info.kapable.utils.configurationmanager.reporting.repository.RuleTypeRepository;
import info.kapable.utils.configurationmanager.reporting.service.RuleTypeService;
import info.kapable.utils.configurationmanager.reporting.web.rest.errors.ExceptionTranslator;

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
 * Test class for the RuleTypeResource REST controller.
 *
 * @see RuleTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConfigurationManagerReportingApp.class)
public class RuleTypeResourceIntTest {

    private static final String DEFAULT_RULE_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_RULE_TYPE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CHECKER_BEAN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CHECKER_BEAN_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_REQUIRED_ARGUMENTS_LIST = "AAAAAAAAAA";
    private static final String UPDATED_REQUIRED_ARGUMENTS_LIST = "BBBBBBBBBB";

    private static final String DEFAULT_REPORTING_BEAN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_REPORTING_BEAN_NAME = "BBBBBBBBBB";

    @Autowired
    private RuleTypeRepository ruleTypeRepository;

    @Autowired
    private RuleTypeService ruleTypeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRuleTypeMockMvc;

    private RuleType ruleType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RuleTypeResource ruleTypeResource = new RuleTypeResource(ruleTypeService);
        this.restRuleTypeMockMvc = MockMvcBuilders.standaloneSetup(ruleTypeResource)
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
    public static RuleType createEntity(EntityManager em) {
        RuleType ruleType = new RuleType()
            .ruleTypeName(DEFAULT_RULE_TYPE_NAME)
            .checkerBeanName(DEFAULT_CHECKER_BEAN_NAME)
            .description(DEFAULT_DESCRIPTION)
            .requiredArgumentsList(DEFAULT_REQUIRED_ARGUMENTS_LIST)
            .reportingBeanName(DEFAULT_REPORTING_BEAN_NAME);
        return ruleType;
    }

    @Before
    public void initTest() {
        ruleType = createEntity(em);
    }

    @Test
    @Transactional
    public void createRuleType() throws Exception {
        int databaseSizeBeforeCreate = ruleTypeRepository.findAll().size();

        // Create the RuleType
        restRuleTypeMockMvc.perform(post("/api/rule-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ruleType)))
            .andExpect(status().isCreated());

        // Validate the RuleType in the database
        List<RuleType> ruleTypeList = ruleTypeRepository.findAll();
        assertThat(ruleTypeList).hasSize(databaseSizeBeforeCreate + 1);
        RuleType testRuleType = ruleTypeList.get(ruleTypeList.size() - 1);
        assertThat(testRuleType.getRuleTypeName()).isEqualTo(DEFAULT_RULE_TYPE_NAME);
        assertThat(testRuleType.getCheckerBeanName()).isEqualTo(DEFAULT_CHECKER_BEAN_NAME);
        assertThat(testRuleType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRuleType.getRequiredArgumentsList()).isEqualTo(DEFAULT_REQUIRED_ARGUMENTS_LIST);
        assertThat(testRuleType.getReportingBeanName()).isEqualTo(DEFAULT_REPORTING_BEAN_NAME);
    }

    @Test
    @Transactional
    public void createRuleTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ruleTypeRepository.findAll().size();

        // Create the RuleType with an existing ID
        ruleType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRuleTypeMockMvc.perform(post("/api/rule-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ruleType)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<RuleType> ruleTypeList = ruleTypeRepository.findAll();
        assertThat(ruleTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRuleTypes() throws Exception {
        // Initialize the database
        ruleTypeRepository.saveAndFlush(ruleType);

        // Get all the ruleTypeList
        restRuleTypeMockMvc.perform(get("/api/rule-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ruleType.getId().intValue())))
            .andExpect(jsonPath("$.[*].ruleTypeName").value(hasItem(DEFAULT_RULE_TYPE_NAME.toString())))
            .andExpect(jsonPath("$.[*].checkerBeanName").value(hasItem(DEFAULT_CHECKER_BEAN_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].requiredArgumentsList").value(hasItem(DEFAULT_REQUIRED_ARGUMENTS_LIST.toString())))
            .andExpect(jsonPath("$.[*].reportingBeanName").value(hasItem(DEFAULT_REPORTING_BEAN_NAME.toString())));
    }

    @Test
    @Transactional
    public void getRuleType() throws Exception {
        // Initialize the database
        ruleTypeRepository.saveAndFlush(ruleType);

        // Get the ruleType
        restRuleTypeMockMvc.perform(get("/api/rule-types/{id}", ruleType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ruleType.getId().intValue()))
            .andExpect(jsonPath("$.ruleTypeName").value(DEFAULT_RULE_TYPE_NAME.toString()))
            .andExpect(jsonPath("$.checkerBeanName").value(DEFAULT_CHECKER_BEAN_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.requiredArgumentsList").value(DEFAULT_REQUIRED_ARGUMENTS_LIST.toString()))
            .andExpect(jsonPath("$.reportingBeanName").value(DEFAULT_REPORTING_BEAN_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRuleType() throws Exception {
        // Get the ruleType
        restRuleTypeMockMvc.perform(get("/api/rule-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRuleType() throws Exception {
        // Initialize the database
        ruleTypeService.save(ruleType);

        int databaseSizeBeforeUpdate = ruleTypeRepository.findAll().size();

        // Update the ruleType
        RuleType updatedRuleType = ruleTypeRepository.findOne(ruleType.getId());
        updatedRuleType
            .ruleTypeName(UPDATED_RULE_TYPE_NAME)
            .checkerBeanName(UPDATED_CHECKER_BEAN_NAME)
            .description(UPDATED_DESCRIPTION)
            .requiredArgumentsList(UPDATED_REQUIRED_ARGUMENTS_LIST)
            .reportingBeanName(UPDATED_REPORTING_BEAN_NAME);

        restRuleTypeMockMvc.perform(put("/api/rule-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRuleType)))
            .andExpect(status().isOk());

        // Validate the RuleType in the database
        List<RuleType> ruleTypeList = ruleTypeRepository.findAll();
        assertThat(ruleTypeList).hasSize(databaseSizeBeforeUpdate);
        RuleType testRuleType = ruleTypeList.get(ruleTypeList.size() - 1);
        assertThat(testRuleType.getRuleTypeName()).isEqualTo(UPDATED_RULE_TYPE_NAME);
        assertThat(testRuleType.getCheckerBeanName()).isEqualTo(UPDATED_CHECKER_BEAN_NAME);
        assertThat(testRuleType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRuleType.getRequiredArgumentsList()).isEqualTo(UPDATED_REQUIRED_ARGUMENTS_LIST);
        assertThat(testRuleType.getReportingBeanName()).isEqualTo(UPDATED_REPORTING_BEAN_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingRuleType() throws Exception {
        int databaseSizeBeforeUpdate = ruleTypeRepository.findAll().size();

        // Create the RuleType

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRuleTypeMockMvc.perform(put("/api/rule-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ruleType)))
            .andExpect(status().isCreated());

        // Validate the RuleType in the database
        List<RuleType> ruleTypeList = ruleTypeRepository.findAll();
        assertThat(ruleTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRuleType() throws Exception {
        // Initialize the database
        ruleTypeService.save(ruleType);

        int databaseSizeBeforeDelete = ruleTypeRepository.findAll().size();

        // Get the ruleType
        restRuleTypeMockMvc.perform(delete("/api/rule-types/{id}", ruleType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RuleType> ruleTypeList = ruleTypeRepository.findAll();
        assertThat(ruleTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RuleType.class);
    }
}
