package org.example.other;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

public class BoyerMooreTest {
    @ParameterizedTest
    @DisplayName("simpleSearch - параметризованный тест различных паттернов")
    @MethodSource("provideSearchTestData")
    void testSimpleSearchParameterized(String text, String pattern, List<Integer> expectedPositions) {
        List<Integer> result = BoyerMoore.searchHorspool(text, pattern);
        assertEquals(expectedPositions, result);
    }
    
    @ParameterizedTest
    @DisplayName("search - параметризованный тест различных паттернов")
    @MethodSource("provideSearchTestData")
    void testSearchParameterized(String text, String pattern, List<Integer> expectedPositions) {
        List<Integer> result = BoyerMoore.search(text, pattern);
        assertEquals(expectedPositions, result);
    }
    
    private static Stream<Arguments> provideSearchTestData() {
        return Stream.of(
            Arguments.of("ABCABCABC", "ABC", Arrays.asList(0, 3, 6)),
            Arguments.of("AAAAAA", "AAA", Arrays.asList(0, 1, 2, 3)),
            Arguments.of("ABABABAB", "ABAB", Arrays.asList(0, 2, 4)),
            Arguments.of("The quick brown fox", "fox", Arrays.asList(16)),
            Arguments.of("test test test", "test", Arrays.asList(0, 5, 10)),
            Arguments.of("Mississippi", "iss", Arrays.asList(1, 4)),
            Arguments.of("abracadabra", "abra", Arrays.asList(0, 7))
        );
    }
    
    @ParameterizedTest
    @DisplayName("simpleSearch - CSV тест с граничными случаями")
    @CsvSource({
        "'a', 'a', 0",
        "'aa', 'a', 0",
        "'ab', 'b', 1",
        "'abc', 'abc', 0",
        "'abcde', 'xyz', -1"
    })
    void testSimpleSearchWithCsv(String text, String pattern, int expectedFirstPosition) {
        List<Integer> result = BoyerMoore.searchHorspool(text, pattern);
        
        if (expectedFirstPosition == -1) {
            assertTrue(result.isEmpty());
        } else {
            assertFalse(result.isEmpty());
            assertEquals(expectedFirstPosition, result.get(0));
        }
    }
}
