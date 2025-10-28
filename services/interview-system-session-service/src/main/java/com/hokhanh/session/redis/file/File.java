package com.hokhanh.session.redis.file;

import java.io.Serializable;
import java.util.UUID;

public record File(
	UUID id,
	UUID folderId,
	String content,
	String name
) implements Serializable {
    private static final long serialVersionUID = 1L;
}