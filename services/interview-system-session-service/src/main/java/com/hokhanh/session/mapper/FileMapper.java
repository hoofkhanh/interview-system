package com.hokhanh.session.mapper;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.hokhanh.session.redis.file.File;
import com.hokhanh.session.response.sharedFile.common.BaseFilePayload;
import com.hokhanh.session.response.sharedFile.createSharedFile.CreateSharedFilePayload;
import com.hokhanh.session.response.sharedFile.deleteSharedFile.DeleteSharedFilePayload;
import com.hokhanh.session.response.sharedFile.updateSharedFile.UpdateSharedFilePayload;

@Service
public class FileMapper {

	private BaseFilePayload toBaseFilePayload(File file) {
		return new BaseFilePayload(
			file.id(),
			file.folderId(),
			file.content(),
			file.name()
		);
	}
	
	public CreateSharedFilePayload toCreateSharedFilePayload(File file) {
		return new CreateSharedFilePayload(toBaseFilePayload(file));
	}

	public UpdateSharedFilePayload toUpdateSharedFilePayload(File file) {
		return new UpdateSharedFilePayload(toBaseFilePayload(file));
	}
	
	public DeleteSharedFilePayload toDeleteSharedFilePayload(UUID id) {
		return new DeleteSharedFilePayload(id);
	}

}
