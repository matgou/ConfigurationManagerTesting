package run.order66.application.web.rest;

import run.order66.application.Order66App;

import run.order66.application.domain.RuleTag;
import run.order66.application.repository.RuleTagRepository;
import run.order66.application.service.RuleTagService;
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
 * Test class for the RuleTagResource REST controller.
 *
 * @see RuleTagResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Order66App.class)
public class RuleTagResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private RuleTagRepository ruleTagRepository;

    @Autowired
    private RuleTagService ruleTagService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRuleTagMockMvc;

    private RuleTag ruleTag;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RuleTagResource ruleTagResource = new RuleTagResource(ruleTagService);
        this.restRuleTagMockMvc = MockMvcBuilders.standaloneSetup(ruleTagResource)
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
    public static RuleTag createEntity(EntityManager em) {
        RuleTag ruleTag = new RuleTag()
            .name(DEFAULT_NAME);
        return ruleTag;
    }

    @Before
    public void initTest() {
        ruleTag = createEntity(em);
    }

    @Test
    @Transactional
    public void createRuleTag() throws Exception {
        int databaseSizeBeforeCreate = ruleTagRepository.findAll().size();

        // Create the RuleTag
        restRuleTagMockMvc.perform(post("/api/rule-tags")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ruleTag)))
            .andExpect(status().isCreated());

        // Validate the RuleTag in the database
        List<RuleTag> ruleTagList = ruleTagRepository.findAll();
        assertThat(ruleTagList).hasSize(databaseSizeBeforeCreate + 1);
        RuleTag testRuleTag = ruleTagList.get(ruleTagList.size() - 1);
        assertThat(testRuleTag.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createRuleTagWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ruleTagRepository.findAll().size();

        // Create the RuleTag with an existing ID
        ruleTag.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRuleTagMockMvc.perform(post("/api/rule-tags")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ruleTag)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<RuleTag> ruleTagList = ruleTagRepository.findAll();
        assertThat(ruleTagList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = ruleTagRepository.findAll().size();
        // set the field null
        ruleTag.setName(null);

        // Create the RuleTag, which fails.

        restRuleTagMockMvc.perform(post("/api/rule-tags")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ruleTag)))
            .andExpect(status().isBadRequest());

        List<RuleTag> ruleTagList = ruleTagRepository.findAll();
        assertThat(ruleTagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRuleTags() throws Exception {
        // Initialize the database
        ruleTagRepository.saveAndFlush(ruleTag);

        // Get all the ruleTagList
        restRuleTagMockMvc.perform(get("/api/rule-tags?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ruleTag.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getRuleTag() throws Exception {
        // Initialize the database
        ruleTagRepository.saveAndFlush(ruleTag);

        // Get the ruleTag
        restRuleTagMockMvc.perform(get("/api/rule-tags/{id}", ruleTag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ruleTag.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRuleTag() throws Exception {
        // Get the ruleTag
        restRuleTagMockMvc.perform(get("/api/rule-tags/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRuleTag() throws Exception {
        // Initialize the database
        ruleTagService.save(ruleTag);

        int databaseSizeBeforeUpdate = ruleTagRepository.findAll().size();

        // Update the ruleTag
        RuleTag updatedRuleTag = ruleTagRepository.findOne(ruleTag.getId());
        updatedRuleTag
            .name(UPDATED_NAME);

        restRuleTagMockMvc.perform(put("/api/rule-tags")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRuleTag)))
            .andExpect(status().isOk());

        // Validate the RuleTag in the database
        List<RuleTag> ruleTagList = ruleTagRepository.findAll();
        assertThat(ruleTagList).hasSize(databaseSizeBeforeUpdate);
        RuleTag testRuleTag = ruleTagList.get(ruleTagList.size() - 1);
        assertThat(testRuleTag.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingRuleTag() throws Exception {
        int databaseSizeBeforeUpdate = ruleTagRepository.findAll().size();

        // Create the RuleTag

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRuleTagMockMvc.perform(put("/api/rule-tags")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ruleTag)))
            .andExpect(status().isCreated());

        // Validate the RuleTag in the database
        List<RuleTag> ruleTagList = ruleTagRepository.findAll();
        assertThat(ruleTagList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRuleTag() throws Exception {
        // Initialize the database
        ruleTagService.save(ruleTag);

        int databaseSizeBeforeDelete = ruleTagRepository.findAll().size();

        // Get the ruleTag
        restRuleTagMockMvc.perform(delete("/api/rule-tags/{id}", ruleTag.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RuleTag> ruleTagList = ruleTagRepository.findAll();
        assertThat(ruleTagList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RuleTag.class);
    }
}
