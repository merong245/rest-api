package me.hol22mol22.demoreatapi.events;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EventTest {

    @Test
    public void builder(){
        Event event = Event.builder()
                .name("name")
                .description("description")
                .build();

        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean(){
        // Given
        String description = "Spring";
        String name = "Event";

        //When
        Event event = new Event();
        event.setName(name);
        event.setDescription(description);

        // Then
        assertThat(event.getName()).isEqualTo(name);

        assertThat(event.getDescription()).isEqualTo(description);
    }

    @Test
    public void testFree(){
        // given
        Event event = Event.builder()
                .basePrice(0)
                .maxPrice(0)
                .build();

        // when
        event.update();


        // then
        assertThat(event.isFree()).isTrue();

        // given
        event = Event.builder()
                .basePrice(100)
                .maxPrice(0)
                .build();

        // when
        event.update();


        // then
        assertThat(event.isFree()).isFalse();

        // given
        event = Event.builder()
                .basePrice(0)
                .maxPrice(100)
                .build();

        // when
        event.update();


        // then
        assertThat(event.isFree()).isFalse();
    }

    @Test
    public void testOffline(){
        // given location
        Event event = Event.builder()
                .location("강남역 2번출구 스타벅스")
                .build();

        // when
        event.update();


        // then
        assertThat(event.isOffline()).isTrue();

        // given no location
        event = Event.builder().build();

        // when
        event.update();


        // then
        assertThat(event.isOffline()).isFalse();

    }
}