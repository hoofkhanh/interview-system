package com.hokhanh.session.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.hokhanh.session.mapper.FileMapper;
import com.hokhanh.session.model.Session;
import com.hokhanh.session.model.Status;
import com.hokhanh.session.redis.file.File;
import com.hokhanh.session.redis.file.FileCacheService;
import com.hokhanh.session.repository.ParticipantRepository;
import com.hokhanh.session.repository.SessionRepository;
import com.hokhanh.session.request.sharedFile.createSharedFile.CreateSharedFileInput;
import com.hokhanh.session.request.sharedFile.deleteSharedFile.DeleteSharedFileInput;
import com.hokhanh.session.request.sharedFile.updateSharedFile.UpdateSharedFileInput;
import com.hokhanh.session.response.common.BaseApiPayload;
import com.hokhanh.session.response.common.CacheApiStatusType;
import com.hokhanh.session.response.sharedFile.createSharedFile.CreateSharedFileApiPayload;
import com.hokhanh.session.response.sharedFile.deleteSharedFile.DeleteSharedFileApiPayload;
import com.hokhanh.session.response.sharedFile.updateSharedFile.UpdateSharedFileApiPayload;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class FileService {
	private final SessionRepository sessionRepo;
	private final ParticipantRepository participantRepo;
	private final FileCacheService fileCacheService;
	private final FileMapper fileMapper;

	public CreateSharedFileApiPayload createSharedFile( CreateSharedFileInput input, UUID userId) {
		Session session = sessionRepo.findById(UUID.fromString(input.baseFile().sessionId())).orElse(null);
		boolean existingResult = participantRepo.existsByUserIdAndSessionId(userId, UUID.fromString(input.baseFile().sessionId()));
		if(session == null) {
			return new CreateSharedFileApiPayload(
				new BaseApiPayload(false, "Session not found"),
				CacheApiStatusType.SESSION_NOT_FOUND,
				null
			);
		}else if(!session.getStatus().equals(Status.STARTING)) {
			return new CreateSharedFileApiPayload(
				new BaseApiPayload(false, "Session not starting"),
				CacheApiStatusType.SESSION_NOT_STARTING,
				null
			);
		}else if(!existingResult) {
			return new CreateSharedFileApiPayload(
				new BaseApiPayload(false, "You not join session"),
				CacheApiStatusType.YOU_NOT_JOIN_SESSION,
				null
			);
		}
		
		File file = fileCacheService.cacheSharedFile(input, userId);
		
		return new CreateSharedFileApiPayload(
			new BaseApiPayload(true, "Create shared file successfully"),
			null,
			fileMapper.toCreateSharedFilePayload(file)
		);
	}

	public UpdateSharedFileApiPayload updateSharedFile( UpdateSharedFileInput input, UUID userId) {
		Session session = sessionRepo.findById(UUID.fromString(input.baseFile().sessionId())).orElse(null);
		boolean existingResult = participantRepo.existsByUserIdAndSessionId(userId, UUID.fromString(input.baseFile().sessionId()));
		if(session == null) {
			return new UpdateSharedFileApiPayload(
				new BaseApiPayload(false, "Session not found"),
				CacheApiStatusType.SESSION_NOT_FOUND,
				null
			);
		}else if(!session.getStatus().equals(Status.STARTING)) {
			return new UpdateSharedFileApiPayload(
				new BaseApiPayload(false, "Session not starting"),
				CacheApiStatusType.SESSION_NOT_STARTING,
				null
			);
		}else if(!existingResult) {
			return new UpdateSharedFileApiPayload(
				new BaseApiPayload(false, "You not join session"),
				CacheApiStatusType.YOU_NOT_JOIN_SESSION,
				null
			);
		}
		
		File file = fileCacheService.updateSharedFile(input, userId);
		
		return new UpdateSharedFileApiPayload(
			new BaseApiPayload(true, "Update shared file successfully"),
			null,
			fileMapper.toUpdateSharedFilePayload(file)
		);
	}

	public DeleteSharedFileApiPayload deleteSharedFile(@Valid DeleteSharedFileInput input, UUID userId) {
		Session session = sessionRepo.findById(UUID.fromString(input.sessionId())).orElse(null);
		boolean existingResult = participantRepo.existsByUserIdAndSessionId(userId, UUID.fromString(input.sessionId()));
		if(session == null) {
			return new DeleteSharedFileApiPayload(
				new BaseApiPayload(false, "Session not found"),
				CacheApiStatusType.SESSION_NOT_FOUND,
				null
			);
		}else if(!session.getStatus().equals(Status.STARTING)) {
			return new DeleteSharedFileApiPayload(
				new BaseApiPayload(false, "Session not starting"),
				CacheApiStatusType.SESSION_NOT_STARTING,
				null
			);
		}else if(!existingResult) {
			return new DeleteSharedFileApiPayload(
				new BaseApiPayload(false, "You not join session"),
				CacheApiStatusType.YOU_NOT_JOIN_SESSION,
				null
			);
		}
		
		fileCacheService.deleteSharedFile(input, userId);
		
		return new DeleteSharedFileApiPayload(
			new BaseApiPayload(true, "Delete shared file successfully"),
			null,
			fileMapper.toDeleteSharedFilePayload(UUID.fromString(input.id()))
		);
	}
	

}
