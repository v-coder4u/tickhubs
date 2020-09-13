package tickhubs.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import org.springframework.util.CollectionUtils;
import tickhubs.dto.*;
import tickhubs.exception.BadRequestException;
import tickhubs.exception.ResourceNotFoundException;
import tickhubs.model.*;
import tickhubs.repository.PollRepository;
import tickhubs.repository.TagRepository;
import tickhubs.repository.UserRepository;
import tickhubs.repository.VoteRepository;
import tickhubs.security.UserPrincipal;
import tickhubs.util.AppConstants;
import tickhubs.util.CoreUtil;
import tickhubs.util.ModelMapper;

import javax.persistence.Lob;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * $ created by Vaibhav Varshney on : Aug 30, 2020
 */

@Service
public class PollServiceImpl {

	@Autowired
	private PollRepository pollRepository;

	@Autowired
	private VoteRepository voteRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TagRepository tagRepository;


	private static final Logger logger = LoggerFactory.getLogger(PollServiceImpl.class);

	public PagedResponse<PollResponse> getAllPolls(UserPrincipal currentUser, int page, int size) {
		validatePageNumberAndSize(page, size);

		// Retrieve Polls
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
		Page<Poll> polls = pollRepository.findAll(pageable);

		if (polls.getNumberOfElements() == 0) {
			return new PagedResponse<>(Collections.emptyList(), polls.getNumber(), polls.getSize(),
					polls.getTotalElements(), polls.getTotalPages(), polls.isLast());
		}

		// Map Polls to PollResponses containing vote counts and poll creator details
		List<Long> pollIds = polls.map(Poll::getId).getContent();
		Map<Long, Long> pollUserVoteMap = getPollUserVoteMap(currentUser, pollIds);
		Map<Long, User> creatorMap = getPollCreatorMap(polls.getContent());

		List<PollResponse> pollResponses = polls.map(poll -> {
			return ModelMapper.mapPollToPollResponse(poll, creatorMap.get(poll.getCreatedBy()),
					pollUserVoteMap == null ? null : pollUserVoteMap.getOrDefault(poll.getId(), null));
		}).getContent();

		return new PagedResponse<>(pollResponses, polls.getNumber(), polls.getSize(), polls.getTotalElements(),
				polls.getTotalPages(), polls.isLast());
	}

	public PagedResponse<PollResponse> getPollsCreatedBy(String username, UserPrincipal currentUser, int page,
			int size) {
		validatePageNumberAndSize(page, size);

		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

		// Retrieve all polls created by the given username
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
		Page<Poll> polls = pollRepository.findByCreatedBy(user.getId(), pageable);

		if (polls.getNumberOfElements() == 0) {
			return new PagedResponse<>(Collections.emptyList(), polls.getNumber(), polls.getSize(),
					polls.getTotalElements(), polls.getTotalPages(), polls.isLast());
		}

		// Map Polls to PollResponses containing vote counts and poll creator details
		List<Long> pollIds = polls.map(Poll::getId).getContent();
		Map<Long, Long> pollUserVoteMap = getPollUserVoteMap(currentUser, pollIds);

		List<PollResponse> pollResponses = polls.map(poll -> {
			return ModelMapper.mapPollToPollResponse(poll, user,
					pollUserVoteMap == null ? null : pollUserVoteMap.getOrDefault(poll.getId(), null));
		}).getContent();

		return new PagedResponse<>(pollResponses, polls.getNumber(), polls.getSize(), polls.getTotalElements(),
				polls.getTotalPages(), polls.isLast());
	}

