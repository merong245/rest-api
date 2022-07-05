package me.hol22mol22.demorestapi.events;

import me.hol22mol22.demorestapi.common.BaseControllerTest;
import me.hol22mol22.demorestapi.common.TestDescription;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class EventControllerTests extends BaseControllerTest {

    @Autowired
    EventRepository eventRepository;

    @Test
    @TestDescription("정상적으로 이벤트를 생성하는 이벤트")
    public void createEvent() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("Rest API Test")
                .beginEnrollmentDateTime(LocalDateTime.of(2022,5,18,14,21))
                .closeEnrollmentDateTime(LocalDateTime.of(2022,5,18,14,22))
                .beginEventDateTime(LocalDateTime.of(2022,5,19,14,21))
                .endEventDateTime(LocalDateTime.of(2022, 5,22,14,22))
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
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.toString()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-events").exists())
                .andDo(document("create-event",
                        links(// links 문서화
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query events"),
                                linkWithRel("update-events").description("link to update an existing"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        requestFields(
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin of Enrollment"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close of Enrollment"),
                                fieldWithPath("beginEventDateTime").description("date time of begin of Event"),
                                fieldWithPath("endEventDateTime").description("date time of end of Event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("base price of new event"),
                                fieldWithPath("maxPrice").description("max price of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enroll")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content Type") //hal
                        ),
                        // 모든 부분이 아니라 일부분만 문서화 하기위해 relaxed prefix
                        // 문서의 일부분만 사용하기에 field에 매칭이 되는 field만 문서화 진행
                        // relaxed를 사용할땐 정확한 문서를 만들지 못한다는 것
                        // link 정보는 이미 테스트 했는데 또한번 테스트를 하기때문 -> 중복 테스트
                        // hal format을 따르기 때문에
                        responseFields(
                                fieldWithPath("id").description("identifier of new event"),
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin of Enrollment"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close of Enrollment"),
                                fieldWithPath("beginEventDateTime").description("date time of begin of Event"),
                                fieldWithPath("endEventDateTime").description("date time of end of Event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("base price of new event"),
                                fieldWithPath("maxPrice").description("max price of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enroll"),
                                fieldWithPath("free").description("it tells is this event is free or not"),
                                fieldWithPath("offline").description("it tells is this event is offline or not"),
                                fieldWithPath("eventStatus").description("event status"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.query-events.href").description("link to query events"),
                                fieldWithPath("_links.update-events.href").description("link to update an existing"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )

                )) // 문서의 이름
        ;
    }

    @Test
    @TestDescription("입력 받을 수 없는 값을 사용한 경우에는 BadRequest 발생 테스트")
    public void createEvent_BadRequest() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("Rest API Test")
                .beginEnrollmentDateTime(LocalDateTime.of(2022, 5,18,14,21))
                .closeEnrollmentDateTime(LocalDateTime.of(2022, 5,18,14,22))
                .beginEventDateTime(LocalDateTime.of(2022, 5,19,14,21))
                .endEventDateTime(LocalDateTime.of(2022, 5,22,14,22))
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
                .beginEnrollmentDateTime(LocalDateTime.of(2022, 5,18,14,21))
                .closeEnrollmentDateTime(LocalDateTime.of(2022, 5,17,14,22))
                .beginEventDateTime(LocalDateTime.of(2022, 5,19,14,21))
                .endEventDateTime(LocalDateTime.of(2022, 5,15,14,22))
                .basePrice(4000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타트업")
                .build();

        this.mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors[0].objectName").exists())
                .andExpect(jsonPath("errors[0].defaultMessage").exists())
                .andExpect(jsonPath("errors[0].code").exists())
                .andExpect(jsonPath("_links.index").exists()) // 에러 발쌩시 이동장소인 index 로 가능 링크 추가
        ;
    }

    @Test
    @TestDescription("30개의 이벤트를 10개씩 두번째 페이제 조회하기")
    public void queryEvents() throws Exception{
        //Given
        // Event 30개 생성
        IntStream.range(0, 30).forEach(this::generateEvent);

        //When && Then
        this.mockMvc.perform(get("/api/events")
                        .param("page", "1")
                        .param("size","10")
                        .param("sort","name,DESC")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("query-events"))
                ;
    }

    @Test
    @TestDescription("기존의 이벤트를 하나 조회하기")
    public void getEvent() throws Exception{
        //Given
        Event event = this.generateEvent(100);

        //When && Then
        this.mockMvc.perform(get("/api/events/{id}",event.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("get-an-event"))
                ;
    }

    @Test
    @TestDescription("없는 이벤트 조회시 404 응답받기")
    public void getEvent404() throws Exception{
        //Given
        Event event = this.generateEvent(100);

        //When && Then
        this.mockMvc.perform(get("/api/events/12312312"))
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    @TestDescription("이벤트 정상 수정")
    public void updateEvent() throws Exception{
        //Given
        Event event = this.generateEvent(200);
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);
        String eventName = "Updated Event";
        eventDto.setName(eventName);

        //When & Then
        this.mockMvc.perform(put("/api/events/{id}",event.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(eventName))
                .andExpect(jsonPath("_links.self").exists())
                .andDo(document("update-event"))
        ;
    }

    @Test
    @TestDescription("입력 값이 없는 경우 이벤트 수정 실패")
    public void updateEvent400Empty() throws Exception{
        //Given
        Event event = this.generateEvent(200);
        EventDto eventDto = new EventDto();

        //When & Then
        this.mockMvc.perform(put("/api/events/{id}",event.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }
    @Test
    @TestDescription("입력 값이 잘못된 경우 이벤트 수정 실패")
    public void updateEvent400Wrong() throws Exception{
        //Given
        Event event = this.generateEvent(200);
        EventDto eventDto = this.modelMapper.map(event,EventDto.class);
        eventDto.setBasePrice(20000);
        eventDto.setMaxPrice(1000);


        //When & Then
        this.mockMvc.perform(put("/api/events/{id}",event.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @TestDescription("존재하지 않는 이벤트 수정시 이벤트 수정 실패")
    public void updateEvent404() throws Exception{
        //Given
        Event event = this.generateEvent(200);
        EventDto eventDto = this.modelMapper.map(event,EventDto.class);

        //When & Then
        this.mockMvc.perform(put("/api/events/141231")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isNotFound())
        ;
    }

    // n개의 이벤트 생성
    private Event generateEvent(int index) {
        Event event = Event.builder()
                .name("event " +index)
                .description("test event")
                .beginEnrollmentDateTime(LocalDateTime.of(2022,5,18,14,21))
                .closeEnrollmentDateTime(LocalDateTime.of(2022,5,18,14,22))
                .beginEventDateTime(LocalDateTime.of(2022,5,19,14,21))
                .endEventDateTime(LocalDateTime.of(2022, 5,22,14,22))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타트업")
                .free(false)
                .offline(true)
                .eventStatus(EventStatus.DRAFT)
                .build()
                ;

        return this.eventRepository.save(event);
    }
}
