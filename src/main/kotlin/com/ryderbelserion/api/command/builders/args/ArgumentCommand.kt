package com.ryderbelserion.api.command.builders.args

abstract class ArgumentCommand {

    abstract fun getArgAsInt(index: Int, notifySender: Boolean): Int

    abstract fun getArgAsLong(index: Int, notifySender: Boolean): Long

    abstract fun getArgAsDouble(index: Int, notifySender: Boolean): Double

    abstract fun getArgAsBoolean(index: Int, notifySender: Boolean): Boolean

    abstract fun getArgAsFloat(index: Int, notifySender: Boolean): Float

}