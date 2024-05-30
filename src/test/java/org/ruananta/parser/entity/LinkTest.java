package org.ruananta.parser.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ruananta.parser.engine.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LinkTest {

    private Task.Link link;
    private Task.Selector selector1;
    private Task.Selector selector2;

    @BeforeEach
    public void setUp() {
        link = new Task.Link();
        selector1 = new Task.Selector();
        selector2 = new Task.Selector();

        selector1.setId(0L);
        selector2.setId(1L);

        link.getSelectors().add(selector1);
        link.getSelectors().add(selector2);
    }

    @Test
    public void testMoveUp() {
        // Arrange
        selector1.setId(1L);
        selector2.setId(2L);

        // Act
        link.moveUp(selector2);

        // Assert
        assertEquals(2L, selector1.getId());
        assertEquals(1L, selector2.getId());
    }

    @Test
    public void testMoveDown() {
        // Arrange
        selector1.setId(1L);
        selector2.setId(2L);

        // Act
        link.moveDown(selector1);

        // Assert
        assertEquals(2L, selector1.getId());
        assertEquals(1L, selector2.getId());
    }
}

