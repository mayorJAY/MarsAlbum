package com.josycom.mayorjay.marsalbum.common.util

import com.josycom.mayorjay.marsalbum.common.domain.model.Rover
import junit.framework.TestCase

class UtilTest : TestCase() {

    fun `test isEmptyOrNull pass_valid_string _empty_or_null_check_done`() {
        val result = "Rover".isEmptyOrNull()
        assertFalse(result)
    }

    fun `test isEmptyOrNull pass_empty_list _empty_or_null_check_done`() {
        val result = emptyList<String>().isEmptyOrNull()
        assertTrue(result)
    }

    fun `test isEmptyOrNull pass_null_object _empty_or_null_check_done`() {
        val rover: Rover? = null
        val result = rover.isEmptyOrNull()
        assertTrue(result)
    }

    fun `test getFormattedDate yyyy-MM-dd_date_format parsed_successfully`() {
        val result = "2022-10-29".getFormattedDate()
        assertEquals("Oct 29 2022", result)
    }

    fun `test getFormattedDate yyyy-MM-dd date_format parsed_successfully`() {
        val result = "1950-02-05".getFormattedDate()
        assertEquals("Feb 05 1950", result)
    }

    fun `test getFormattedDate any_other_date_format parsing_failed`() {
        val result = "22/12/2023".getFormattedDate()
        assertFalse(result.contains("Dec"))
    }

    fun `test getFormattedDate any_other_date_format parsing_failed _same_date_returned`() {
        val result = "22/12/2023".getFormattedDate()
        assertEquals("22/12/2023", result)
    }
}