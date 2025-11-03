package com.hokhanh.session.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.hokhanh.common.user.response.UserByEmailPayload;
import com.hokhanh.session.client.auth.AuthClient;
import com.hokhanh.session.client.auth.BaseAuthPayload;
import com.hokhanh.session.client.question.QuestionClient;
import com.hokhanh.session.client.user.UserClient;
import com.hokhanh.session.mapper.SessionMapper;
import com.hokhanh.session.model.Participant;
import com.hokhanh.session.model.Session;
import com.hokhanh.session.model.Status;
import com.hokhanh.session.repository.ParticipantRepository;
import com.hokhanh.session.repository.SessionRepository;
import com.hokhanh.session.request.session.createSession.CreateSessionInput;
import com.hokhanh.session.request.session.deleteSession.DeleteSessionInput;
import com.hokhanh.session.request.session.joinSession.JoinSessionInput;
import com.hokhanh.session.request.session.updateSession.UpdateSessionInput;
import com.hokhanh.session.response.common.BaseApiPayload;
import com.hokhanh.session.response.session.common.BaseSessionPayload;
import com.hokhanh.session.response.session.common.SessionApiStatusType;
import com.hokhanh.session.response.session.createSession.CreateSessionApiPayload;
import com.hokhanh.session.response.session.deleteSession.DeleteSessionApiPayload;
import com.hokhanh.session.response.session.joinSession.JoinSessionApiPayload;
import com.hokhanh.session.response.session.sessions.SessionsPayload;
import com.hokhanh.session.response.session.updateSession.UpdateSessionApiPayload;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SessionService {
	
	private final SessionRepository sessionRepo;
	private final QuestionClient questionClient;
	private final SessionMapper sessionMapper;
	private final ParticipantRepository participantRepo;
	private final UserClient userClient;
	private final AuthClient authClient;

	public CreateSessionApiPayload createSession(CreateSessionInput input, UUID userId) {
		UUID creatorId = questionClient.creatorIdByIdInternal(input.baseSession().questionId());
		if(creatorId ==  null) {
			return new CreateSessionApiPayload(
				new BaseApiPayload(false, "question not found"),
				SessionApiStatusType.QUESTION_NOT_FOUND,
				null
			);
		}else if(!creatorId.equals(userId)) {
			return new CreateSessionApiPayload(
				new BaseApiPayload(false, "you not owner"),
				SessionApiStatusType.YOU_NOT_OWNER,
				null
			);
		}
		
		boolean existingResult = sessionRepo.existsByTitleAndCreatorId(input.baseSession().title(), userId);
		if(existingResult) {
			return new CreateSessionApiPayload(
				new BaseApiPayload(false, "title duplicated"),
				SessionApiStatusType.TITLE_DUPLICATED,
				null
			);
		}
		
		String randomLink = UUID.randomUUID().toString();
		Session session = sessionRepo.save(sessionMapper.toSession(input.baseSession(), userId, randomLink));
		
		return new CreateSessionApiPayload(
			new BaseApiPayload(true, "Create successfully"),
			null,
			sessionMapper.toCreateSessionPayload(session)
		);
	}

	public UpdateSessionApiPayload updateSession( UpdateSessionInput input, UUID userId) {
		Session session = sessionRepo.findById(UUID.fromString(input.id())).orElse(null);
		if(session == null) {
			return new UpdateSessionApiPayload(
				new BaseApiPayload(false, "Session not found"),
				SessionApiStatusType.SESSION_NOT_FOUND,
				null
			);
		}else if(session.getStatus().equals(Status.ENDED)) {
			return new UpdateSessionApiPayload(
				new BaseApiPayload(false, "Session ended"),
				SessionApiStatusType.SESSION_ENDED,
				null
			);
		}else if(!session.getCreatorId().equals(userId)) {
			return new UpdateSessionApiPayload(
				new BaseApiPayload(false, "You not owner (session)"),
				SessionApiStatusType.YOU_NOT_OWNER,
				null
			);
		}
		
		boolean existingResult = sessionRepo.existsByTitleAndCreatorIdAndIdNot(input.baseSession().title(), userId, UUID.fromString(input.id()));
		if(existingResult) {
			return new UpdateSessionApiPayload(
				new BaseApiPayload(false, "Title duplicated"),
				SessionApiStatusType.TITLE_DUPLICATED,
				null
			);
		}
		
		UUID creatorId = questionClient.creatorIdByIdInternal(input.baseSession().questionId());
		if(creatorId ==  null) {
			return new UpdateSessionApiPayload(
				new BaseApiPayload(false, "Question not found"),
				SessionApiStatusType.QUESTION_NOT_FOUND,
				null
			);
		}else if(!creatorId.equals(userId)) {
			return new UpdateSessionApiPayload(
				new BaseApiPayload(false, "You not owner (question)"),
				SessionApiStatusType.YOU_NOT_OWNER,
				null
			);
		}
		
		session.updateFromInput(input);
		session = sessionRepo.save(session);
		
		return new UpdateSessionApiPayload(
			new BaseApiPayload(true, "Update successfully"),
			null,
			sessionMapper.toUpdateSessionPayload(session)
		);
	}

	public DeleteSessionApiPayload deleteSession( DeleteSessionInput input, UUID userId) {
		Session session = sessionRepo.findById(UUID.fromString(input.id())).orElse(null);
		if(session == null) {
			return new DeleteSessionApiPayload(
				new BaseApiPayload(false, "Session not found"),
				SessionApiStatusType.SESSION_NOT_FOUND, 
				null
			);
		}else if (session.getStatus().equals(Status.STARTING)) {
			return new DeleteSessionApiPayload(
				new BaseApiPayload(false, "Session starting"),
				SessionApiStatusType.SESSION_STARTING, 
				null
			);
		}else if(!session.getCreatorId().equals(userId)) {
			return new DeleteSessionApiPayload(
				new BaseApiPayload(false, "You not owner"),
				SessionApiStatusType.YOU_NOT_OWNER, 
				null
			);
		}
		
//		candidateEvaluationClient.deleteBySessionIdInternal(sessionId);
		
		sessionRepo.deleteById(UUID.fromString(input.id()));
		return new DeleteSessionApiPayload(
			new BaseApiPayload(true, "Delete successfully"),
			null,
			sessionMapper.toDeleteSessionPayload(session)
		);
	}

	public JoinSessionApiPayload joinSession( JoinSessionInput input, UUID userId) {
		Session session = sessionRepo.findById(UUID.fromString(input.sessionId())).orElse(null);
		if(session == null) {
			return new JoinSessionApiPayload(
				new BaseApiPayload(false, "Session not found"),
				SessionApiStatusType.SESSION_NOT_FOUND,
				null
			);
		}else if(session.getStartTime().isAfter(LocalDateTime.now())) {
			return new JoinSessionApiPayload(
				new BaseApiPayload(false, "Session pending"),
				SessionApiStatusType.SESSION_PENDING,
				null
			);
		}else if(session.getStatus().equals(Status.ENDED)) {
			return new JoinSessionApiPayload(
				new BaseApiPayload(false, "Session ended"),
				SessionApiStatusType.SESSION_ENDED,
				null
			);
		}
		
		if(session.getStatus().equals(Status.PENDING)) {
			session.setStatus(Status.STARTING);
			sessionRepo.save(session);
		}
		
		boolean existingResult = participantRepo.existsByUserIdAndSessionId(userId, session.getId());
		if(existingResult == false) {
			Participant participant = new Participant();
			participant.setUserId(userId);
			participant.setSession(session);
			participantRepo.save(participant);
			session.getParticipants().add(participant);
		}
		
		List<String> ids = session.getParticipants().stream().map(p -> p.getUserId().toString()).toList();
		List<UserByEmailPayload> usersById = userClient.usersByIdInternal(ids);
		List<BaseAuthPayload> auths = authClient.authsByUserId(ids);
		
		return new JoinSessionApiPayload(
			new BaseApiPayload(true, "Join session successfully"),
			null,
			sessionMapper.toJoinSessionPayload(session, usersById, auths)
		);
	}

	public SessionsPayload sessions(int page, int size, UUID userId) {
		Page<Session> session = sessionRepo.findByCreatorId(userId,  PageRequest.of(page, size));
		return sessionMapper.toSessions(session);
	}

	public UUID endSession(String sessionId, UUID userId) {
		Session session = sessionRepo.findById(UUID.fromString(sessionId)).orElse(null);
		if(session != null) {
			session.setStatus(Status.ENDED);
			session.setEndTime(LocalDateTime.now());
			sessionRepo.save(session);
			return session.getId();
		}
		
		return null;
	}
	

}
