//package com.example.jdbc.programInsert;
//
//
//import com.example.jdbc.controller.TestJdbcController;
//import com.example.jdbc.dto.ProgramDetailDTO;
//import com.example.jdbc.dto.ProgramInsert;
//import com.example.jdbc.service.ProgramDetailService;
//import com.example.jdbc.service.ProgramInsertService;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.Before;
//import org.junit.Ignore;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MockMvcBuilder;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//public class ProgramInsertTest {
//
//
//    private MockMvc mockMvc;
//
//
//    @Mock
//    private ProgramInsertService service;
//
//    @InjectMocks
//    private TestJdbcController controller;
//
//    @BeforeEach
//    public void setUp(WebApplicationContext context) {
//        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
//    }
//
////    @Before
////    public void setup() {
////
////        // this must be called for the @Mock annotations above to be processed
////        // and for the mock service to be injected into the controller under
////        // test.
////        MockitoAnnotations.initMocks(this);
////
////        this.mockMvc = MockMvcBuilders.standaloneSetup(testJdbcController).build();
////
////    }
////    @Autowired
////    private ProgramInsertService programInsertService;
////
////    @Autowired
////    private ProgramDetailService programDetail;
////
//////    @Mock
//////    private JdbcTemplate jdbcTemplate;
////
////    @BeforeEach
////    public  void beforeAll() {
////        this.mockMvc = MockMvcBuilders.standaloneSetup(new TestJdbcController(new JdbcTemplate())).build();
////    }
//
//    @Ignore
//    private String asJsonString(Object data){
//
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            String value = objectMapper.writeValueAsString(data);
//            return value;
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Test
//    @DisplayName("halo test")
//    void name() throws Exception {
//        ProgramInsert program = new ProgramInsert();
//        program.setFIDProgram("107");
//        program.setNamaProgram("Claim");
//        program.setTanggalMulaiProgram("01/12/2022");
//        program.setTanggalAwalRealisasi("11/10/2022");
//
//        ProgramDetailDTO p = new ProgramDetailDTO();
//        p.setRequestId("1333");
//        this.mockMvc.perform( MockMvcRequestBuilders
//                .post("/programInsert")
//                .content(asJsonString(program))
//                .accept(MediaType.APPLICATION_JSON_VALUE)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .accept(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.ResponseCodes").exists())
//        ;
//    }
//}