	public PagedResponse<PollResponse> getPollsVotedBy(String username, UserPrincipal currentUser, int page, int size) {
		validatePageNumberAndSize(page, size);

		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

		// Retrieve all pollIds in which the given username has voted
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
		Page<Long> userVotedPollIds = voteRepository.findVotedPollIdsByUserId(user.getId(), pageable);

		if (userVotedPollIds.getNumberOfElements() == 0) {
			return new PagedResponse<>(Collections.emptyList(), userVotedPollIds.getNumber(),
					userVotedPollIds.getSize(), userVotedPollIds.getTotalElements(), userVotedPollIds.getTotalPages(),
					userVotedPollIds.isLast());
		}

		// Retrieve all poll details from the voted pollIds.
		List<Long> pollIds = userVotedPollIds.getContent();

		Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
		List<Poll> polls = pollRepository.findByIdIn(pollIds, sort);

		// Map Polls to PollResponses containing vote counts and poll creator details
		Map<Long, Long> pollUserVoteMap = getPollUserVoteMap(currentUser, pollIds);
		Map<Long, User> creatorMap = getPollCreatorMap(polls);

		List<PollResponse> pollResponses = polls.stream().map(poll -> {
			return ModelMapper.mapPollToPollResponse(poll, creatorMap.get(poll.getCreatedBy()),
					pollUserVoteMap == null ? null : pollUserVoteMap.getOrDefault(poll.getId(), null));
		}).collect(Collectors.toList());

		return new PagedResponse<>(pollResponses, userVotedPollIds.getNumber(), userVotedPollIds.getSize(),
				userVotedPollIds.getTotalElements(), userVotedPollIds.getTotalPages(), userVotedPollIds.isLast());
	}

	public Poll createPoll(PollRequest pollRequest) {
		Poll poll = new Poll();
		poll.setQuestion(pollRequest.getQuestion());

		pollRequest.getChoices().forEach(choiceRequest -> {
			poll.addChoice(new Choice(choiceRequest.getText()));
		});

		Instant now = Instant.now();
		Instant expirationDateTime = now.plus(Duration.ofDays(pollRequest.getPollLength().getDays()))
				.plus(Duration.ofHours(pollRequest.getPollLength().getHours()));

		poll.setExpirationDateTime(expirationDateTime);

		User user = userRepository.getOne(CoreUtil.getCurrentUserId());
		poll.setCreatedBy(user);

		Tag tag = tagRepository.getOne(pollRequest.getTagId());
		poll.setTag(tag);
		return pollRepository.save(poll);
	}

	public PollResponse getPollById(Long pollId, UserPrincipal currentUser) {
		Poll poll = pollRepository.findById(pollId)
				.orElseThrow(() -> new ResourceNotFoundException("Poll", "id", pollId));

		// Retrieve Vote Counts of every choice belonging to the current poll
		List<ChoiceVoteCount> votes = voteRepository.countByPollIdGroupByChoiceId(pollId);

		Map<Long, Long> choiceVotesMap = votes.stream()
				.collect(Collectors.toMap(ChoiceVoteCount::getChoiceId, ChoiceVoteCount::getVoteCount));

		// Retrieve poll creator details
		User creator = userRepository.findById(poll.getCreatedBy().getId())
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", poll.getCreatedBy()));

		// Retrieve vote done by logged in user
		Vote userVote = null;
		if (currentUser != null) {
			userVote = voteRepository.findByUserIdAndPollId(currentUser.getId(), pollId);
		}

		return ModelMapper.mapPollToPollResponse(poll, creator,
				userVote != null ? userVote.getChoice().getId() : null);
	}

