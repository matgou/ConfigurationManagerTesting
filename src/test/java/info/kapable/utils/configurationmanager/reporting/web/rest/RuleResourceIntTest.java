package info.kapable.utils.configurationmanager.reporting.web.rest;

import info.kapable.utils.configurationmanager.reporting.ConfigurationManagerReportingApp;
import info.kapable.utils.configurationmanager.reporting.domain.Rule;
import info.kapable.utils.configurationmanager.reporting.domain.RuleReport;
import info.kapable.utils.configurationmanager.reporting.domain.enumeration.StatusEnum;
import info.kapable.utils.configurationmanager.reporting.repository.RuleReportRepository;
import info.kapable.utils.configurationmanager.reporting.repository.RuleRepository;
import info.kapable.utils.configurationmanager.reporting.service.RuleService;
import info.kapable.utils.configurationmanager.reporting.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RuleResource REST controller.
 *
 * @see RuleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConfigurationManagerReportingApp.class)
public class RuleResourceIntTest {

    private static final String DEFAULT_RULE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_RULE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_RULE_ARGS = "AAAAAAAAAA";
    private static final String UPDATED_RULE_ARGS = "BBBBBBBBBB";

    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private RuleReportRepository ruleReportRepository;

    @Autowired
    private RuleService ruleService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRuleMockMvc;

    private Rule rule;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RuleResource ruleResource = new RuleResource(ruleService);
        this.restRuleMockMvc = MockMvcBuilders.standaloneSetup(ruleResource)
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
    public static Rule createEntity(EntityManager em) {
        Rule rule = new Rule()
            .ruleName(DEFAULT_RULE_NAME)
            .ruleArgs(DEFAULT_RULE_ARGS);
        return rule;
    }

    @Before
    public void initTest() {
        rule = createEntity(em);
    }

    @Test
    @Transactional
    public void createRule() throws Exception {
        int databaseSizeBeforeCreate = ruleRepository.findAll().size();

        // Create the Rule
        restRuleMockMvc.perform(post("/api/rules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rule)))
            .andExpect(status().isCreated());

        // Validate the Rule in the database
        List<Rule> ruleList = ruleRepository.findAll();
        assertThat(ruleList).hasSize(databaseSizeBeforeCreate + 1);
        Rule testRule = ruleList.get(ruleList.size() - 1);
        assertThat(testRule.getRuleName()).isEqualTo(DEFAULT_RULE_NAME);
        assertThat(testRule.getRuleArgs()).isEqualTo(DEFAULT_RULE_ARGS);
    }

    @Test
    @Transactional
    public void createRuleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ruleRepository.findAll().size();

        // Create the Rule with an existing ID
        rule.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRuleMockMvc.perform(post("/api/rules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rule)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Rule> ruleList = ruleRepository.findAll();
        assertThat(ruleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRules() throws Exception {
        // Initialize the database
        ruleRepository.saveAndFlush(rule);

        // Get all the ruleList
        restRuleMockMvc.perform(get("/api/rules?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rule.getId().intValue())))
            .andExpect(jsonPath("$.[*].ruleName").value(hasItem(DEFAULT_RULE_NAME.toString())))
            .andExpect(jsonPath("$.[*].ruleArgs").value(hasItem(DEFAULT_RULE_ARGS.toString())));
    }

    @Test
    @Transactional
    public void getRule() throws Exception {
        // Initialize the database
        ruleRepository.saveAndFlush(rule);

        // Get the rule
        restRuleMockMvc.perform(get("/api/rules/{id}", rule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rule.getId().intValue()))
            .andExpect(jsonPath("$.ruleName").value(DEFAULT_RULE_NAME.toString()))
            .andExpect(jsonPath("$.ruleArgs").value(DEFAULT_RULE_ARGS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRule() throws Exception {
        // Get the rule
        restRuleMockMvc.perform(get("/api/rules/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRule() throws Exception {
        // Initialize the database
        ruleService.save(rule);

        int databaseSizeBeforeUpdate = ruleRepository.findAll().size();

        // Update the rule
        Rule updatedRule = ruleRepository.findOne(rule.getId());
        updatedRule
            .ruleName(UPDATED_RULE_NAME)
            .ruleArgs(UPDATED_RULE_ARGS);

        restRuleMockMvc.perform(put("/api/rules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRule)))
            .andExpect(status().isOk());

        // Validate the Rule in the database
        List<Rule> ruleList = ruleRepository.findAll();
        assertThat(ruleList).hasSize(databaseSizeBeforeUpdate);
        Rule testRule = ruleList.get(ruleList.size() - 1);
        assertThat(testRule.getRuleName()).isEqualTo(UPDATED_RULE_NAME);
        assertThat(testRule.getRuleArgs()).isEqualTo(UPDATED_RULE_ARGS);
    }

    @Test
    @Transactional
    public void updateNonExistingRule() throws Exception {
        int databaseSizeBeforeUpdate = ruleRepository.findAll().size();

        // Create the Rule

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRuleMockMvc.perform(put("/api/rules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rule)))
            .andExpect(status().isCreated());

        // Validate the Rule in the database
        List<Rule> ruleList = ruleRepository.findAll();
        assertThat(ruleList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRule() throws Exception {
        // Initialize the database
        ruleService.save(rule);

        int databaseSizeBeforeDelete = ruleRepository.findAll().size();

        // Get the rule
        restRuleMockMvc.perform(delete("/api/rules/{id}", rule.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Rule> ruleList = ruleRepository.findAll();
        assertThat(ruleList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rule.class);
    }
    


    @Test
    @Transactional
    public void testGetRulesExecutions() throws Exception {
        final Calendar cal = Calendar.getInstance();
        LocalDate today = cal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        cal.add(Calendar.DATE, -1);
        LocalDate yesterday = cal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        
    	Rule rule1 = new Rule()
    		.ruleName(DEFAULT_RULE_NAME)
    		.ruleArgs(DEFAULT_RULE_ARGS);
        ruleRepository.save(rule1);
        Rule rule2 = new Rule()
        	.ruleName(DEFAULT_RULE_NAME)
        	.ruleArgs(DEFAULT_RULE_ARGS);
        ruleRepository.save(rule2);
        
        RuleReport rule1Report1 = new RuleReport();
        rule1Report1.setReportDate(yesterday);
        rule1Report1.setRule(rule1);
        rule1Report1.setStatus(StatusEnum.HardFail);
        ruleReportRepository.save(rule1Report1);
        RuleReport rule1Report2 = new RuleReport();
        rule1Report2.setReportDate(today);
        rule1Report2.setRule(rule1);
        rule1Report2.setStatus(StatusEnum.Success);
        ruleReportRepository.save(rule1Report2);

        RuleReport rule2Report1 = new RuleReport();
        rule2Report1.setReportDate(yesterday);
        rule2Report1.setRule(rule1);
        rule2Report1.setStatus(StatusEnum.Success);
        ruleReportRepository.save(rule1Report1);
        RuleReport rule2Report2 = new RuleReport();
        rule2Report2.setReportDate(today);
        rule2Report2.setRule(rule2);
        rule2Report2.setStatus(StatusEnum.HardFail);
        ruleReportRepository.save(rule2Report2);
    }
}
