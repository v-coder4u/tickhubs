package tickhubs.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;
import java.util.List;

/**
 * $ created by Vaibhav Varshney on : Aug 30, 2020
 */

@Getter
@Setter
public class PollResponse {
	private Long id;
	private String question;
	private List<ChoiceResponse> choices;
	private UserSummary createdBy;
	private Date creationDateTime;
	private Instant expirationDateTime;
	private Boolean isExpired;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Long selectedChoice;
	private Long totalVotes;
}
