package com.bantaypetsa.app.domain.engine

import com.bantaypetsa.app.domain.model.StockActionType

/**
 * Pure domain engine enforcing inventory mathematics and stock integrity invariants:
 * Invariant: Q_after = Q_before + delta >= 0
 */
object StockInventoryEngine {

    sealed class StockActionResult {
        data class Success(
            val quantityBefore: Double,
            val quantityAfter: Double,
            val quantityChange: Double,
            val actionType: StockActionType,
            val reason: String
        ) : StockActionResult()

        data class Error(
            val code: ErrorCode,
            val message: String
        ) : StockActionResult()
    }

    enum class ErrorCode {
        NEGATIVE_DELTA,
        INSUFFICIENT_STOCK,
        NEGATIVE_TARGET_STOCK,
        INVALID_QUANTITY
    }

    /**
     * Applies a stock action adhering strictly to mathematical invariants.
     *
     * @param currentQuantity Current quantity on hand (must be >= 0)
     * @param actionType ADD, REMOVE, or ADJUST
     * @param inputQuantity For ADD/REMOVE: the amount to add or remove (must be > 0).
     *                      For ADJUST: the new target quantity on hand (must be >= 0).
     * @param reason Optional note or rationale
     */
    fun processStockAction(
        currentQuantity: Double,
        actionType: StockActionType,
        inputQuantity: Double,
        reason: String = ""
    ): StockActionResult {
        if (currentQuantity < 0) {
            return StockActionResult.Error(
                ErrorCode.INVALID_QUANTITY,
                "Current stock cannot be negative (was $currentQuantity)."
            )
        }

        return when (actionType) {
            StockActionType.ADD -> {
                if (inputQuantity <= 0) {
                    StockActionResult.Error(
                        ErrorCode.NEGATIVE_DELTA,
                        "Addition quantity must be strictly greater than 0 (received $inputQuantity)."
                    )
                } else {
                    val newQty = currentQuantity + inputQuantity
                    StockActionResult.Success(
                        quantityBefore = currentQuantity,
                        quantityAfter = newQty,
                        quantityChange = inputQuantity,
                        actionType = actionType,
                        reason = reason.ifBlank { "Restock / Delivery intake" }
                    )
                }
            }

            StockActionType.REMOVE -> {
                if (inputQuantity <= 0) {
                    StockActionResult.Error(
                        ErrorCode.NEGATIVE_DELTA,
                        "Removal quantity must be strictly greater than 0 (received $inputQuantity)."
                    )
                } else if (currentQuantity < inputQuantity) {
                    StockActionResult.Error(
                        ErrorCode.INSUFFICIENT_STOCK,
                        "Cannot remove $inputQuantity units; only $currentQuantity units available on hand."
                    )
                } else {
                    val newQty = currentQuantity - inputQuantity
                    StockActionResult.Success(
                        quantityBefore = currentQuantity,
                        quantityAfter = newQty,
                        quantityChange = -inputQuantity,
                        actionType = actionType,
                        reason = reason.ifBlank { "Retail sale / customer purchase" }
                    )
                }
            }

            StockActionType.ADJUST -> {
                if (inputQuantity < 0) {
                    StockActionResult.Error(
                        ErrorCode.NEGATIVE_TARGET_STOCK,
                        "Target stock quantity after adjustment cannot be negative (received $inputQuantity)."
                    )
                } else {
                    val delta = inputQuantity - currentQuantity
                    StockActionResult.Success(
                        quantityBefore = currentQuantity,
                        quantityAfter = inputQuantity,
                        quantityChange = delta,
                        actionType = actionType,
                        reason = reason.ifBlank { "Physical count inventory adjustment" }
                    )
                }
            }
        }
    }
}
