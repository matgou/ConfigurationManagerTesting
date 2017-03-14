package info.kapable.utils.configurationmanager.reporting.web.rest;

import info.kapable.utils.configurationmanager.reporting.ConfigurationManagerReportingApp;

import info.kapable.utils.configurationmanager.reporting.domain.Process;
import info.kapable.utils.configurationmanager.reporting.repository.ProcessRepository;
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
 * Test class for the ProcessResource REST controller.
 *
 * @see ProcessResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConfigurationManagerReportingApp.class)
public class ProcessResourceIntTest {

    private static final String DEFAULT_PROCESS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROCESS_NAME = "BBBBBBBBBB";

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProcessMockMvc;

    private Process process;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProcessResource processResource = new ProcessResource(processRepository);
        this.restProcessMockMvc = MockMvcBuilders.standaloneSetup(processResource)
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
    public static Process createEntity(EntityManager em) {
        Process process = new Process()
            .processName(DEFAULT_PROCESS_NAME);
        return process;
    }

    @Before
    public void initTest() {
        process = createEntity(em);
    }

    @Test
    @Transactional
    public void createProcess() throws Exception {
        int databaseSizeBeforeCreate = processRepository.findAll().size();

        // Create the Process
        restProcessMockMvc.perform(post("/api/processes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(process)))
            .andExpect(status().isCreated());

        // Validate the Process in the database
        List<Process> processList = processRepository.findAll();
        assertThat(processList).hasSize(databaseSizeBeforeCreate + 1);
        Process testProcess = processList.get(processList.size() - 1);
        assertThat(testProcess.getProcessName()).isEqualTo(DEFAULT_PROCESS_NAME);
    }

    @Test
    @Transactional
    public void createProcessWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = processRepository.findAll().size();

        // Create the Process with an existing ID
        process.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProcessMockMvc.perform(post("/api/processes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(process)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Process> processList = processRepository.findAll();
        assertThat(processList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllProcesses() throws Exception {
        // Initialize the database
        processRepository.saveAndFlush(process);

        // Get all the processList
        restProcessMockMvc.perform(get("/api/processes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(process.getId().intValue())))
            .andExpect(jsonPath("$.[*].processName").value(hasItem(DEFAULT_PROCESS_NAME.toString())));
    }

    @Test
    @Transactional
    public void getProcess() throws Exception {
        // Initialize the database
        processRepository.saveAndFlush(process);

        // Get the process
        restProcessMockMvc.perform(get("/api/processes/{id}", process.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(process.getId().intValue()))
            .andExpect(jsonPath("$.processName").value(DEFAULT_PROCESS_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProcess() throws Exception {
        // Get the process
        restProcessMockMvc.perform(get("/api/processes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProcess() throws Exception {
        // Initialize the database
        processRepository.saveAndFlush(process);
        int databaseSizeBeforeUpdate = processRepository.findAll().size();

        // Update the process
        Process updatedProcess = processRepository.findOne(process.getId());
        updatedProcess
            .processName(UPDATED_PROCESS_NAME);

        restProcessMockMvc.perform(put("/api/processes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProcess)))
            .andExpect(status().isOk());

        // Validate the Process in the database
        List<Process> processList = processRepository.findAll();
        assertThat(processList).hasSize(databaseSizeBeforeUpdate);
        Process testProcess = processList.get(processList.size() - 1);
        assertThat(testProcess.getProcessName()).isEqualTo(UPDATED_PROCESS_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingProcess() throws Exception {
        int databaseSizeBeforeUpdate = processRepository.findAll().size();

        // Create the Process

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProcessMockMvc.perform(put("/api/processes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(process)))
            .andExpect(status().isCreated());

        // Validate the Process in the database
        List<Process> processList = processRepository.findAll();
        assertThat(processList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProcess() throws Exception {
        // Initialize the database
        processRepository.saveAndFlush(process);
        int databaseSizeBeforeDelete = processRepository.findAll().size();

        // Get the process
        restProcessMockMvc.perform(delete("/api/processes/{id}", process.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Process> processList = processRepository.findAll();
        assertThat(processList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Process.class);
    }
}
