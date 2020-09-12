package tickhubs.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * $ created by Vaibhav Varshney on : Aug 30, 2020
 */

@Getter
@Setter
public class PollRequest {
	@NotBlank
	@Size(max = 140)
	private String question;

	@NotNull
	@Size(min = 2, max = 6)
	@Valid
	private List<ChoiceRequest> choices;

	@NotNull
	@Valid
	private PollLength pollLength;

	private Long tagId;
}
