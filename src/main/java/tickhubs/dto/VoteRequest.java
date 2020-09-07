package tickhubs.dto;

import javax.validation.constraints.NotNull;

/**
 * $ created by Vaibhav Varshney on : Aug 30, 2020
 */

public class VoteRequest {
	@NotNull
	private Long choiceId;

	public Long getChoiceId() {
		return choiceId;
	}

	public void setChoiceId(Long choiceId) {
		this.choiceId = choiceId;
	}
}
