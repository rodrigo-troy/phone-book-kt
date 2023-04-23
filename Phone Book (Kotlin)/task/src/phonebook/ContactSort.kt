package phonebook

import kotlin.system.measureTimeMillis

/**
 * Created with IntelliJ IDEA.
$ Project: Phone Book (Kotlin)
 * User: rodrigotroy
 * Date: 23-04-23
 * Time: 17:48
 */
class ContactSort {
    fun bubbleSort(list: List<Contact>, maxTimeMillis: Long): SortResult {
        val sortedList = list.toMutableList()
        var swapped: Boolean

        var currentTimeMillis = 0L

        for (i in sortedList.indices) {
            val sortTimeMillis = measureTimeMillis {
                swapped = false
                for (j in 0 until (sortedList.size - 1 - i)) {
                    if (sortedList[j] > sortedList[j + 1]) {
                        val temp = sortedList[j]
                        sortedList[j] = sortedList[j + 1]
                        sortedList[j + 1] = temp
                        swapped = true
                    }
                }
            }

            currentTimeMillis += sortTimeMillis

            if (!swapped) break

            if (currentTimeMillis > maxTimeMillis) {
                return SortResult(sortedList, currentTimeMillis, false)
            }
        }

        return SortResult(sortedList, currentTimeMillis, true)
    }

    fun quickSort(list: List<Contact>, maxTimeMillis: Long): SortResult {
        val sortedList = list.toMutableList()
        var currentTimeMillis = 0L

        val sortTimeMillis = measureTimeMillis {
            quickSort(sortedList, 0, sortedList.size - 1)
        }

        currentTimeMillis += sortTimeMillis

        return SortResult(sortedList, currentTimeMillis, true)
    }

    private fun quickSort(sortedList: MutableList<Contact>, low: Int, high: Int) {
        if (low >= high) return

        val pivot = sortedList[high]
        var wall = low - 1

        for (j in low until high) {
            if (sortedList[j] <= pivot) {
                wall++
                val temp = sortedList[wall]
                sortedList[wall] = sortedList[j]
                sortedList[j] = temp
            }
        }

        val temp = sortedList[wall + 1]
        sortedList[wall + 1] = sortedList[high]
        sortedList[high] = temp

        quickSort(sortedList, low, wall)
        quickSort(sortedList, wall + 2, high)
    }
}
