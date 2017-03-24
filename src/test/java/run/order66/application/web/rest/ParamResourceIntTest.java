package run.order66.application.web.rest;

import run.order66.application.Order66App;

import run.order66.application.domain.Param;
import run.order66.application.repository.ParamRepository;
import run.order66.application.service.ParamService;
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

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ParamResource REST controller.
 *
 * @see ParamResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Order66App.class)
public class ParamResourceIntTest {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    @Autowired
    private ParamRepository paramRepository;

    @Autowired
    private ParamService paramService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restParamMockMvc;

    private Param param;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ParamResource paramResource = new ParamResource(paramService);
        this.restParamMockMvc = MockMvcBuilders.standaloneSetup(paramResource)
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
    public static Param createEntity(EntityManager em) {
        Param param = new Param()
            .key(DEFAULT_KEY)
            .value(DEFAULT_VALUE);
        return param;
    }

    @Before
    public void initTest() {
        param = createEntity(em);
    }

    @Test
    @Transactional
    public void createParam() throws Exception {
        int databaseSizeBeforeCreate = paramRepository.findAll().size();

        // Create the Param
        restParamMockMvc.perform(post("/api/params")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(param)))
            .andExpect(status().isCreated());

        // Validate the Param in the database
        List<Param> paramList = paramRepository.findAll();
        assertThat(paramList).hasSize(databaseSizeBeforeCreate + 1);
        Param testParam = paramList.get(paramList.size() - 1);
        assertThat(testParam.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testParam.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createParamWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paramRepository.findAll().size();

        // Create the Param with an existing ID
        param.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restParamMockMvc.perform(post("/api/params")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(param)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Param> paramList = paramRepository.findAll();
        assertThat(paramList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = paramRepository.findAll().size();
        // set the field null
        param.setKey(null);

        // Create the Param, which fails.

        restParamMockMvc.perform(post("/api/params")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(param)))
            .andExpect(status().isBadRequest());

        List<Param> paramList = paramRepository.findAll();
        assertThat(paramList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllParams() throws Exception {
        // Initialize the database
        paramRepository.saveAndFlush(param);

        // Get all the paramList
        restParamMockMvc.perform(get("/api/params?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(param.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    @Test
    @Transactional
    public void getParam() throws Exception {
        // Initialize the database
        paramRepository.saveAndFlush(param);

        // Get the param
        restParamMockMvc.perform(get("/api/params/{id}", param.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(param.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingParam() throws Exception {
        // Get the param
        restParamMockMvc.perform(get("/api/params/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParam() throws Exception {
        // Initialize the database
        paramService.save(param);

        int databaseSizeBeforeUpdate = paramRepository.findAll().size();

        // Update the param
        Param updatedParam = paramRepository.findOne(param.getId());
        updatedParam
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE);

        restParamMockMvc.perform(put("/api/params")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedParam)))
            .andExpect(status().isOk());

        // Validate the Param in the database
        List<Param> paramList = paramRepository.findAll();
        assertThat(paramList).hasSize(databaseSizeBeforeUpdate);
        Param testParam = paramList.get(paramList.size() - 1);
        assertThat(testParam.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testParam.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingParam() throws Exception {
        int databaseSizeBeforeUpdate = paramRepository.findAll().size();

        // Create the Param

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restParamMockMvc.perform(put("/api/params")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(param)))
            .andExpect(status().isCreated());

        // Validate the Param in the database
        List<Param> paramList = paramRepository.findAll();
        assertThat(paramList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteParam() throws Exception {
        // Initialize the database
        paramService.save(param);

        int databaseSizeBeforeDelete = paramRepository.findAll().size();

        // Get the param
        restParamMockMvc.perform(delete("/api/params/{id}", param.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Param> paramList = paramRepository.findAll();
        assertThat(paramList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Param.class);
    }
}
