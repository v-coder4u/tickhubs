package tickhubs.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * $ created by Vaibhav Varshney on : Aug 30, 2020
 */
@Getter
@Setter
public class ChoiceRequest {
	@NotBlank
	@Size(max = 40)
	private String text;
}
