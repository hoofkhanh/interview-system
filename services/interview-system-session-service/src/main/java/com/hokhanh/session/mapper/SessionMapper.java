package com.hokhanh.session.mapper;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.hokhanh.common.user.response.UserByEmailPayload;
import com.hokhanh.session.client.auth.BaseAuthPayload;
import com.hokhanh.session.common.SessionStatusType;
import com.hokhanh.session.model.Session;
import com.hokhanh.session.model.Status;
import com.hokhanh.session.request.session.common.BaseSessionInput;
import com.hokhanh.session.response.session.common.BaseSessionPayload;
import com.hokhanh.session.response.session.createSession.CreateSessionPayload;
import com.hokhanh.session.response.session.deleteSession.DeleteSessionPayload;
import com.hokhanh.session.response.session.joinSession.JoinSessionPayload;
import com.hokhanh.session.response.session.joinSession.MemberPayload;
import com.hokhanh.session.response.session.sessions.BasePagePayload;
import com.hokhanh.session.response.session.sessions.SessionsPayload;
import com.hokhanh.session.response.session.updateSession.UpdateSessionPayload;

@Service
public class SessionMapper {

	public Session toSession(BaseSessionInput baseSession, UUID creatorId, String link) {
		return Session.builder()
				.creatorId(creatorId)
				.questionId(UUID.fromString(baseSession.questionId()))
				.link(link)
				.startTime(baseSession.startTime())
				.status(Status.valueOf(baseSession.status().name()))
				.title(baseSession.title())
				.githubLink(baseSession.githubLink())
				.build();
	}
	
	private BaseSessionPayload toBaseSessionPayload(Session session) {
		return new BaseSessionPayload(
			session.getId(),
			session.getCreatorId(),
			session.getQuestionId(),
			session.getLink(),
			session.getStartTime(),
			SessionStatusType.valueOf(session.getStatus().name()),
			session.getTitle(),
			session.getGithubLink()
		);
	}

	public CreateSessionPayload toCreateSessionPayload(Session session) {
		return new CreateSessionPayload(toBaseSessionPayload(session));
	}

	public UpdateSessionPayload toUpdateSessionPayload(Session session) {
		return new UpdateSessionPayload(toBaseSessionPayload(session));
	}

	public DeleteSessionPayload toDeleteSessionPayload(Session session) {
		return new DeleteSessionPayload(session.getId());
	}

	public JoinSessionPayload toJoinSessionPayload(Session session, List<UserByEmailPayload> usersById, List<BaseAuthPayload> auths) {
		 Map<UUID, String> userFullNameMap = usersById.stream()
		            .collect(Collectors.toMap(
		                    u -> u.baseUser().id(),
		                    u -> u.baseUser().fullName()
		            ));

		 List<MemberPayload> members = auths.stream()
	            .filter(a -> userFullNameMap.containsKey(a.userId())) // chỉ lấy userId có trong cả 2
	            .map(a -> new MemberPayload(
	                    a.userId(),
	                    userFullNameMap.get(a.userId()),
	                    a.role() != null ? a.role().name() : null
	            ))
	            .toList();
		 
		 return new JoinSessionPayload(members, toBaseSessionPayload(session));
	}

	public SessionsPayload toSessions(Page<Session> session) {
		BasePagePayload basePage = new BasePagePayload(
				session.getSize(), 
				session.getNumber(), 
				session.getTotalElements(), 
				session.getTotalPages()
		);
		List<BaseSessionPayload> payloads=  session.map(s -> toBaseSessionPayload(s)).toList();
		return new SessionsPayload(payloads, basePage);
	}
}
