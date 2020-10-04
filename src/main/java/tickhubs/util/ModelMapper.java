package tickhubs.util;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import tickhubs.dto.RoomResponse;
import tickhubs.model.Poll;
import tickhubs.model.Room;
import tickhubs.model.User;
import tickhubs.dto.ChoiceResponse;
import tickhubs.dto.PollResponse;
import tickhubs.dto.UserSummary;

/**
 * $ created by Vaibhav Varshney on : Aug 30, 2020
 */

public class ModelMapper {

	public static PollResponse mapPollToPollResponse(Poll poll, User creator, Long userVote) {
		PollResponse pollResponse = new PollResponse();
		pollResponse.setId(poll.getId());
		pollResponse.setQuestion(poll.getQuestion());
		pollResponse.setCreationDateTime(poll.getCreatedDate());
		pollResponse.setExpirationDateTime(poll.getExpirationDateTime());
		Instant now = Instant.now();
		pollResponse.setIsExpired(poll.getExpirationDateTime().isBefore(now));

		List<ChoiceResponse> choiceResponses = poll.getChoices().stream().map(choice -> {
			ChoiceResponse choiceResponse = new ChoiceResponse();
			choiceResponse.setId(choice.getId());
			choiceResponse.setText(choice.getText());
			choiceResponse.setVoteCount(choice.getCount());
			return choiceResponse;
		}).collect(Collectors.toList());

		pollResponse.setChoices(choiceResponses);
		UserSummary creatorSummary = new UserSummary(creator.getId(), creator.getUsername(), creator.getName());
		pollResponse.setCreatedBy(creatorSummary);

		long totalVotes = pollResponse.getChoices().stream().mapToLong(ChoiceResponse::getVoteCount).sum();
		pollResponse.setTotalVotes(totalVotes);

		return pollResponse;
	}

	public static RoomResponse mapRoomToRoomResponse(Room room, User creator) {
		RoomResponse roomResponse = new RoomResponse();
		roomResponse.setId(room.getId());
		roomResponse.setCreationDateTime(room.getCreatedDate());
		roomResponse.setName(room.getName());
		roomResponse.setWebsite(room.getWebsite());
		roomResponse.setDescription(room.getDescription());
		UserSummary creatorSummary = new UserSummary(creator.getId(), creator.getUsername(), creator.getName());
		roomResponse.setCreatedByUser(creatorSummary);
		long totalPollsCount = 1;
		roomResponse.setRoomType(room.getRoomType());
		roomResponse.setTotalPolls(totalPollsCount);
		return roomResponse;
	}

}
