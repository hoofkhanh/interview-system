package com.hokhanh.session.redis.file;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.hokhanh.session.redis.RedisService;
import com.hokhanh.session.request.sharedFile.createSharedFile.CreateSharedFileInput;
import com.hokhanh.session.request.sharedFile.deleteSharedFile.DeleteSharedFileInput;
import com.hokhanh.session.request.sharedFile.updateSharedFile.UpdateSharedFileInput;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileCacheService {

	private final RedisService redisService;
	
	private final static String SHARED_FILE_REDIS_KEY_PREFIX = "project:";
	private final static String SHARED_FILE_REDIS_KEY_SUFFIX = ":files";
	
	private UUID generateUUID() {
		return UUID.randomUUID();
	}
	
	public File cacheSharedFile(CreateSharedFileInput input, UUID userId) {
		File file = new File(generateUUID(), null, input.baseFile().content(), input.baseFile().name());
		
		String redisKey = SHARED_FILE_REDIS_KEY_PREFIX + input.baseFile().sessionId() + ":shared" +SHARED_FILE_REDIS_KEY_SUFFIX;
		
		redisService.hset( redisKey , file.id().toString(), file);
		return file;
	}
	
	public File updateSharedFile(UpdateSharedFileInput input, UUID userId) {
		String redisKey = SHARED_FILE_REDIS_KEY_PREFIX + input.baseFile().sessionId() + ":shared" +SHARED_FILE_REDIS_KEY_SUFFIX;
		
		File file = new File(
			UUID.fromString(input.id()), 
			null,
			input.baseFile().content(), 
			input.baseFile().name()
		);
		
		redisService.hset( redisKey , file.id().toString(), file);
		return file;
	}

	public void deleteSharedFile(@Valid DeleteSharedFileInput input, UUID userId) {
		String redisKey = SHARED_FILE_REDIS_KEY_PREFIX + input.sessionId() + ":shared" +SHARED_FILE_REDIS_KEY_SUFFIX;
		redisService.hdel(redisKey, input.id());
	}
}
