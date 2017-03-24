package run.order66.application.web.rest;

import run.order66.application.Order66App;

import run.order66.application.domain.RuleReport;
import run.order66.application.repository.RuleReportRepository;
import run.order66.application.service.RuleReportService;
import run.order66.application.web.rest.errors.ExceptionTranslator;

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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static run.order66.application.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import run.order66.application.domain.enumeration.StatusEnum;
/**
 * Test class for the RuleReportResource REST controller.
 *
 * @see RuleReportResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Order66App.class)
public class RuleReportResourceIntTest {

    private static final LocalDate DEFAULT_REPORT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REPORT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final StatusEnum DEFAULT_STATUS = StatusEnum.Unknown;
    private static final StatusEnum UPDATED_STATUS = StatusEnum.Success;

    private static final String DEFAULT_LOG = "AAAAAAAAAA";
    private static final String UPDATED_LOG = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_SUBMIT_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_SUBMIT_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_FINISH_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FINISH_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private RuleReportRepository ruleReportRepository;

    @Autowired
    private RuleReportService ruleReportService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRuleReportMockMvc;

    private RuleReport ruleReport;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RuleReportResource ruleReportResource = new RuleReportResource(ruleReportService);
        this.restRuleReportMockMvc = MockMvcBuilders.standaloneSetup(ruleReportResource)
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
    public static RuleReport createEntity(EntityManager em) {
        RuleReport ruleReport = new RuleReport()
            .reportDate(DEFAULT_REPORT_DATE)
            .status(DEFAULT_STATUS)
            .log(DEFAULT_LOG)
            .submitAt(DEFAULT_SUBMIT_AT)
            .finishAt(DEFAULT_FINISH_AT);
        return ruleReport;
    }

    @Before
    public void initTest() {
        ruleReport = createEntity(em);
    }

    @Test
    @Transactional
    public void createRuleReport() throws Exception {
        int databaseSizeBeforeCreate = ruleReportRepository.findAll().size();

        // Create the RuleReport
        restRuleReportMockMvc.perform(post("/api/rule-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ruleReport)))
            .andExpect(status().isCreated());

        // Validate the RuleReport in the database
        List<RuleReport> ruleReportList = ruleReportRepository.findAll();
        assertThat(ruleReportList).hasSize(databaseSizeBeforeCreate + 1);
        RuleReport testRuleReport = ruleReportList.get(ruleReportList.size() - 1);
        assertThat(testRuleReport.getReportDate()).isEqualTo(DEFAULT_REPORT_DATE);
        assertThat(testRuleReport.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testRuleReport.getLog()).isEqualTo(DEFAULT_LOG);
        assertThat(testRuleReport.getSubmitAt()).isEqualTo(DEFAULT_SUBMIT_AT);
        assertThat(testRuleReport.getFinishAt()).isEqualTo(DEFAULT_FINISH_AT);
    }

    @Test
    @Transactional
    public void createRuleReportWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ruleReportRepository.findAll().size();

        // Create the RuleReport with an existing ID
        ruleReport.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRuleReportMockMvc.perform(post("/api/rule-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ruleReport)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<RuleReport> ruleReportList = ruleReportRepository.findAll();
        assertThat(ruleReportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRuleReports() throws Exception {
        // Initialize the database
        ruleReportRepository.saveAndFlush(ruleReport);

        // Get all the ruleReportList
        restRuleReportMockMvc.perform(get("/api/rule-reports?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ruleReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportDate").value(hasItem(DEFAULT_REPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].log").value(hasItem(DEFAULT_LOG.toString())))
            .andExpect(jsonPath("$.[*].submitAt").value(hasItem(sameInstant(DEFAULT_SUBMIT_AT))))
            .andExpect(jsonPath("$.[*].finishAt").value(hasItem(sameInstant(DEFAULT_FINISH_AT))));
    }

    @Test
    @Transactional
    public void getRuleReport() throws Exception {
        // Initialize the database
        ruleReportRepository.saveAndFlush(ruleReport);

        // Get the ruleReport
        restRuleReportMockMvc.perform(get("/api/rule-reports/{id}", ruleReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ruleReport.getId().intValue()))
            .andExpect(jsonPath("$.reportDate").value(DEFAULT_REPORT_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.log").value(DEFAULT_LOG.toString()))
            .andExpect(jsonPath("$.submitAt").value(sameInstant(DEFAULT_SUBMIT_AT)))
            .andExpect(jsonPath("$.finishAt").value(sameInstant(DEFAULT_FINISH_AT)));
    }

    @Test
    @Transactional
    public void getNonExistingRuleReport() throws Exception {
        // Get the ruleReport
        restRuleReportMockMvc.perform(get("/api/rule-reports/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRuleReport() throws Exception {
        // Initialize the database
        ruleReportService.save(ruleReport);

        int databaseSizeBeforeUpdate = ruleReportRepository.findAll().size();

        // Update the ruleReport
        RuleReport updatedRuleReport = ruleReportRepository.findOne(ruleReport.getId());
        updatedRuleReport
            .reportDate(UPDATED_REPORT_DATE)
            .status(UPDATED_STATUS)
            .log(UPDATED_LOG)
            .submitAt(UPDATED_SUBMIT_AT)
            .finishAt(UPDATED_FINISH_AT);

        restRuleReportMockMvc.perform(put("/api/rule-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRuleReport)))
            .andExpect(status().isOk());

        // Validate the RuleReport in the database
        List<RuleReport> ruleReportList = ruleReportRepository.findAll();
        assertThat(ruleReportList).hasSize(databaseSizeBeforeUpdate);
        RuleReport testRuleReport = ruleReportList.get(ruleReportList.size() - 1);
        assertThat(testRuleReport.getReportDate()).isEqualTo(UPDATED_REPORT_DATE);
        assertThat(testRuleReport.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRuleReport.getLog()).isEqualTo(UPDATED_LOG);
        assertThat(testRuleReport.getSubmitAt()).isEqualTo(UPDATED_SUBMIT_AT);
        assertThat(testRuleReport.getFinishAt()).isEqualTo(UPDATED_FINISH_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingRuleReport() throws Exception {
        int databaseSizeBeforeUpdate = ruleReportRepository.findAll().size();

        // Create the RuleReport

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRuleReportMockMvc.perform(put("/api/rule-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ruleReport)))
            .andExpect(status().isCreated());

        // Validate the RuleReport in the database
        List<RuleReport> ruleReportList = ruleReportRepository.findAll();
        assertThat(ruleReportList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRuleReport() throws Exception {
        // Initialize the database
        ruleReportService.save(ruleReport);

        int databaseSizeBeforeDelete = ruleReportRepository.findAll().size();

        // Get the ruleReport
        restRuleReportMockMvc.perform(delete("/api/rule-reports/{id}", ruleReport.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RuleReport> ruleReportList = ruleReportRepository.findAll();
        assertThat(ruleReportList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RuleReport.class);
    }
}
