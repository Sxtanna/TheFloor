package com.sxtanna.bby.base

import com.sxtanna.korm.Korm
import java.io.InputStream

data class Resource(val name: String, val link: String) {

    companion object Source {

        private val loaded = mutableMapOf<String, Resource>()


        fun load(korm: Korm, stream: InputStream) {
            loaded.putAll(korm.pull(stream).toHash())
        }

        fun values(): Collection<Resource> {
            return loaded.values
        }

    }

}