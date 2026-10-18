package com.bantaypetsa.app.domain

import com.bantaypetsa.app.domain.engine.StockInventoryEngine
import com.bantaypetsa.app.domain.model.StockActionType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class StockInventoryEngineTest {

    @Test
    fun testAddStockIncreasesQuantityAndMaintainsInvariant() {
        val current = 15.0
        val addDelta = 10.0

        val result = StockInventoryEngine.processStockAction(
            currentQuantity = current,
            actionType = StockActionType.ADD,
            inputQuantity = addDelta,
            reason = "Restocked from distributor"
        )

        assertTrue(result is StockInventoryEngine.StockActionResult.Success)
        val success = result as StockInventoryEngine.StockActionResult.Success
        assertEquals(15.0, success.quantityBefore, 0.001)
        assertEquals(25.0, success.quantityAfter, 0.001)
        assertEquals(10.0, success.quantityChange, 0.001)
        assertEquals(StockActionType.ADD, success.actionType)
        // Invariant check: Q_after = Q_before + delta >= 0
        assertEquals(success.quantityAfter, success.quantityBefore + success.quantityChange, 0.001)
        assertTrue(success.quantityAfter >= 0)
    }

    @Test
    fun testRemoveStockDecreasesQuantityAndMaintainsInvariant() {
        val current = 20.0
        val removeDelta = 7.0

        val result = StockInventoryEngine.processStockAction(
            currentQuantity = current,
            actionType = StockActionType.REMOVE,
            inputQuantity = removeDelta,
            reason = "Customer purchase"
        )

        assertTrue(result is StockInventoryEngine.StockActionResult.Success)
        val success = result as StockInventoryEngine.StockActionResult.Success
        assertEquals(20.0, success.quantityBefore, 0.001)
        assertEquals(13.0, success.quantityAfter, 0.001)
        assertEquals(-7.0, success.quantityChange, 0.001)
        assertEquals(StockActionType.REMOVE, success.actionType)
        // Invariant check: Q_after = Q_before + delta >= 0
        assertEquals(success.quantityAfter, success.quantityBefore + success.quantityChange, 0.001)
        assertTrue(success.quantityAfter >= 0)
    }

    @Test
    fun testRemoveStockInsufficientInventoryFails() {
        val current = 5.0
        val removeDelta = 10.0 // Attempting to remove more than on hand

        val result = StockInventoryEngine.processStockAction(
            currentQuantity = current,
            actionType = StockActionType.REMOVE,
            inputQuantity = removeDelta
        )

        assertTrue(result is StockInventoryEngine.StockActionResult.Error)
        val error = result as StockInventoryEngine.StockActionResult.Error
        assertEquals(StockInventoryEngine.ErrorCode.INSUFFICIENT_STOCK, error.code)
    }

    @Test
    fun testRemoveExactCurrentStockReachesZeroCleanly() {
        val current = 12.0
        val removeDelta = 12.0

        val result = StockInventoryEngine.processStockAction(
            currentQuantity = current,
            actionType = StockActionType.REMOVE,
            inputQuantity = removeDelta
        )

        assertTrue(result is StockInventoryEngine.StockActionResult.Success)
        val success = result as StockInventoryEngine.StockActionResult.Success
        assertEquals(0.0, success.quantityAfter, 0.001)
        assertEquals(-12.0, success.quantityChange, 0.001)
        assertTrue(success.quantityAfter >= 0)
    }

    @Test
    fun testZeroOrNegativeQuantityDeltaRejected() {
        val zeroResult = StockInventoryEngine.processStockAction(
            currentQuantity = 10.0,
            actionType = StockActionType.ADD,
            inputQuantity = 0.0
        )
        assertTrue(zeroResult is StockInventoryEngine.StockActionResult.Error)

        val negativeResult = StockInventoryEngine.processStockAction(
            currentQuantity = 10.0,
            actionType = StockActionType.REMOVE,
            inputQuantity = -5.0
        )
        assertTrue(negativeResult is StockInventoryEngine.StockActionResult.Error)
    }

    @Test
    fun testAdjustStockSetsQuantityTargetAndComputesCorrectDelta() {
        // Physical count reveals stock went from 14 down to 10
        val result = StockInventoryEngine.processStockAction(
            currentQuantity = 14.0,
            actionType = StockActionType.ADJUST,
            inputQuantity = 10.0,
            reason = "Damaged packaging write-off"
        )

        assertTrue(result is StockInventoryEngine.StockActionResult.Success)
        val success = result as StockInventoryEngine.StockActionResult.Success
        assertEquals(14.0, success.quantityBefore, 0.001)
        assertEquals(10.0, success.quantityAfter, 0.001)
        assertEquals(-4.0, success.quantityChange, 0.001)
    }

    @Test
    fun testAdjustStockNegativeTargetRejected() {
        val result = StockInventoryEngine.processStockAction(
            currentQuantity = 14.0,
            actionType = StockActionType.ADJUST,
            inputQuantity = -3.0
        )

        assertTrue(result is StockInventoryEngine.StockActionResult.Error)
        val error = result as StockInventoryEngine.StockActionResult.Error
        assertEquals(StockInventoryEngine.ErrorCode.NEGATIVE_TARGET_STOCK, error.code)
    }
}
