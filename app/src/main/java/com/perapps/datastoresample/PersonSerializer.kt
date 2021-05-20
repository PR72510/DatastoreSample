package com.perapps.datastoresample

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

/**
 * Created by PR72510 on 20/05/21.
 */
object PersonSerializer : Serializer<Person> {
    override val defaultValue: Person
        get() = Person.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): Person {
        try {
            return Person.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read Proto, ", exception)
        }
    }

    override suspend fun writeTo(t: Person, output: OutputStream) = t.writeTo(output)
}