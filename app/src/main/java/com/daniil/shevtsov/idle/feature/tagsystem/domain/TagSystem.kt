package com.daniil.shevtsov.idle.feature.tagsystem.domain

import com.daniil.shevtsov.idle.feature.action.domain.Action

class TagSystem {
    fun addTag(tag: String?) {
    }

    fun isActionAvailable(playerTags: List<Tag>, action: Action): Boolean {
        return playerTags.containsAll(action.tags.filter { it.value == TagRelation.Required }.keys.toList())
    }
}