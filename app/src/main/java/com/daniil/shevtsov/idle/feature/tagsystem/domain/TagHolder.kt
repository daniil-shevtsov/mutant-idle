package com.daniil.shevtsov.idle.feature.tagsystem.domain

import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.SpikeTags

interface TagHolder {
    val id: String
    val tags: SpikeTags
}
