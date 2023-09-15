package br.com.vitordel.avanaderpg.characters.controller;

import br.com.vitordel.avanaderpg.characters.model.Character;
import br.com.vitordel.avanaderpg.characters.model.CharacterCategory;
import br.com.vitordel.avanaderpg.characters.service.CharacterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class CharacterControllerTest {

    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Mock
    private CharacterService characterService;

    @InjectMocks
    private CharacterController characterController;

    Character RECORD_1 = new Character(1L, CharacterCategory.HERO, "species_1", 1L, 1L, 1L, 1L, 1L, 1L);
    Character RECORD_2 = new Character(2L, CharacterCategory.MONSTER, "species_2", 2L, 2L, 2L, 2L, 2L, 2L);
    Character RECORD_3 = new Character(3L, CharacterCategory.HERO, "species_3", 3L, 3L, 3L, 3L, 3L, 3L);


    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(characterController).build();
    }

    @Test
    public void getAllRecords_success() throws Exception {
        List<Character> records = new ArrayList<>(Arrays.asList(RECORD_1, RECORD_2, RECORD_3));

        Mockito.when(characterService.getAllCharacters()).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/characters")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
                .andExpect((ResultMatcher) jsonPath("$[2].species", is("species_3")));
    }
}
