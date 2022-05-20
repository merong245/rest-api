package me.hol22mol22.demoreatapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.hol22mol22.demoreatapi.common.TestDescription;
import me.hol22mol22.demoreatapi.events.Event;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    @TestDescription("정상적으로 이벤트를 생성하는 이벤트")
    public void createEvent() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("Rest API Test")
                .beginEnrollmentDateTime(LocalDateTime.of(2022,05,18,14,21))
                .closeEnrollmentDateTime(LocalDateTime.of(2022,05,18,14,22))
                .beginEventDateTime(LocalDateTime.of(2022,05,19,14,21))
                .endEventDateTime(LocalDateTime.of(2022,05,22,14,22))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타트업")
                .build();

        mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(Matchers.not(true)))
                .andExpect(jsonPath("eventStatus").value(Matchers.not(EventStatus.PUBLISHED )));
    }

    @Test
    @TestDescription("입력 받을 수 없는 값을 사용한 경우에는 BadRequest 발생 테스트")
    public void createEvent_BadRequest() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("Rest API Test")
                .beginEnrollmentDateTime(LocalDateTime.of(2022,05,18,14,21))
                .closeEnrollmentDateTime(LocalDateTime.of(2022,05,18,14,22))
                .beginEventDateTime(LocalDateTime.of(2022,05,19,14,21))
                .endEventDateTime(LocalDateTime.of(2022,05,22,14,22))
                .basePrice(100)
                .maxPrice(200)
                .free(true)
                .offline(false)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타트업")
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @TestDescription("입력값이 비어있는 경우에 BadRequest 발생하는 테스트")
    public void createEvent_BadRequest_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("입력값이 잘못된 경우에 BadRequest가 발생하는 테스트")
    public void createEvent_BadRequest_Wrong_Input() throws Exception {
        // 시작날짜보다 종료날짜가 빠름
        // basePrice가 maxPrice보다 높음
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("Rest API Test")
                .beginEnrollmentDateTime(LocalDateTime.of(2022,05,18,14,21))
                .closeEnrollmentDateTime(LocalDateTime.of(2022,05,17,14,22))
                .beginEventDateTime(LocalDateTime.of(2022,05,19,14,21))
                .endEventDateTime(LocalDateTime.of(2022,05,15,14,22))
                .basePrice(4000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타트업")
                .build();;

        this.mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }
}