	public PollResponse castVoteAndGetUpdatedPoll(Long pollId, VoteRequest voteRequest, UserPrincipal currentUser) {
		Poll poll = pollRepository.findById(pollId)
				.orElseThrow(() -> new ResourceNotFoundException("Poll", "id", pollId));

		if (poll.getExpirationDateTime().isBefore(Instant.now())) {
			throw new BadRequestException("Sorry! This Poll has already expired");
		}

		User user = userRepository.getOne(currentUser.getId());

		Choice selectedChoice = poll.getChoices().stream()
				.filter(choice -> choice.getId().equals(voteRequest.getChoiceId())).findFirst()
				.orElseThrow(() -> new ResourceNotFoundException("Choice", "id", voteRequest.getChoiceId()));

		Vote vote = new Vote();
		vote.setPoll(poll);
		vote.setUser(user);
		vote.setChoice(selectedChoice);

		try {
			vote = voteRepository.save(vote);
		} catch (DataIntegrityViolationException ex) {
			logger.info("User {} has already voted in Poll {}", currentUser.getId(), pollId);
			throw new BadRequestException("Sorry! You have already cast your vote in this poll");
		}

		// -- Vote Saved, Return the updated Poll Response now --

		// Retrieve Vote Counts of every choice belonging to the current poll
		List<ChoiceVoteCount> votes = voteRepository.countByPollIdGroupByChoiceId(pollId);

		Map<Long, Long> choiceVotesMap = votes.stream()
				.collect(Collectors.toMap(ChoiceVoteCount::getChoiceId, ChoiceVoteCount::getVoteCount));

		// Retrieve poll creator details
		User creator = userRepository.findById(poll.getCreatedBy().getId())
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", poll.getCreatedBy()));

		return ModelMapper.mapPollToPollResponse(poll, creator, vote.getChoice().getId());
	}

	private void validatePageNumberAndSize(int page, int size) {
		if (page < 0) {
			throw new BadRequestException("Page number cannot be less than zero.");
		}

		if (size > AppConstants.MAX_PAGE_SIZE) {
			throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
		}
	}

	//Not In Use Now
	/*private Map<Long, Long> getChoiceVoteCountMap(List<Long> pollIds) {
		// Retrieve Vote Counts of every Choice belonging to the given pollIds
		List<ChoiceVoteCount> votes = voteRepository.countByPollIdInGroupByChoiceId(pollIds);

		Map<Long, Long> choiceVotesMap = votes.stream()
				.collect(Collectors.toMap(ChoiceVoteCount::getChoiceId, ChoiceVoteCount::getVoteCount));

		return choiceVotesMap;
	}*/

	private Map<Long, Long> getPollUserVoteMap(UserPrincipal currentUser, List<Long> pollIds) {
		// Retrieve Votes done by the logged in user to the given pollIds
		Map<Long, Long> pollUserVoteMap = null;
		if (currentUser != null) {
			List<Vote> userVotes = voteRepository.findByUserIdAndPollIdIn(currentUser.getId(), pollIds);

			pollUserVoteMap = userVotes.stream()
					.collect(Collectors.toMap(vote -> vote.getPoll().getId(), vote -> vote.getChoice().getId()));
		}
		return pollUserVoteMap;
	}

	private Map<Long, User> getPollCreatorMap(List<Poll> polls) {
		List<User> userIds = polls.stream().map(Poll::getCreatedBy).distinct().collect(Collectors.toList());
		List<Long> creatorIds = userIds.stream().map(User::getId).distinct().collect(Collectors.toList());

		List<User> creators = userRepository.findByIdIn(creatorIds);
		Map<Long, User> creatorMap = creators.stream().collect(Collectors.toMap(User::getId, Function.identity()));
		return creatorMap;
	}

	public ApiResponse<List<PollResponse>> getAllPollsByTagId(Long tagId) {
		ApiResponse<List<PollResponse>> response = new ApiResponse<>(true,"fetched Successfully");
		if(Objects.isNull(tagId)){
			return response;
		}

		Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "id");
		Instant instant = Instant.now();
		Page<Poll> polls = pollRepository.findAllByTagIdAndExpirationDateTimeGreaterThan(tagId, instant,pageable);
		if(polls.getNumberOfElements() != 0) {
			Map<Long, User> creatorMap = getPollCreatorMap(polls.getContent());
			List<PollResponse> pollResponses = polls.map(poll ->
					ModelMapper.mapPollToPollResponse(poll, poll.getCreatedBy(), null)).getContent();

			response.setData(pollResponses);
			return response;
		}
		return response;
	}
}
