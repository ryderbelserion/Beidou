package com.ryderbelserion.api.command.builders.args

interface CommandArgs {

    fun getArgAsInt(index: Int, notifySender: Boolean): Int

    fun getArgAsLong(index: Int, notifySender: Boolean): Long

    fun getArgAsDouble(index: Int, notifySender: Boolean): Double

    fun getArgAsBoolean(index: Int, notifySender: Boolean): Boolean

    fun getArgAsFloat(index: Int, notifySender: Boolean): Float

}