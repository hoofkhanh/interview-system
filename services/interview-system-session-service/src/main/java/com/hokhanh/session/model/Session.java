package com.hokhanh.session.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.hokhanh.session.request.session.updateSession.UpdateSessionInput;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Session {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(nullable = false)
	private UUID creatorId;
	
	@Column(nullable = false)
	private UUID questionId;
	
	@OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Participant> participants;
	
	@Column(nullable = false, unique = true , columnDefinition = "TEXT")
	private String link;
	
	private LocalDateTime endTime;
	
	@Column(nullable = false)
	private LocalDateTime startTime;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@Column(nullable = false)
	private String title;
	
	@Column(columnDefinition = "TEXT")
	private String githubLink;
	
	public void updateFromInput(UpdateSessionInput input) {
		this.setQuestionId(UUID.fromString(input.baseSession().questionId()));
		this.setStartTime(input.baseSession().startTime());
		this.setStatus(Status.valueOf(input.baseSession().status().name()));
		this.setTitle(input.baseSession().title());
		this.setGithubLink(input.baseSession().githubLink());
		
	}
}
