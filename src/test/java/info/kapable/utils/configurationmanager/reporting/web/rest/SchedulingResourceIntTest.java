package info.kapable.utils.configurationmanager.reporting.web.rest;

import info.kapable.utils.configurationmanager.reporting.ConfigurationManagerReportingApp;

import info.kapable.utils.configurationmanager.reporting.domain.Scheduling;
import info.kapable.utils.configurationmanager.reporting.repository.SchedulingRepository;
import info.kapable.utils.configurationmanager.reporting.service.SchedulingService;
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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import info.kapable.utils.configurationmanager.reporting.domain.enumeration.TriggerEnum;
/**
 * Test class for the SchedulingResource REST controller.
 *
 * @see SchedulingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConfigurationManagerReportingApp.class)
public class SchedulingResourceIntTest {

    private static final TriggerEnum DEFAULT_TRIGGER = TriggerEnum.cronSchedule;
    private static final TriggerEnum UPDATED_TRIGGER = TriggerEnum.repeatForever;

    private static final String DEFAULT_RULE = "AAAAAAAAAA";
    private static final String UPDATED_RULE = "BBBBBBBBBB";

    private static final String DEFAULT_SCHEDULING_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_SCHEDULING_LABEL = "BBBBBBBBBB";

    @Autowired
    private SchedulingRepository schedulingRepository;

    @Autowired
    private SchedulingService schedulingService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSchedulingMockMvc;

    private Scheduling scheduling;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SchedulingResource schedulingResource = new SchedulingResource(schedulingService);
        this.restSchedulingMockMvc = MockMvcBuilders.standaloneSetup(schedulingResource)
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
    public static Scheduling createEntity(EntityManager em) {
        Scheduling scheduling = new Scheduling()
            .trigger(DEFAULT_TRIGGER)
            .rule(DEFAULT_RULE)
            .schedulingLabel(DEFAULT_SCHEDULING_LABEL);
        return scheduling;
    }

    @Before
    public void initTest() {
        scheduling = createEntity(em);
    }

    @Test
    @Transactional
    public void createScheduling() throws Exception {
        int databaseSizeBeforeCreate = schedulingRepository.findAll().size();

        // Create the Scheduling
        restSchedulingMockMvc.perform(post("/api/schedulings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scheduling)))
            .andExpect(status().isCreated());

        // Validate the Scheduling in the database
        List<Scheduling> schedulingList = schedulingRepository.findAll();
        assertThat(schedulingList).hasSize(databaseSizeBeforeCreate + 1);
        Scheduling testScheduling = schedulingList.get(schedulingList.size() - 1);
        assertThat(testScheduling.getTrigger()).isEqualTo(DEFAULT_TRIGGER);
        assertThat(testScheduling.getRule()).isEqualTo(DEFAULT_RULE);
        assertThat(testScheduling.getSchedulingLabel()).isEqualTo(DEFAULT_SCHEDULING_LABEL);
    }

    @Test
    @Transactional
    public void createSchedulingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = schedulingRepository.findAll().size();

        // Create the Scheduling with an existing ID
        scheduling.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSchedulingMockMvc.perform(post("/api/schedulings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scheduling)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Scheduling> schedulingList = schedulingRepository.findAll();
        assertThat(schedulingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSchedulings() throws Exception {
        // Initialize the database
        schedulingRepository.saveAndFlush(scheduling);

        // Get all the schedulingList
        restSchedulingMockMvc.perform(get("/api/schedulings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scheduling.getId().intValue())))
            .andExpect(jsonPath("$.[*].trigger").value(hasItem(DEFAULT_TRIGGER.toString())))
            .andExpect(jsonPath("$.[*].rule").value(hasItem(DEFAULT_RULE.toString())))
            .andExpect(jsonPath("$.[*].schedulingLabel").value(hasItem(DEFAULT_SCHEDULING_LABEL.toString())));
    }

    @Test
    @Transactional
    public void getScheduling() throws Exception {
        // Initialize the database
        schedulingRepository.saveAndFlush(scheduling);

        // Get the scheduling
        restSchedulingMockMvc.perform(get("/api/schedulings/{id}", scheduling.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(scheduling.getId().intValue()))
            .andExpect(jsonPath("$.trigger").value(DEFAULT_TRIGGER.toString()))
            .andExpect(jsonPath("$.rule").value(DEFAULT_RULE.toString()))
            .andExpect(jsonPath("$.schedulingLabel").value(DEFAULT_SCHEDULING_LABEL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingScheduling() throws Exception {
        // Get the scheduling
        restSchedulingMockMvc.perform(get("/api/schedulings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateScheduling() throws Exception {
        // Initialize the database
        schedulingService.save(scheduling);

        int databaseSizeBeforeUpdate = schedulingRepository.findAll().size();

        // Update the scheduling
        Scheduling updatedScheduling = schedulingRepository.findOne(scheduling.getId());
        updatedScheduling
            .trigger(UPDATED_TRIGGER)
            .rule(UPDATED_RULE)
            .schedulingLabel(UPDATED_SCHEDULING_LABEL);

        restSchedulingMockMvc.perform(put("/api/schedulings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedScheduling)))
            .andExpect(status().isOk());

        // Validate the Scheduling in the database
        List<Scheduling> schedulingList = schedulingRepository.findAll();
        assertThat(schedulingList).hasSize(databaseSizeBeforeUpdate);
        Scheduling testScheduling = schedulingList.get(schedulingList.size() - 1);
        assertThat(testScheduling.getTrigger()).isEqualTo(UPDATED_TRIGGER);
        assertThat(testScheduling.getRule()).isEqualTo(UPDATED_RULE);
        assertThat(testScheduling.getSchedulingLabel()).isEqualTo(UPDATED_SCHEDULING_LABEL);
    }

    @Test
    @Transactional
    public void updateNonExistingScheduling() throws Exception {
        int databaseSizeBeforeUpdate = schedulingRepository.findAll().size();

        // Create the Scheduling

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSchedulingMockMvc.perform(put("/api/schedulings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scheduling)))
            .andExpect(status().isCreated());

        // Validate the Scheduling in the database
        List<Scheduling> schedulingList = schedulingRepository.findAll();
        assertThat(schedulingList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteScheduling() throws Exception {
        // Initialize the database
        schedulingService.save(scheduling);

        int databaseSizeBeforeDelete = schedulingRepository.findAll().size();

        // Get the scheduling
        restSchedulingMockMvc.perform(delete("/api/schedulings/{id}", scheduling.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Scheduling> schedulingList = schedulingRepository.findAll();
        assertThat(schedulingList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Scheduling.class);
    }
}
