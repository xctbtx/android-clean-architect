package com.xctbtx.cleanarchitectsample.data.post.mapper

import com.xctbtx.cleanarchitectsample.data.post.dto.PostDto
import com.xctbtx.cleanarchitectsample.domain.post.model.Post

object PostMapper {
    fun PostDto.toDomain(): Post {
        return Post(
            id = this.id,
            title = this.title,
            content = this.content,
            imageUrl = this.image_url,
        )
    }
}
