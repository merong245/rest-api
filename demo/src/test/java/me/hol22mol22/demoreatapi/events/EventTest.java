package me.hol22mol22.demoreatapi.events;


import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
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

    @ParameterizedTest
    // is not Type safe
    @CsvSource({
            "0, 0, true",
            "0, 100, false",
            "100, 0, false",
            "100, 100, false"
    })
    public void testFree(int basePrice, int maxPrice, boolean isFree){
        // given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();

        // when
        event.update();


        // then
        assertThat(event.isFree()).isEqualTo(isFree);

    }

    private static Stream<Arguments> paramsForTestOffline(){
        return Stream.of(
                Arguments.of("강남역 2번출구 스타벅스", true),
                Arguments.of("", false),
                Arguments.of(null, false)
        );
    }

    @ParameterizedTest
    @MethodSource("paramsForTestOffline")
    public void testOffline(String location, boolean isOffline){
        // given location
        Event event = Event.builder()
                .location(location)
                .build();

        // when
        event.update();


        // then
        assertThat(event.isOffline()).isEqualTo(isOffline);

    }
}